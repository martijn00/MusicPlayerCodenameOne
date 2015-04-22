/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.ui.Container;
import com.codename1.ui.Slider;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.util.HashMap;
import java.util.Map;
import com.codenameone.music.api.IJsonResponseHandler;
import com.codenameone.music.view.ListModelHelper;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class PlaylistHelper {

    private static final Map<Integer, Map<String, Object>> downloadingRequests = new HashMap<Integer, Map<String, Object>>();

    public static boolean isPlaylistDownloading(Playlist playlist)
    {
        return downloadingRequests.containsKey(playlist.getId());
    }

    public static Integer getPlaylistDownloadingProgress(Playlist playlist)
    {
        return (Integer) downloadingRequests.get(playlist.getId()).get("progress");
    }

    public static void killPlaylistDownloadingProgress(Playlist playlist)
    {
        ((ConnectionRequest)downloadingRequests.get(playlist.getId()).get("request")).kill();
    }

    public static void downloadPlaylist(final Playlist playlist, Container ctnPlaylistProgress)
    {
        if (ctnPlaylistProgress != null) {
            StateMachine.getInstance().resetComponentHeight(ctnPlaylistProgress);
            ctnPlaylistProgress.setHeight(StateMachine.getPixelFromMM(1,false));
        }
        downloadPlaylistItem(playlist, 0);
    }

    private static void downloadPlaylistItem(final Playlist playlist, final int index)
    {
        // If there is a previews item, update it's listing.
        Map prevItem = playlist.getItemAt(index-1);
        if (prevItem != null)
            ListModelHelper.prepareTrackForListing(prevItem);

        if (Api.getInstance().canDownload() && index < playlist.getSize()) {
            Map item = playlist.getItemAt(index);

            // Is the item already downloaded or downloading?
            if (MediaHelper.getState(item) != MediaHelper.NOT_DOWNLOADED) {
                downloadPlaylistItem(playlist, index+1);
                return;
            }

            Api.getInstance().getTrack(new IJsonResponseHandler() {

                @Override
                public void onSuccess(Map track, Map<String, String> headers) {
                    playlist.setItem(index, track);

                    final Map<String, Object> data = new HashMap<String, Object>();
                    data.put("progress", 0);
                    data.put("request", MediaHelper.download(track, new ActionListener() {

                        /**
                         * This method is stateless and can be called from every playlist and in every view.
                         */
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            /**
                             * If the connection has been killed or another error
                             * occurred, finish downloading by simulating that the last
                             * item has been downloaded.
                             */
                            if (((NetworkEvent)evt).getError() != null)
                                downloadPlaylistItem(playlist, playlist.getSize());

                            int progress = ((NetworkEvent)evt).getProgressPercentage();

                            int totalProgress = index * 100 + progress;
                            data.put("progress", totalProgress);

                            // Update the slider only if it's the correct playlist you're viewing.
                            if (StateMachine.getInstance().playlistView.getPlaylistId().intValue() == playlist.getId().intValue()) {
                                Slider slider = StateMachine.getInstance().findSldPlaylistDownloadingProgress();
                                Map downloadingRequest = downloadingRequests.get(playlist.getId());
                                if (slider != null && downloadingRequest != null) {
                                    // If the progress hasn't changed, don't do anything on the ui.
                                    if (slider.getProgress() != (Integer)downloadingRequest.get("progress")) {
                                        slider.setProgress((Integer)downloadingRequest.get("progress"));
                                    }
                                }
                            }

                            if (progress == 100)
                                downloadPlaylistItem(playlist, index+1);
                        }
                    }));

                    downloadingRequests.put(playlist.getId(), data);

                    ListModelHelper.prepareTrackForListing(track);
                    // Update playlist-view while downloading
                    if (StateMachine.getInstance().findCtlPlaylistMediaItems() != null)
                        StateMachine.getInstance().findCtlPlaylistMediaItems().getModel().setSelectedIndex(StateMachine.getInstance().findCtlPlaylistMediaItems().getModel().getSelectedIndex());

                    // If you've switched to player while downloading
                    if (StateMachine.getInstance().findCtlPlayerQueue() != null)
                        StateMachine.getInstance().findCtlPlayerQueue().getModel().setSelectedIndex(StateMachine.getInstance().findCtlPlayerQueue().getModel().getSelectedIndex());
                }

                @Override
                public void onFailure(int code, String message, Map<String, String> headers) {
                    downloadPlaylistItem(playlist, index);
                }
            }, MediaHelper.getId(item), (String)item.get("language"), false);
        } else {
            downloadingRequests.remove(playlist.getId());

            // TODO: Please take a look at this guy here ... think this won't work with parallel download of playlists
            Container ctn = StateMachine.getInstance().findCtnPlaylistProgress();
            if (ctn != null) {
                StateMachine.getInstance().hideComponent(ctn);
            }
        }
    }
}
