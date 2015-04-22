/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.FileSystemStorage;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import com.codename1.util.StringUtil;
import com.dusbabek.lib.id3.TagHeader;
import com.dusbabek.lib.id3.TagHeaderFactory;
import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.ID3v2TagFactory;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NoSuchTagException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.codenameone.music.api.FileDownloadRequest;
import com.codenameone.music.view.DialogNotice;
import userclasses.StateMachine;

/**
 * This class is a helper to the Map, that is put into a playlist as a
 * song. It helps you f.e. to find out if the track is offline available.
 *
 * @author Martijn00
 */
public class MediaHelper {

    public static final int NOT_DOWNLOADED = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOADED = 2;
    private static final java.util.List<String> downloadingTracks = new ArrayList<String>();

    private static int getInteger(Object idObj)
    {
        int id;
        if (idObj instanceof Double)
            id = ((Double)idObj).intValue();
        else
            id = (Integer)idObj;

        return id;
    }

    /**
     * Tracks often have a double, but sometimes an integer as id.
     * @param track
     * @return
     */
    public static int getId(Map track)
    {
        return getInteger(track.get("id"));
    }

    public static int getParentId(Map track) {
        return getInteger(track.get("parent_id"));
    }

    private static Boolean isDownloaded(Map track)
    {
        return FileManager.getInstance().exists(
                getLocalFilename(track));
    }

    private static Boolean isDownloading(Map track)
    {
        return downloadingTracks.contains(getLocalFilename(track));
    }

    public static Boolean isCurrentInPlayer(Map track)
    {
        Map current = StateMachine.getInstance().player.getCurrent();
        return current != null && MediaHelper.getId(current) == MediaHelper.getId(track);
    }

    public static String getLocalFilename(Map track)
    {
        return getId(track) + "_" + track.get("language") + ".mp3";
    }

    /**
     * Downloads a file to the local storage.
     * If you call it even if the file is downloaded, it'll be overwritten.
     * Track must be loaded here! Please make sure that it is by calling isReady()
     * Please make also sure the file-system is ready by calling FileManager.isReady()
     *
     * @param track
     * @param onProgressUpdate
     * @return 
     */
    public static FileDownloadRequest download(final Map track, final ActionListener onProgressUpdate)
    {
        downloadingTracks.add(getLocalFilename(track));

        final String tmpFilename = "_" + getLocalFilename(track);

        final FileDownloadRequest request = new FileDownloadRequest() {
            @Override
            protected void readResponse(InputStream is) throws IOException
            {
                try {
                    super.readResponse(is);
                } catch (IOException ex) {
                    DialogNotice.show(
                            StateMachine._translate("dialog_download_download", "[Default] Download failed"),
                            StringUtil.replaceAll(StateMachine._translate("dialog_download_track", "[Default] Downloading of the track {track.title} failed"), "{track.title}", (String)track.get("lblTitle")),
                            null
                    );
                    kill();
                }

                if (!isKilled())
                    FileManager.getInstance().rename(tmpFilename, getLocalFilename(track));
                else
                    // Remove the file, because it's just partially downloaded.
                    FileManager.getInstance().delete(tmpFilename);
            }

            @Override
            public void kill() {
                super.kill();

                downloadingTracks.remove(getLocalFilename(track));

                onProgressUpdate.actionPerformed(new NetworkEvent(this, new Exception()));
            }
        };

        // Add the location, the file should be saved to (use temp, so we are sure it's not fully downloaded if the app crashes)
        request.setDestinationFile(FileManager.getInstance().getFullPath(tmpFilename));

        ActionListener progressListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent _evt) {
                NetworkEvent evt = (NetworkEvent) _evt;

                // This is the request we started here - not any else.
                if (evt.getConnectionRequest() == request)
                {
                    // We don't want the -1 at the start ;)
                    if (evt.getProgressPercentage() < 0)
                        return;

                    // Remove it after this connection reached 100%
                    if (evt.getProgressPercentage() == 100) {
                        PlaylistFactory.getInstance().addOfflineTrack(track);
                        NetworkManager.getInstance().removeProgressListener(this);
                        downloadingTracks.remove(getLocalFilename(track));
                    }

                    onProgressUpdate.actionPerformed(evt);
                }
            }
        };

        // To update download-progress ...
        NetworkManager.getInstance().addProgressListener(progressListener);

        // Add Request to queue to be sent
        request.setPost(false);

        String url = null;
        for (Map itemMedia : (ArrayList<Map>)track.get("media")) {
            if ("audio".equals(itemMedia.get("type"))) {
                Map file = ((ArrayList<Map>)itemMedia.get("files")).get(0);
                url = (String)file.get("url");
            }
        }

        if (url == null) {
            onProgressUpdate.actionPerformed(new NetworkEvent(request, new Exception()));
            return request;
        }

        request.setUrl(url);

        // Add credentials to the request
        request.setCookiesEnabled(false);
        UserProfile profile = Api.getInstance().getProfile();
        if (profile != null && profile.getToken() != null) {
            String credentials = Api.getInstance().getProfile().getUsername() + ":" + Api.getInstance().getProfile().getToken();

            request.addRequestHeader("Authorization", "Basic " + Base64.encodeNoNewline((credentials).getBytes()));
        }

        NetworkManager.getInstance().addToQueue(request);

        return request;
    }

    public static int getState(Map track) {
        if (isDownloading(track))
            return DOWNLOADING;
        else if (isDownloaded(track))
            return DOWNLOADED;
        else
            return NOT_DOWNLOADED;
    }

    public static AbstractID3v2Tag getId3ByFile(String path) throws IOException, UnsupportedTagException, InvalidDataException, NoSuchTagException {
        InputStream in = FileSystemStorage.getInstance().openInputStream(path);

        // Get the size of the ID3 header and load only that into memory
        TagHeader tagHeader = TagHeaderFactory.makeHeader(in);
        int dataSize = (int) tagHeader.getDataSize();
        byte[] data = new byte[dataSize];

        in = FileSystemStorage.getInstance().openInputStream(path);

        int read = in.read(data);
        if (read != dataSize)
            throw new IOException("Expected " + dataSize + " bytes.");

        // Use the 2nd library to parse the ID3 tag.
        return ID3v2TagFactory.createTag(data);
    }
}
