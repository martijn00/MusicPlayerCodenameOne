/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music.view;

import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import com.codenameone.music.PlayerStatus;
import userclasses.StateMachine;

/**
 *
 * @author mvandijk
 */
public class EmbPlayerView {
    protected StateMachine ui;
    
    private static Container EmbPlayerContainer;
    private static ArrayList<String> blockedViews = new ArrayList<String>();
    private Boolean isVisible = false;
    
    public EmbPlayerView(StateMachine ui)
    {
        this.ui = ui;
        
        blockedViews.add(LoginView.FORM_NAME);
        blockedViews.add(PlayerView.FORM_NAME);
        blockedViews.add(QueueView.FORM_NAME);
        blockedViews.add(ForgotPasswordView.FORM_NAME);
        blockedViews.add(CreateAccountView.FORM_NAME);

        ui.player.addStatusChangeListener(new DataChangedListener() {
            @Override
            public void dataChanged(int type, int index) {
                PlayerStatus status = PlayerStatus.values()[type];

                switch (status) {
                    case STOPPED:
                    case PAUSED:
                        if (isVisible) {
                            setPlayPauseButtonState(false);
                            updateEmbPlayer();
                        }
                        break;

                    // Is it necessary to update all data on the player when playing? Title at least doesn't change ... But keep an eye on the player's wake-up method
                    case PLAYING:
                    case LOADING:
                        setPlayPauseButtonState(true);
                        if (!isVisible) {
                            init(Display.getInstance().getCurrent());
                        } else {
                            updateEmbPlayer();
                        }
                        break;
                }
            }
        });

        ui.player.addPlayingListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isVisible) {
                    updateEmbPlayer();
                }
            }
        });
    }
    
    public void init(Form f)
    {
        if(EmbPlayerContainer == null)
        {
            EmbPlayerContainer = StateMachine.createContainer("EmbPlayerBottom");

            ui.findBtnOpenPlayer(EmbPlayerContainer).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    onOpenPlayerAction(evt);
                }
            });
            ui.findBtnPlayerPlayPause(EmbPlayerContainer).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    onPlayPauseAction(evt);
                }
            });
        }
        
        if(!blockedViews.contains(f.getName()) && !(f instanceof Dialog))
        {
            if(EmbPlayerContainer.getParent() != null)
            {
                EmbPlayerContainer.getParent().removeAll();
                isVisible = false;
            }

            if(!ui.player.getQueue().isEmpty())
            {
                f.getContentPane().addComponent(BorderLayout.SOUTH, EmbPlayerContainer);
                f.revalidate();
                isVisible = true;
                updateEmbPlayer();
            }
        }
    }

    public void updateEmbPlayer() {
        if(!Display.getInstance().isMinimized())
        {
            int playedTime = ui.player.getPosition();
            int totalTime = ui.player.getDuration();

            Map media = ui.player.getCurrent();
            if(media != null)
                ui.findLblSongTitle(EmbPlayerContainer).setText((String)media.get("lblTitle"));

            ui.findLblPlayerCurrentTime(EmbPlayerContainer).setText(ui.playerView.formatTime(playedTime));
            ui.findSldPlayerTime(EmbPlayerContainer).setProgress((int)((float)playedTime/(float)totalTime*100));
        }
    }
    
    public void setPlayPauseButtonState(final boolean isPlaying) {
        if (Display.getInstance().getCurrent() != null && SideMenuBar.isShowing()) {
            ui.sideMenu.getInstance().getParentForm().addShowListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    setPlayPauseButtonState(isPlaying);
                }
            });
        } else {
            Image icon = StateMachine.getResourceFile().getImage(isPlaying ? "miniplayer_pause.png" : "miniplayer_play.png");
            Image iconPressed = StateMachine.getResourceFile().getImage(isPlaying ? "miniplayer_pause.png" : "miniplayer_play.png");

            icon.lock();
            iconPressed.lock();

            ui.findBtnPlayerPlayPause(EmbPlayerContainer).setIcon(icon);
            ui.findBtnPlayerPlayPause(EmbPlayerContainer).setPressedIcon(iconPressed);
        }
    }
    
    public void onOpenPlayerAction(ActionEvent event) {
        Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 200));
        ui.playerView.show(true);
    }
    
    public void onPlayPauseAction(ActionEvent event) {
        ui.player.playPause();
    }
}
