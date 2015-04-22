/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music.view;

import com.codename1.ui.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codenameone.music.MediaHelper;
import com.codenameone.music.Playlist;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class ListModelHelper {

    static private final Map<String, String> icons = new HashMap<String, String>();

    static private void initIcons()
    {
        String[] types = new String[]{"soundbook", "soundbook_downloaded", "song", "song_downloaded", "speech", "speech_downloaded", "featured", "folder"};
        for(String type : types) {
            icons.put(type, type);
        }
    }

    static private Image getImage(String type, boolean isDownloaded)
    {
        if (icons.isEmpty()) {
            initIcons();
        }

        if (isDownloaded) {
            type = type + "_downloaded";
        }

        if (icons.containsKey(type)) {
            Image icon = StateMachine.getResourceFile().getImage("category_" + icons.get(type) + ".png");
            icon.lock();

            return icon;
        } else {
            // We have song and song_downloaded as image.
            return getImage("song", isDownloaded);
        }
    }

    static public void prepareAlbumsForListing(ArrayList<Map> albums)
    {
        if (albums != null) {
            for (Map album : albums) {
                _prepareAlbumForListing(album);
            }
        }
    }
    
    static private void _prepareAlbumForListing(Map album)
    {
        album.put("btnMediaIcon", getImage("folder", false));
        album.put("lblTitle", album.get("title"));
    }

    // A document can be either a track, an album or a contributor
    static public void prepareDocumentsForListing(ArrayList<Map> documents)
    {
        if (documents != null) {
            for (Map document : documents) {
                if ("track".equals(document.get("type"))) {
                    _prepareTrackForListing(document);
                } else if ("album".equals(document.get("type"))) {
                    document.put("lblTitle", String.valueOf(document.get("title")));
                    document.put("lblSecondaryTitle", "");
                    document.put("btnMediaIcon", StateMachine.getResourceFile().getImage("category_folder.png"));
                }
            }
        }
    }

    static public void prepareTracksForListing(ArrayList<Map> tracks)
    {
        if (tracks != null) {
            for (Map track : tracks) {
                _prepareTrackForListing(track);
            }
        }
    }

    static public void preparePlaylistForListing(Playlist playlist)
    {
        for (int i = 0; i < playlist.getSize(); i++) {
            _prepareTrackForListing(playlist.getItemAt(i));
        }
    }

    static private void _prepareTrackForListing(Map track)
    {
        updatePlayingIcon(track, MediaHelper.isCurrentInPlayer(track));

        generateTrackTitles(track);
    }

    static public void prepareTrackForListing(Map track)
    {
        _prepareTrackForListing(track);
    }

    static public void updatePlayingIcon(Map track, boolean isPlaying)
    {
        String type = (String)track.get("subtype");
        if(isPlaying) {
            track.put("btnMediaIcon", StateMachine.getResourceFile().getImage("category_playing.png"));
        } else {
            track.put("btnMediaIcon", getImage(type, MediaHelper.getState(track) == MediaHelper.DOWNLOADED));
        }
    }

    static private void generateTrackTitles(Map track)
    {
        List<String> title = new ArrayList<String>();

        // Get generated title for singsong or exegesis
        if (track.get("_meta") != null && ((Map)track.get("_meta")).get("title") != null) {
            Map meta = ((Map)track.get("_meta"));
            if ("singsong".equals(track.get("subtype")) || "exegesis".equals(track.get("subtype"))) {
                title.add((String) meta.get("title"));
            }
        }

        // Get default title, if there was no generated one
        if (title.isEmpty() && track.get("title") != null) {
            title.add((String) track.get("title"));
        }

        // List of interprets
        if (track.get("rel") != null) {
            List<String> interprets = new ArrayList<String>();
            for (Object val : (List)track.get("rel")) {
                Map rel = (Map) val;

                if ("interpret".equals(rel.get("type"))) {
                    interprets.add((String) rel.get("name"));
                }
            }

            if (!interprets.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                builder.append(interprets.remove(0));

                for (String s : interprets) {
                    builder.append( ", ");
                    builder.append( s);
                }

                title.add(builder.toString());
            }
        }

        // Album names
        if (track.get("_meta") != null && ((Map)track.get("_meta")).get("album") != null) {
            title.add((String) ((Map)track.get("_meta")).get("album"));
        }

        if (title.size() < 2) {
            title.add(0, (String) track.get("subtype"));
        }
        track.put("lblTitle", String.valueOf(title.get(0)));
        
        if (title.size() > 1) {
            track.put("lblSecondaryTitle", String.valueOf(title.get(1)));
        } else {
            track.put("lblSecondaryTitle", "");
        }
    }
}
