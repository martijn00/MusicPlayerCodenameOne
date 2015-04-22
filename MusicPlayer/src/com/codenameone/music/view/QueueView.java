/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.GenericListCellRenderer;
import java.util.ArrayList;
import java.util.Map;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class QueueView {
    protected StateMachine ui;
    public static final String FORM_NAME = "ScreenQueue";
    
    public QueueView(StateMachine ui)
    {
        this.ui = ui;
    }

    public Form show()
    {
        return ui.showForm(FORM_NAME, null);
    }
    
    public void beforeShow(Form f)
    {
        f.setTitle(ui.translate("view_title_queue", "[default] Queue"));
        
        if(Display.getInstance().getCurrent().getName().equals("ScreenPlayer"))
        {
            f.setTransitionInAnimator(CommonTransitions.createEmpty());
            f.setTransitionOutAnimator(CommonTransitions.createFade(200));
        }

        addFormCommand(f);
    }

    private void addFormCommand(Form f)
    {
        Image icon = StateMachine.getResourceFile().getImage("player_player_icon.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("player_player_icon_active.png");
        iconPressed.lock();
        Command titleCmd = new Command(null, icon) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createFade(200));
                ui.back();
            }
        };

        titleCmd.setPressedIcon(iconPressed);
        titleCmd.putClientProperty("TitleCommand", Boolean.TRUE);

        f.addCommand(titleCmd);
    }

    public boolean initQueueModel(List cmp)
    {
        ArrayList<Map> list = ui.player.getQueue();
        ListModelHelper.prepareTracksForListing(list);

        cmp.setModel(new TrackListModel(list));
        return true;
    }

    public void onQueueAction(Component c, ActionEvent event) 
    {
        event.consume();
        final List list = (List) event.getSource();
        final Map item = (Map)list.getSelectedItem();

        Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
        if (clickedButton != null) {
            // Occurs when touching a button

            if (clickedButton.getName().equals("btnMediaActionFixed")) {
                ArrayList<Command> commands = new ArrayList<Command>();

                Image icon = StateMachine.getResourceFile().getImage("popup_trash_icon.png");
                icon.lock();
                commands.add(new Command(ui.translate("command_remove_item_from_playlist", "[default]Remove item"), icon) {

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ui.player.removeFromQueue((Map) list.getSelectedItem());
                        if(ui.player.isQueueEmpty())
                        {
                            Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createEmpty());
                            ui.playerView.setFromMiniPlayer(false);
                            ui.back();
                            ui.back();
                        }
                        else
                            initQueueModel(list);
                    }
                });
                ui.dialogOptions.show(commands);
                return;
            }
        }
        
        ui.player.setQueueAndPlay((TrackListModel) list.getModel());
        Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createFade(200));
        ui.back();
    }
}
