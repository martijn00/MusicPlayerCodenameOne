package com.codenameone.music.mediaplayer;

import com.codename1.media.Media;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codenameone.music.PlayerStatus;
import com.codenameone.music.view.ListModelHelper;
import userclasses.StateMachine;
import java.util.Map;

public class NativeIOS {
    public static DataChangedListener getStatusChangeListener() {
        return new DataChangedListener() {
            @Override
            public void dataChanged(int type, int index) {
                PlayerStatus status = PlayerStatus.values()[type];

                switch (status) {
                    case PLAYING:
                        StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_DURATION, (long) StateMachine.getInstance().player.getDuration());
                        break;

                    case LOADING:
                        Map media = StateMachine.getInstance().player.getCurrent();

                        ListModelHelper.prepareTrackForListing(media);
                        StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_TITLE, media.get("lblTitle"));
                        StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_ARTIST, media.get("lblSecondaryTitle"));
                        StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_ALBUM_COVER, StateMachine.getInstance().player.getCover());
                        break;
                }
            }
        };
    }

    public static ActionListener getPlayingListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Integer milisecTime = StateMachine.getInstance().player.getPosition();
                long longTime = milisecTime.longValue();

                StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_POSITION, longTime);
            }
        };
    }

    public static ActionListener getCoverLoadedListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                StateMachine.getInstance().player.setVariable(Media.VARIABLE_BACKGROUND_ALBUM_COVER, StateMachine.getInstance().player.getCover());
            }
        };
    }
}
