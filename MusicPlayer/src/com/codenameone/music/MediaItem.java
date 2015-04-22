/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.util.EventDispatcher;
import com.codename1.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NoSuchTagException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * @author Martijn00
 */
public class MediaItem {

    private ConnectionRequest simulatorRequest;
    
    private Media media = null;
    private boolean is_cleaned = false;

    protected Map track;

    private final EventDispatcher statusChangeEvent;
    private final EventDispatcher coverLoadedEvent;
    private MediaStatus status = MediaStatus.PREPARING;

    private Image cover;
    private String coverUrl;
    private ConnectionRequest coverRequest;

    public MediaItem(String url, Map track, Boolean isOffline) throws IOException {
        Log.p("Start creating MediaItem");
        
        this.statusChangeEvent = new EventDispatcher();
        this.statusChangeEvent.setBlocking(false);

        this.coverLoadedEvent = new EventDispatcher();
        this.coverLoadedEvent.setBlocking(false);

        this.track = track;
        this.coverUrl = (String) ((Map)track.get("_meta")).get("attached_picture");

        final Runnable onFinished = new Runnable() {
            public void run() {
                if (is_cleaned) {
                    return;
                }

                setStatus(MediaStatus.FINISHED);
            }
        };
        
        String osname = System.getProperty("os.name");
        
        // Init media
        if(isOffline)
        {
            Log.p("Loading media from FileSystemStorage");
            final InputStream is = FileSystemStorage.getInstance().openInputStream(url);
            media = MediaManager.createMedia(is, "audio/mp3", onFinished);
        }
        //Workaround to make simulator play media with HTTPS by downloading it first
        else if(osname != null && (osname.toLowerCase().indexOf("windows") > -1 || osname.toLowerCase().indexOf("mac")  > -1))
        {
            Log.p("Workaround download media on: " + osname);
            simulatorRequest = new ConnectionRequest() {
                @Override
                protected void readResponse(InputStream input) throws IOException {
                    media = MediaManager.createMedia(input, "audio/mp3", onFinished);
                }
            };

            //simulatorRequest.setFailSilently(true);
            simulatorRequest.setPost(false);
            simulatorRequest.setUrl(url);

            UserProfile profile = Api.getInstance().getProfile();
            if (profile != null && profile.getToken() != null) {
                String credentials = Api.getInstance().getProfile().getUsername() + ":" + Api.getInstance().getProfile().getToken();

                simulatorRequest.addRequestHeader("Authorization", "Basic " + Base64.encodeNoNewline((credentials).getBytes()));
            }

            NetworkManager.getInstance().addToQueueAndWait(simulatorRequest);
        }
        else
        {
            Log.p("Loading media from url");
            media = MediaManager.createMedia(url, false, onFinished);
            
            // Start preloading for online media ...
            // TODO: Enable again when Shai got it fixed. A prepared media won't call the onFinished method but will just stop playing some milli-seconds before.
            //media.prepare();
        }
    }

    public void initCover()
    {
        final MediaItem obj = this;

        cover = null;

        if (MediaHelper.getState(track) == MediaHelper.DOWNLOADED) {
            // Try to get a loc
            try {
                String path = FileManager.getInstance().getFullPath(MediaHelper.getLocalFilename(track));
                AbstractID3v2Tag id3 = MediaHelper.getId3ByFile(path);

                byte[] albumImage = id3.getAlbumImage();
                if (albumImage != null) {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(albumImage);
                    cover = URLImage.create(imageStream);

                    Log.p("Cover image loaded from local file");
                    coverLoadedEvent.fireActionEvent(new ActionEvent(obj));

                    return;
                }
            } catch (IOException e) {
                // Do nothing
            } catch (InvalidDataException e) {
                // Do nothing
            } catch (UnsupportedTagException e) {
                // Do nothing
            } catch (NoSuchTagException e) {
                // Do nothing
            }
        }

        // If we're not online, just return here. There's nothing more we can do.
        if (!Api.getInstance().isOnline()) {
            Log.p("Cover image loaded: fallback - API is offline");
            coverLoadedEvent.fireActionEvent(new ActionEvent(obj));

            return;
        }

        if (coverUrl == null || coverUrl.equals("")) {
            Log.p("Cover image loaded: fallback - track has no cover");
            coverLoadedEvent.fireActionEvent(new ActionEvent(obj));

            return;
        }

        coverRequest = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {
                if(is_cleaned) {
                    return;
                }

                cover = URLImage.create(input);

                Log.p("Cover image loaded from server");
                coverLoadedEvent.fireActionEvent(new ActionEvent(obj));
            }

            @Override
            protected void handleIOException(IOException err) {
                //Cover could not be loaded because requested timed-out: do nothing
            }

            @Override
            protected void handleException(Exception err) {
                //Error 500: do nothing
            }
        };

        // Add Request to queue to be sent
        coverRequest.setPost(false);
        coverRequest.setUrl(coverUrl);

        // Add credentials to the request
        coverRequest.setCookiesEnabled(false);
        UserProfile profile = Api.getInstance().getProfile();
        if (profile != null && profile.getToken() != null) {
            String credentials = Api.getInstance().getProfile().getUsername() + ":" + Api.getInstance().getProfile().getToken();

            coverRequest.addRequestHeader("Authorization", "Basic " + Base64.encodeNoNewline((credentials).getBytes()));
        }

        Log.p("Cover request send, url: " + coverUrl);
        NetworkManager.getInstance().addToQueue(coverRequest);

        coverLoadedEvent.fireActionEvent(new ActionEvent(obj));
    }

    public void addStatusChangeListener(DataChangedListener listener) {
        statusChangeEvent.addListener(listener);
    }

    public void addCoverLoadedListener(ActionListener listener) {
        coverLoadedEvent.addListener(listener);
    }

    public synchronized MediaStatus getStatus() {
        if (is_cleaned) {
            return MediaStatus.STOPPED;
        }

        if (status == MediaStatus.PLAYING && !media.isPlaying()) {
            return MediaStatus.BUFFERING;
        }

        return status;
    }

    private synchronized void setStatus(MediaStatus status) {
        if (is_cleaned) {
            // Just to be sure, that every media, that once has been cleaned, is really cleaned. Also if an event (like FINISHED) is called later.
            if (status != MediaStatus.STOPPED) {
                dispose();
            }
            return;
        }

        this.status = status;

        statusChangeEvent.fireDataChangeEvent(0, status.ordinal());
    }

    public void play() {
        if(media != null)
        {
            is_cleaned = false;

            setStatus(MediaStatus.BUFFERING);
            media.play();

            // Call the timer every second from now on
            final int[] counter = {0};
            final Timer timeoutTimer = new Timer();
            timeoutTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (is_cleaned) {
                        timeoutTimer.cancel();
                        return;
                    }

                    counter[0]++;

                    // If it was already waiting for 10 sec, and the media still has no duration, time out.
                    if (counter[0] > 100 && media.getDuration() < 0) {
                        dispose();
                        setStatus(MediaStatus.TIMED_OUT);
                        timeoutTimer.cancel();
                        return;
                    }

                    if (media.getDuration() > 0) {
                        setStatus(MediaStatus.PLAYING);
                        timeoutTimer.cancel();
                        return;
                    }
                }
            }, 0, 100);
        }
        else
        {
            dispose();
        }
    }

    public void pause() {
        if (is_cleaned || media == null) {
            return;
        }

        setStatus(MediaStatus.PAUSED);
        media.pause();
    }

    public void setTime(int time) {
        if(media != null)
            media.setTime(time);
    }

    public int getTime() {
        if(media != null)
            return media.getTime();
        else
            return -1;
    }

    public int getDuration() {
        if(media != null)
            return media.getDuration();
        else
            return -1;
    }

    public void dispose() {
        setStatus(MediaStatus.STOPPED);
        is_cleaned = true;

        if(simulatorRequest != null)
            simulatorRequest.kill();
        
        if(coverRequest != null)
            coverRequest.kill();

        if(media != null)
            media.cleanup();
    }

    @Override
    public boolean equals(Object o) {
        int id = MediaHelper.getId(track);
        String language = (String)track.get("language");

        if (o instanceof Map) {
            if (((Map) o).containsKey("type") && "track".equals(((Map) o).get("type"))) {
                if (language.equals(((Map) o).get("language")) && id == MediaHelper.getId((Map) o)) {
                    Log.p("MediaRunnable: Instance is equal - " + id + " " + language);
                    return true;
                }
            }
        }

        if (o instanceof MediaItem) {
            if (this.track == ((MediaItem) o).track) {
                Log.p("MediaRunnable: Instance is equal - " + id + " " + language);
                return true;
            }
        }

        Log.p("MediaRunnable: Instance is not equal - " + id + " " + language);

        return false;
    }

    public void setVariable(String key, Object value) {
        media.setVariable(key, value);
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
