package com.codenameone.music.mediaplayer;

import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;

public class NativeAndroid {
    public static DataChangedListener getStatusChangeListener() {
        return null;

        //TODO: enable a proper notification here
        //ListModelHelper.prepareTrackForListing(media, false);
        //Display.getInstance().notifyStatusBar("MusicPlayer", (String)media.get("lblTitle"), (String)media.get("lblSecondaryTitle"), false, false, null);
    }

    public static ActionListener getPlayingListener() {
        return null;
    }

    public static ActionListener getCoverLoadedListener() {
        return null;
    }
}
