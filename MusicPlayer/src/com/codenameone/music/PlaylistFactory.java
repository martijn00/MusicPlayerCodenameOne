/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Display;
import com.mpatric.mp3agic.AbstractID3v2Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codenameone.music.api.IJsonResponseHandler;
import com.codenameone.music.api.IListResponseHandler;
import com.codenameone.music.api.IResponseHandler;
import com.codenameone.music.api.IStringResponseHandler;

/**
 *
 * @author Martijn00
 */
public class PlaylistFactory {
    
    private static PlaylistFactory INSTANCE;

    private PlaylistFactory() {
    }

    public static PlaylistFactory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlaylistFactory();
        }
        return INSTANCE;
    }

    public List<Map> getOfflineTracks()
    {
        return getOfflineTracks(false);
    }

    public List<Map> getOfflineTracks(boolean forceRebuilding)
    {
        if (!Storage.getInstance().exists("offline_tracks") || forceRebuilding) {
            Storage.getInstance().writeObject("offline_tracks", rebuildOfflineTrackList());
        }

        return (List<Map>)Storage.getInstance().readObject("offline_tracks");
    }

    private List<Map> rebuildOfflineTrackList()
    {
        List<Map> playlist = new ArrayList<Map>();
        String[] files = FileManager.getInstance().listFiles();

        if (files == null) {
            return playlist;
        }

        for (String file : files) {

            String[] tmp_parts2;
            String[] tmp_parts3 = null;

            tmp_parts2 = Util.split(file, "_");
            if (tmp_parts2.length == 2) {
                tmp_parts3 = Util.split(tmp_parts2[1], ".");
            }

            if (tmp_parts2.length == 2 && tmp_parts3 != null && tmp_parts3.length == 2) {

                Integer id = Integer.parseInt(tmp_parts2[0]);
                String language = tmp_parts3[0];

                Map track;

                // Try to get the track from the cache ...
                Map trackDocument = Api.getInstance().getTrackFromCache(id, language);
                if (trackDocument != null) {
                    track = trackDocument;
                } else {

                    // If the track isn't stored in the cache, try to read the ID3 data
                    track = new HashMap();
                    track.put("id", id);
                    track.put("language", language);

                    try {
                        AbstractID3v2Tag id3 = MediaHelper.getId3ByFile(FileManager.getInstance().getFullPath(file));

                        Map<String, String> metaData = new HashMap<String, String>();
                        metaData.put("album", id3.getAlbum());
                        metaData.put("subtype", "unknown");

                        track.put("title", id3.getTitle());
                        track.put("_meta", metaData);
                    } catch (Exception ex) {

                        // Set up a final variable, that we can change in a runnable, and run a request for a track.
                        final List<Map> _list = new ArrayList<Map>();
                        Api.getInstance().getTrack(new IJsonResponseHandler() {
                            @Override
                            public void onSuccess(Map data, Map<String, String> headers) {
                                _list.add(new HashMap());
                            }

                            @Override
                            public void onFailure(int code, String message, Map<String, String> headers) {
                                _list.add(new HashMap());
                            }
                        }, id, language, true);

                        // Wait for the response to come in ...
                        Display.getInstance().invokeAndBlock(new Runnable() {
                            @Override
                            public void run() {
                                while (_list.size() == 0) {
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        // Do nothing ...
                                    }
                                }

                            }
                        });

                        // And get the response.
                        Map _trackDocument = Api.getInstance().getTrackFromCache(id, language);
                        if (_trackDocument != null) {
                            track = _trackDocument;
                        } else {
                            // If that also fails, I don't know what's left to try ...
                            Map<String, String> metaData = new HashMap<String, String>();
                            metaData.put("album", "-unknown-");
                            metaData.put("subtype", "unknown");

                            track.put("title", "-unknown-");
                            track.put("_meta", metaData);
                        }
                    }
                }

                playlist.add(track);
            }
        }
        
        return playlist;
    }

    public void addOfflineTrack(Map item)
    {
        List<Map> playlist = getOfflineTracks();
        playlist.add(item);

        Storage.getInstance().writeObject("offline_tracks", playlist);
    }

    public List<Map<String, Object>> getLocalPlaylists() {
        List<Map<String, Object>> list = (List<Map<String, Object>>)Storage.getInstance().readObject("Playlists");
        
        if (list != null) {
            for (Map<String, Object> playlist : list) {
                Integer id = Integer.parseInt(String.valueOf(playlist.get("id")));

                playlist.put("offline_available", (getLocalPlaylist(id) != null && getLocalPlaylist(id).getOfflineAvailable()));
            }

            return list;
        } else {
            return new ArrayList<Map<String, Object>>();
        }
    }

    private void setLocalPlaylists(List<Map<String, Object>> playlists)
    {
        // Get a list of ids of the now saved playlists
        List<Integer> playlistIds = new ArrayList<Integer>();
        for (Map<String, Object> playlist : playlists) {
            playlistIds.add((Integer)playlist.get("id"));
        }

        // Loop through the old playlists to check if a playlist has been removed. Remove it locally then as well.
        List<Map<String, Object>> oldPlaylists = getLocalPlaylists();
        for (Map<String, Object> playlist : oldPlaylists) {
            int playlistId = (Integer)playlist.get("id");
            if (!playlistIds.contains(playlistId)) {
                setLocalPlaylist(playlistId, null);
            }
        }
        
        Storage.getInstance().writeObject("Playlists", playlists);
    }

    public boolean hasLocalPlaylists() {
        return getLocalPlaylists().size() > 0;
    }

    public ConnectionRequest getPlaylists(final IListResponseHandler _handler) {
        List<Map<String, Object>> playlists = getLocalPlaylists();
        
        final IListResponseHandler handler = new IListResponseHandler() {

            public void onSuccess(List playlists, Map<String, String> headers) {
                if (_handler != null && playlists != null) {

                    for (Object item : playlists) {
                        Map<String, Object> playlist = (Map<String, Object>)item;
                        Integer id = Integer.parseInt(String.valueOf(playlist.get("id")));

                        playlist.put("offline_available", (getLocalPlaylist(id) != null && getLocalPlaylist(id).getOfflineAvailable()));
                    }

                    _handler.onSuccess(playlists, new HashMap<String, String>());
                }
            }

            public void onFailure(int code, String message, Map<String, String> headers) {
                if (_handler != null) {
                    _handler.onFailure(code, message, headers);
                }
            }
        };
        
        if (playlists != null) {
            handler.onSuccess(playlists, new HashMap<String, String>());
        }

        if (Api.getInstance().isOnline()) {
            return Api.getInstance().getPlaylists(new IJsonResponseHandler() {

                @Override
                public void onSuccess(Map data, Map<String, String> headers) {
                    ArrayList<Map<String, Object>> playlists = new ArrayList<Map<String, Object>>();

                    ArrayList<Map> lists = (ArrayList<Map>)data.get("root");
                    for (Map playlist : lists) {
                        int id;
                        if (playlist.get("id") instanceof Double) {
                            id = ((Double) playlist.get("id")).intValue();
                        } else {
                            id = Integer.parseInt(playlist.get("id").toString());
                        }


                        Map<String, Object> values = new HashMap<String, Object>();
                        values.put("id", id);
                        values.put("name", (String) playlist.get("name"));
                        values.put("track_count", ((Double) playlist.get("track_count")).intValue());

                        playlists.add(values);
                    }

                    setLocalPlaylists(playlists);

                    if (handler != null) {
                        handler.onSuccess(playlists, headers);
                    }
                }

                @Override
                public void onFailure(int code, String message, Map<String, String> headers) {
                    if (handler != null) {
                        handler.onFailure(code, message, headers);
                    }
                }
            });
        } else {
            return null;
        }
    }

    private Map<String, Object> getRemotePlaylistModel(Playlist playlist)
    {
        Map<String, Object> data = new HashMap<String, Object>();
        
        data.put("name", playlist.getName());
        data.put("type", "track_collection");

        List<Map> trackReferences = new ArrayList<Map>();

        for (int i = 0; playlist.getSize() > i; i++) {
            Map item = playlist.getItemAt(i);

            Map ref = new HashMap<String, String>();
            ref.put("id", item.get("id"));
            ref.put("language", item.get("language"));

            trackReferences.add(ref);
        }
        data.put("track_references", trackReferences);

        List<String> access = new ArrayList<String>();
        access.add(Api.getInstance().getProfile().getUsername());
        data.put("access", access);

        return data;
    }

    public ConnectionRequest createPlaylist(final String text, final IStringResponseHandler handler)
    {
        return Api.getInstance().addPlaylist(new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                List<Map<String, Object>> playlists = getLocalPlaylists();

                try {
                    Map<String, Object> playlist = new HashMap<String, Object>();
                    playlist.put("id", Integer.parseInt(headers.get("X-Document-Id")));
                    playlist.put("name", text);
                    playlist.put("track_count", 0);
                    playlist.put("offline_available", false);
                    playlists.add(playlist);

                    setLocalPlaylists(playlists);
                } catch (NumberFormatException e) {
                    // The list is anyways re-loaded on a getPlaylist() call.
                }

                handler.onSuccess(headers);
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        }, getRemotePlaylistModel(new Playlist(text)));
    }

    private Playlist getLocalPlaylist(int key)
    {
        return (Playlist) Storage.getInstance().readObject("Playlist:"+key);
    }

    private void setLocalPlaylist(int key, Playlist playlist)
    {
        if (playlist != null) {
            Storage.getInstance().writeObject("Playlist:"+key, playlist);
        } else {
            Storage.getInstance().deleteStorageFile("Playlist:"+key);
        }

        // Only trigger cleanup if the playlist is removed
        if (playlist == null) {
            PlaylistFactory.getInstance().cleanupDownloadedFiles();
        }
    }

    public ConnectionRequest getPlaylist(final int key, final IPlaylistResponseHandler handler)
    {        
        Playlist playlist = getLocalPlaylist(key);
        if (playlist != null) {
            handler.onSuccess(playlist, new HashMap<String, String>());
        }

        if (Api.getInstance().isOnline()) {
            return Api.getInstance().getPlaylist(new IJsonResponseHandler() {

                @Override
                public void onSuccess(Map data, Map<String, String> headers) {
                    final Playlist playlist = new Playlist(data);

                    Playlist oldPlaylist = getLocalPlaylist(key);
                    if (oldPlaylist != null && oldPlaylist.getOfflineAvailable()) {
                        playlist.setOfflineAvailable(true);
                    }

                    setLocalPlaylist(key, playlist);

                    handler.onSuccess(playlist, headers);
                }

                @Override
                public void onFailure(int code, String message, Map<String, String> headers) {
                    handler.onFailure(code, message, headers);
                }
            }, key);
        } else {
            return null;
        }
    }
    
    public void setPlaylistOfflineAvailable(int key, boolean offline_available)
    {
        Playlist playlist = getLocalPlaylist(key);

        playlist.setOfflineAvailable(offline_available);

        setLocalPlaylist(playlist.getId(), playlist);

        // Only trigger cleanup if the playlist will not be offline available
        if (!playlist.getOfflineAvailable()) {
            PlaylistFactory.getInstance().cleanupDownloadedFiles();
        }
    }

    public ConnectionRequest savePlaylist(final Playlist playlist, final IStringResponseHandler handler)
    {
        return Api.getInstance().setPlaylist(new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                if (handler != null) {
                    setLocalPlaylist(playlist.getId(), playlist);

                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        }, playlist.getId(), getRemotePlaylistModel(playlist));
    }

    public ConnectionRequest deletePlaylist(final int playlistId, final IStringResponseHandler handler)
    {
        return Api.getInstance().deletePlaylist(new IStringResponseHandler() {

            @Override
            public void onSuccess(Map<String, String> headers) {
                
                // Remove the deleted playlist from the playlists-list
                List<Map<String, Object>> playlists = getLocalPlaylists();
                for (Map<String, Object> playlist : playlists) {
                    if ((Integer)playlist.get("id") == playlistId) {
                        playlists.remove(playlist);
                        break;
                    }
                }
                setLocalPlaylists(playlists);

                setLocalPlaylist(playlistId, null);

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        }, playlistId);
    }

    /**
     * Delete all files that are not linked to a playlist that is offline available
     */
    private void cleanupDownloadedFiles()
    {
        // Gather a list of files that are linked to a playlist, being offline available
        List<String> filesToKeep = new ArrayList<String>();
        for(Map<String, Object> playlistData : getLocalPlaylists()) {
            Playlist localPlaylist = getLocalPlaylist((Integer)playlistData.get("id"));
            if (localPlaylist != null && localPlaylist.getOfflineAvailable()) {
                for (int i = 0; i < localPlaylist.getSize(); i++) {
                    if (MediaHelper.getState(localPlaylist.getItemAt(i)) == MediaHelper.DOWNLOADED) {
                        filesToKeep.add(MediaHelper.getLocalFilename(localPlaylist.getItemAt(i)));
                    }
                }
            }
        }

        // Loop through all files in the system. If they're not linked to a playlist, delete them.
        String[] files = FileManager.getInstance().listFiles();

        if (files != null) {
            for(String filename : files) {
                if (filename.endsWith(".mp3") && !filesToKeep.contains(filename)) {
                    FileManager.getInstance().delete(filename);
                }
            }
        }
    }

    public interface IPlaylistResponseHandler extends IResponseHandler {

        public void onSuccess(Playlist playlist, Map<String, String> headers);
    }
}
