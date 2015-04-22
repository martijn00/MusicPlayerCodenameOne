/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import java.util.Map;
import com.codenameone.music.PlayerStatus;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class PlayerView {

    protected StateMachine ui;
    private Boolean isSliderDragging = false;
    private Boolean isFromMiniPlayer = false;

    public static String FORM_NAME = "ScreenPlayer";

    // Just to prevent initialisation without parameters ...
    private PlayerView() {
    }

    public PlayerView(final StateMachine ui) {
        ui.player.addStatusChangeListener(new DataChangedListener() {
            @Override
            public void dataChanged(int type, int index) {
                // If the PlayerView is not shown, do nothing.
                if (Display.getInstance().isMinimized() || Display.getInstance().getCurrent() == null || !FORM_NAME.equals(Display.getInstance().getCurrent().getName())) {
                    return;
                }

                PlayerStatus status = PlayerStatus.values()[type];
                switch (status) {
                    case STOPPED:
                        updatePlayer();
                        setLoadingSpinnerVisibility(false);
                        setPlayPauseButtonState(false);
                        updateSelection();
                        break;
                    case PAUSED:
                        updatePlayer();
                        setLoadingSpinnerVisibility(false);
                        setPlayPauseButtonState(false);
                        break;
                    // Is it necessary to update all data on the player when playing? Title at least doesn't change ... But keep an eye on the player's wake-up method
                    case PLAYING:
                        updatePlayer();
                        setLoadingSpinnerVisibility(false);
                        setPlayPauseButtonState(true);
                        break;
                    case LOADING:
                        updateCover();
                        updatePlayer();
                        setLoadingSpinnerVisibility(true);
                        setPlayPauseButtonState(true);
                        updateSelection();
                        break;
                }
            }
        });

        ui.player.addPlayingListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!Display.getInstance().isMinimized() && Display.getInstance().getCurrent() != null && FORM_NAME.equals(Display.getInstance().getCurrent().getName())) {
                    updatePlayer();
                }
            }
        });

        ui.player.addCoverLoadedListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!Display.getInstance().isMinimized() && Display.getInstance().getCurrent() != null && FORM_NAME.equals(Display.getInstance().getCurrent().getName())) {
                    updateCover();
                }
            }
        });

        this.ui = ui;
    }

    public void show() {
        show(false);
    }

    public void show(Boolean fromMiniPlayer) {
        this.isFromMiniPlayer = fromMiniPlayer;
        ui.showForm(FORM_NAME, null);
    }

    public void onPlayPauseAction(Component c, ActionEvent event) {
        ui.player.playPause();
    }

    public void onPreviewsAction(Component c, ActionEvent event) {
        if(ui.player.getPosition() < 3000)
            ui.player.playPrevious();
        else
            ui.player.setPosition(0);
    }

    public void onNextAction(Component c, ActionEvent event) {
        ui.player.playNext();
    }

    public void updatePlayPauseButton(Container c) {
        setPlayPauseButtonState(c, ui.player.getStatus() == PlayerStatus.PLAYING || ui.player.getStatus() == PlayerStatus.LOADING);
    }

    private void setLoadingSpinnerVisibility(boolean isVisible) {
        setLoadingSpinnerVisibility(Display.getInstance().getCurrent(), isVisible);
    }

    private void setLoadingSpinnerVisibility(Container c, boolean isVisible) {
        if (c != null && ui.findSpnPlayerLoading(c) != null) {
            ui.findSpnPlayerLoading(c).setVisible(isVisible);
        }
    }

    private void setPlayPauseButtonState(boolean isPlaying) {
        setPlayPauseButtonState(Display.getInstance().getCurrent(), isPlaying);
    }

    private void setPlayPauseButtonState(Container c, final boolean isPlaying) {
        if (Display.getInstance().getCurrent() != null && SideMenuBar.isShowing()) {
            ui.sideMenu.getInstance().getParentForm().addShowListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    setPlayPauseButtonState((Form) evt.getSource(), isPlaying);
                }
            });
        } else if (c != null && ui.findBtnPlayerPlayPause(c) != null) {
            Image icon = StateMachine.getResourceFile().getImage(isPlaying ? "player_pause.png" : "player_play.png");
            Image iconPressed = StateMachine.getResourceFile().getImage(isPlaying ? "player_pause_active.png" : "player_play_active.png");

            icon.lock();
            iconPressed.lock();

            ui.findBtnPlayerPlayPause(c).setIcon(icon);
            ui.findBtnPlayerPlayPause(c).setPressedIcon(iconPressed);
        }
    }

    private void updatePlayer() {
        updatePlayer(Display.getInstance().getCurrent());
    }

    public void updatePlayer(Form c) {
        int playedTime = ui.player.getPosition();
        int totalTime = ui.player.getDuration();

        if (!isSliderDragging) {
            ui.findLblPlayerCurrentTime(c).setText(formatTime(playedTime));
        }
        ui.findLblPlayerTotalTime(c).setText(formatTime(totalTime));
        int progress = (int) ((float) playedTime / (float) totalTime * 100);

        if (!isSliderDragging) {
            ui.findSldPlayerTime(c).setProgress(progress);
        }
        if (ui.player.getStatus() == PlayerStatus.LOADING || ui.player.getStatus() == PlayerStatus.STOPPED) {
            ui.findSldPlayerTime(c).setEditable(false);
        } else {
            ui.findSldPlayerTime(c).setEditable(true);
        }

        Map media = ui.player.getCurrent();
        if (media != null) {
            ui.findLblPlayerTitleName(c).setText((String) media.get("lblTitle"));
            ui.findLblPlayerPerformerName(c).setText((String) media.get("lblSecondaryTitle"));
            ui.findLblPlayerPerformerName(c).getParent().revalidate();
        }
    }

    private void updateCover()
    {
        Form f = Display.getInstance().getCurrent();

        // Don't update UI components if UI is not visible
        if (f != null && f.getName() != null && f.getName().equals(FORM_NAME)) {
            updateCover(f);
        }
    }

    private void updateCover(final Form f)
    {
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                Image i = ui.player.getCover();

                int screenWidth = Display.getInstance().getDisplayWidth();
                int screenHeight = i.getHeight() / i.getWidth() * screenWidth;

                i = i.scaled(screenWidth, screenHeight);

                ui.findLblPLayerCover(f).setIcon(i);
            }
        });
    }

    public String formatTime(int i) {
        // Convert milli-sec to sec
        i = (i < 0 ? 0 : i) / 1000;

        String seconds = "" + (i % 60);
        String minutes = "" + i / 60;

        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }

        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }

        return minutes + ":" + seconds;
    }

    public void beforeShow(final Form f) {
        f.setTitle(ui.translate("view_title_player", "[default] Now playing"));
        
        ui.findSldPlayerTime(f).addDataChangedListener(new DataChangedListener() {
            @Override
            public void dataChanged(int type, int index) {
                // 0 is interaction by user by moving the slider backwards
                // 1 is interaction by user by either moving the slider forwards or clicking somewhere
                // 2 is interaction by "setProgress()" method
                if (type != 2) {
                    isSliderDragging = true;
                    ui.findLblPlayerCurrentTime(f).setText(formatTime((int) ((float) ui.findSldPlayerTime(f).getProgress() / 100 * (float) ui.player.getDuration())));
                }
            }
        });
        ui.findSldPlayerTime(f).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ui.player.setPosition(ui.findSldPlayerTime(f).getProgress());

                isSliderDragging = false;
            }
        });

        updateCover(f);
        
        updateRepeatButton(f);
        updateShuffleButton(f);
        updatePlayPauseButton(f);
        updatePlayer(f);
        updateSelection(f);
        setLoadingSpinnerVisibility(f, ui.player.getStatus() == PlayerStatus.LOADING);

        Image icon = StateMachine.getResourceFile().getImage("player_queue_icon.png");
        Image iconPressed = StateMachine.getResourceFile().getImage("player_queue_icon_active.png");
        icon.lock();
        iconPressed.lock();
        Command titleCmd = new Command(null, icon) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createFade(200));
                ui.queueView.show();
            }
        };
        titleCmd.setPressedIcon(iconPressed);
        titleCmd.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCmd);
        
        int screenWidth = Display.getInstance().getCurrent().getWidth();
        Image coverMask = StateMachine.getResourceFile().getImage("player_gradient_overlay.png");
        coverMask = coverMask.scaled(screenWidth, coverMask.getHeight());
        ui.findLblPLayerCoverMask(f).setIcon(coverMask);
        ui.findLblPLayerCoverMask(f).getParent().revalidate();

        if (isFromMiniPlayer) {
            f.setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 200));
        }
    }
    
    public final class EmptyImage extends EncodedImage
    {
        public EmptyImage(int width, int height) {
            super(width,height);
        }
        @Override
        protected void drawImage(Graphics g, Object nativeGraphics, int x, int y) {}
        @Override
        protected void drawImage(Graphics g, Object nativeGraphics, int x, int y, int w, int h) {}
    }

    public void onRepeatAction(Component c, ActionEvent event) {
        ui.player.setRepeat(!ui.player.getRepeat());
        updateRepeatButton();
    }

    private void updateRepeatButton() {
        updateRepeatButton(Display.getInstance().getCurrent());
    }

    private void updateRepeatButton(Form f) {
        final Button repeatBtn = ui.findBtnPlayerRepeat(f);
        if (repeatBtn == null) {
            return;
        }

        Image icon = StateMachine.getResourceFile().getImage(
                ui.player.getRepeat()
                ? "player_repeat_active.png"
                : "player_repeat.png");

        icon.lock();
        repeatBtn.setIcon(icon);

        updateSelection(f);
    }

    public void onShuffleAction(Component c, ActionEvent event) {
        Display.getInstance().callSerially(new Runnable() {
            public void run() {
                ui.player.setShuffle(!ui.player.getShuffle());
            }
        });
        
        updateShuffleButton(c.getComponentForm(), !ui.player.getShuffle());
    }
    
    private void updateShuffleButton(Form f)
    {
        updateShuffleButton(f, ui.player.getShuffle());
    }

    private void updateShuffleButton(Form f, Boolean shuffle) {
        final Button shuffleBtn = ui.findBtnPlayerShuffle(f);
        if (shuffleBtn == null) {
            return;
        }

        Image icon = StateMachine.getResourceFile().getImage(
                shuffle
                ? "player_shuffle_active.png"
                : "player_shuffle.png");

        icon.lock();
        shuffleBtn.setIcon(icon);
    }

    public void updateSelection() {
        updateSelection(Display.getInstance().getCurrent());
    }

    public void updateSelection(Form f) {
        if (f.getName() != null && f.getName().equals(FORM_NAME)) {
            ui.findBtnPlayerNext(f).setEnabled(ui.player.hasNext());
            ui.findBtnPlayerPrevious(f).setEnabled(ui.player.hasPrevious());
        }
    }

    public void onOptionAction(Component c, ActionEvent event) {
        // Get the object already here, so it doesn't switch if this song is over.
        final Map item = ui.player.getCurrent();

        ui.dialogOptions.showTrackOptions(item);
    }

    public void setFromMiniPlayer(Boolean fromMiniPlayer) {
        isFromMiniPlayer = fromMiniPlayer;
    }
    
    public void onPlaylistAction(Component c, ActionEvent event) 
    {
        c.getComponentForm().setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 200));
        ui.playlistOverviewView.show(ui.player.getCurrent());
    }
}
