package com.codenameone.music.view;

import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.list.DefaultListModel;
import com.codenameone.music.MediaHelper;
import userclasses.StateMachine;
import java.util.Collection;
import java.util.Map;

public class TrackListModel extends DefaultListModel<Map> {

    private int currentTrack = 0;
    private int setTrackIndex = -1;
    
    public TrackListModel() {
        super();
        
        StateMachine.getInstance().player.addStatusChangeListener(new DataChangedListener() {
            public void dataChanged(int type, int index) {
                setSelectedIndex(getSelectedIndex());
            }
        });
    }

    public TrackListModel(Collection<Map> items) {
        super(items);
    }

    @Override
    public Map getItemAt(int index) {

        Map track = super.getItemAt(index);

        if (track != null && "track".equals(track.get("type"))) {
            boolean trackIsCurrentInPlayer = MediaHelper.isCurrentInPlayer(track) && (setTrackIndex == index || currentTrack != MediaHelper.getId(track));
            boolean hasPlayingIcon = track.get("btnMediaIcon") == StateMachine.getResourceFile().getImage("category_playing.png");

            // !! This is a BINARY OR !! If either one is true, the result is true. Else it's false.
            if (hasPlayingIcon | trackIsCurrentInPlayer) {
                ListModelHelper.updatePlayingIcon(track, trackIsCurrentInPlayer);
                currentTrack = MediaHelper.getId(track);
                setTrackIndex = index;
            }
        }

        return track;
    }
}
