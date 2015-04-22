/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.util.EventDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.codenameone.music.view.TrackListModel;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class MediaPlayer {

    private MediaItem currentMedia;
    private final EventDispatcher statusChangeEvent;
    private final EventDispatcher playingEvent;
    private final EventDispatcher coverLoadedEvent;
    private PlayerStatus status = PlayerStatus.STOPPED;
    private Queue queue;
    private Timer timer;

    // These variables are just to keep the cover for the next track. If he has the same cover, we don't need to load it again.
    private Image cover;
    private String coverUrl;

    public MediaPlayer() {
        queue = (Queue)Storage.getInstance().readObject("queue");

        if (queue == null) {
            queue = new Queue();
        }

        // communicates using a thread-agnostic dispatcher
        this.statusChangeEvent = new EventDispatcher();
        this.statusChangeEvent.setBlocking(false);

        this.playingEvent = new EventDispatcher();
        this.playingEvent.setBlocking(false);

        this.coverLoadedEvent = new EventDispatcher();
        this.coverLoadedEvent.setBlocking(false);

        timer = new Timer();
    }

    /**
     * Called when the device is waking up.
     * This will update the player-data when the 
     */
    public void onWakeUp()
    {
        statusChangeEvent.fireDataChangeEvent(0, status.ordinal());
        playingEvent.fireActionEvent(null);
    }

    /*******************************************************************************************************************
     *
     * The players status management
     *
     ******************************************************************************************************************/

    public synchronized PlayerStatus getStatus() {
        if (currentMedia == null) {
            return PlayerStatus.STOPPED;
        }

        switch(currentMedia.getStatus()) {
            case PREPARING:
                return PlayerStatus.LOADING;

            case PLAYING:
                return PlayerStatus.PLAYING;

            case PAUSED:
                return PlayerStatus.PAUSED;

            case BUFFERING:
                return PlayerStatus.LOADING;

            case FINISHED:
            case TIMED_OUT:
            case STOPPED:
                return PlayerStatus.STOPPED;
        }

        // Unidentified state ... should never happen.
        return PlayerStatus.STOPPED;
    }
    
    private synchronized void setStatus(PlayerStatus status)
    {
        Log.p("PlayerStatus changed to: " + status);
        this.status = status;

        statusChangeEvent.fireDataChangeEvent(0, status.ordinal());

        switch (status) {
            case STOPPED:
            case PAUSED:
                timer.cancel();
                timer = new Timer();
                break;

            case PLAYING:
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        playingEvent.fireActionEvent(null);
                    }
                }, 0, 1000);
                break;
        }
    }

    /*******************************************************************************************************************
     *
     * Get from current media
     *
     ******************************************************************************************************************/

    public Image getCover()
    {
        if (cover == null) {
            return StateMachine.getResourceFile().getImage("player_cover_fallback.png");
        } else {
            return cover;
        }
    }

    public int getPosition() {
        if (currentMedia == null) {
            return 0;
        }

        int time = currentMedia.getTime();

        if (time < 0) {
            return 0;
        }

        return time;
    }

    public int getDuration() {
        if (currentMedia == null) {
            return -1;
        }

        return currentMedia.getDuration();
    }

    /*******************************************************************************************************************
     *
     * Actions on the current media
     *
     ******************************************************************************************************************/

    public void play()
    {
        final ActionListener coverActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MediaItem eventMedia = (MediaItem)evt.getSource();

                // This is triggered if the loading of the cover took too much time (it runs in background). The currentMedia is already gone. Therefore check if currentMedia still is the media this cover has been loaded for.
                if (eventMedia.equals(currentMedia)) {
                    coverUrl = eventMedia.getCoverUrl();
                    cover = eventMedia.getCover();

                    Log.p("Cover loaded.");
                    coverLoadedEvent.fireActionEvent(new ActionEvent(null));
                }
            }
        };

        if (currentMedia != null) {
            // Since now it's interesting to get informed if the cover has been loaded. Case: If the track is preloaded, but the cover is not initialized.
            currentMedia.addCoverLoadedListener(coverActionListener);

            // If the cover of the pre-loaded element already is loaded, we have to set it. The trigger won't fire if the cover already is loaded.
            if (coverUrl == null || !coverUrl.equals(currentMedia.getCoverUrl())) {
                cover = currentMedia.getCover();
            }
        }

        setStatus(PlayerStatus.LOADING);
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                try {
                    // Rebuild currentMedia if it's STOPPED or offline available
                    if(currentMedia != null && (currentMedia.getStatus() == MediaStatus.STOPPED || MediaHelper.getState(queue.getCurrent()) == MediaHelper.DOWNLOADED)) {
                        currentMedia.dispose();
                        currentMedia = null;
                    }

                    final Map currentInQueue = queue.getCurrent();
                    if (currentInQueue == null) {
                        log("The queue has no current element ..");
                        currentMedia = null;
                        return;
                    }

                    if (currentMedia == null) {
                        currentMedia = createMediaByTrack(currentInQueue, coverActionListener);
                    }

                    currentMedia.addStatusChangeListener(new DataChangedListener() {
                        public void dataChanged(int type, int index) {
                            MediaStatus status = MediaStatus.values()[type];

                            Log.p("MediaStatus changed to: " + status.toString());

                            switch (status) {
                                // Buffering is the first status when calling play() and it's never explicitly set on any other position.
                                case BUFFERING:
                                    setStatus(PlayerStatus.LOADING);
                                    break;
                                case FINISHED:
                                    playNext();
                                    break;
                                case TIMED_OUT:
                                    setStatus(PlayerStatus.STOPPED);
                                    break;
                                case PLAYING:
                                    setStatus(PlayerStatus.PLAYING);
                                    break;
                                case STOPPED:
                                    currentMedia = null;
                                    setStatus(PlayerStatus.STOPPED);
                                    break;
                            }
                        }
                    });
                    currentMedia.play();
                } catch (MediaNotFoundException e) {
                    log(e.getMessage());
                } catch (IOException e) {
                    log(e.getMessage());
                }
            }
        });
    }

    public void pause() {
        Display.getInstance().invokeAndBlock(new Runnable() {
            public void run() {
                if (currentMedia != null) {
                    currentMedia.pause();
                    setStatus(PlayerStatus.PAUSED);
                } else {
                    setStatus(PlayerStatus.STOPPED);
                }
            }
        });
    }

    public void stop() {
        Display.getInstance().invokeAndBlock(new Runnable() {
            public void run() {
                if (currentMedia != null) {
                    currentMedia.dispose();
                }
            }
        });
    }

    public void resume() {
        Display.getInstance().invokeAndBlock(new Runnable() {
            public void run() {
                log("Resume");
                if (currentMedia != null && currentMedia.getStatus() == MediaStatus.PAUSED) {
                    currentMedia.play();
                    setStatus(PlayerStatus.PLAYING);
                } else {
                    log("Media not found, playing current in queue");
                    play();
                }
            }
        });
    }

    public void setPosition(final int percent) {
        Display.getInstance().invokeAndBlock(new Runnable() {
            public void run() {
                if (currentMedia == null) {
                    setStatus(PlayerStatus.STOPPED);
                    return;
                }

                currentMedia.setTime((int) ((float) percent / 100 * (float) currentMedia.getDuration()));
            }
        });
    }

    public void setVariable(String key, Object value) {
        if(currentMedia != null)
            currentMedia.setVariable(key, value);
    }

    /*******************************************************************************************************************
     *
     * Queue actions
     *
     ******************************************************************************************************************/

    public boolean getRepeat() {
        return queue.getRepeat();
    }

    public void setRepeat(final Boolean repeat) {
        queue.setRepeat(repeat);
    }

    public boolean getShuffle() {
        return queue.getShuffle();
    }

    public void setShuffle(final Boolean shuffle) {
        Display.getInstance().invokeAndBlock(new Runnable() {
            public void run() {
                queue.setShuffle(shuffle);
            }
        });
    }

    public boolean hasNext()
    {
        return queue.hasNext();
    }

    public boolean hasPrevious()
    {
        return queue.hasPrevious();
    }

    public Map getCurrent()
    {
        return queue.getCurrent();
    }

    public ArrayList<Map> getQueue()
    {
        ArrayList<Map> list = new ArrayList<Map>();

        if (queue.getCurrent() != null) {
            list.add(queue.getCurrent());
        }

        list.addAll(queue.getUserQueue());
        list.addAll(queue.getNextQueue());

        if (queue.getRepeat()) {
            list.addAll(queue.getPreviousQueue());
        }

        return list;
    }

    public boolean isQueueEmpty() {
        // Return TRUE if ...
        return
                // No item is currently playing
                queue.getCurrent() == null
                        // The user-queue is empty
                        && queue.getUserQueue().isEmpty()
                        // The next-queue is empty
                        && queue.getNextQueue().isEmpty()
                        // The REPEAT option is not activated or the previous-queue is empty
                        && (!queue.getRepeat() || queue.getPreviousQueue().isEmpty())
                ;
    }

    public void setQueueAndPlay(final TrackListModel playlist)
    {
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                // Start playing the selected item before the playlist is saved. This seems to shorten the time before the player can start playing.
                stop();

                queue.setQueue(playlist);
                play();
            }
        });
    }

    public void addToQueue(Map item)
    {
        boolean isQueueEmpty = isQueueEmpty();

        queue.add(item);

        if(isQueueEmpty && queue.hasNext())
            playNext();
    }

    public void removeFromQueue(Map item)
    {
        boolean isCurrent = (item == queue.getCurrent());

        queue.removeItem(item);

        if (isCurrent) {
            if(hasNext())
                playNext();
            else
                stop();
        }
    }

    /**
     * This method MUST NOT be used. It's just in here because CN1 has such limited support for Generics in Java ...
     * @deprecated
     */
    public void setQueueAndPlay(DefaultListModel<Map> playlist)
    {
        setQueueAndPlay((TrackListModel)playlist);
    }

    /*******************************************************************************************************************
     *
     * Register listeners at the players events
     *
     ******************************************************************************************************************/

    public void addStatusChangeListener(DataChangedListener listener) {
        statusChangeEvent.addListener(listener);
    }

    public void addPlayingListener(ActionListener listener) {
        playingEvent.addListener(listener);
    }

    public void addCoverLoadedListener(ActionListener coverLoadedListener) {
        coverLoadedEvent.addListener(coverLoadedListener);
    }

    /*******************************************************************************************************************
     *
     * Helper ...
     *
     ******************************************************************************************************************/

    private MediaItem createMediaByTrack(Map track, ActionListener coverActionListener) throws MediaNotFoundException, IOException {
        String url = null;
        Boolean isOffline = false;

        if (MediaHelper.getState(track) == MediaHelper.DOWNLOADED)
        {
            String path = FileManager.getInstance().getFullPath(MediaHelper.getLocalFilename(track));
            url = path;
            isOffline = true;
        }
        else {
            for (Map itemMedia : (ArrayList<Map>)track.get("media")) {
                if ("audio".equals(itemMedia.get("type"))) {
                    Map file = ((ArrayList<Map>)itemMedia.get("files")).get(0);
                    url = (String)file.get("url");
                }
            }

            // There was no playable media in here. Go on and try playing the next.
            if (url == null) {
                log("No audio found in " + MediaHelper.getId(track));
                throw new MediaNotFoundException();
            }

            if (url.startsWith("https://")) {
                url = "http://"
                        // Maybe we have an "@" in the username ...
                        + Util.encodeUrl(Api.getInstance().getProfile().getUsername())
                        + ":"
                        + Util.encodeUrl(Api.getInstance().getProfile().getToken())
                        + "@"
                        + url.substring(8);
            }
        }

        Log.p("url: " + url);

        final MediaItem media = new MediaItem(url, track, isOffline);
        Log.p("new Media created");

        // This should be null if the track is pre-loading.
        if (coverActionListener != null) {
            media.addCoverLoadedListener(coverActionListener);
        }

        if (coverUrl == null || !coverUrl.equals(media.getCoverUrl())) {
            // Reset cover variables
            coverUrl = null;
            cover = null;

            // Load the cover in a separate thread. It may take time to read the ID3 header of the file or where ever the image actually comes from.
            Display.getInstance().scheduleBackgroundTask(new Runnable() {
                public void run() {
                    media.initCover();
                }
            });
        } else {
            media.setCover(cover);
            Log.p("Reuse loaded cover.");
        }

        return media;
    }

    private static void log(String str) {
        Log.p("MediaPlayer: " + str);
    }

    public void playPause() {
        if (status == PlayerStatus.PAUSED || status == PlayerStatus.STOPPED) {
            resume();
        }
        else { // Playing or loading ...
            pause();
        }
    }

    public void playNext() {
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                if (queue.hasNext()) {
                    setStatus(PlayerStatus.LOADING);
                    if(currentMedia != null)
                        currentMedia.dispose();
                    queue.setNextAsCurrent();
                    play();
                }
            }
        });
    }

    public void playPrevious() {
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                if (queue.hasPrevious()) {
                    setStatus(PlayerStatus.LOADING);
                    if(currentMedia != null)
                        currentMedia.dispose();
                    queue.setPreviousAsCurrent();
                    play();
                }
            }
        });
    }
}