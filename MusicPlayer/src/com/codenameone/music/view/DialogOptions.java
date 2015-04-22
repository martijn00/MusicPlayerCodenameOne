/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.NetworkEvent;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import java.util.ArrayList;
import java.util.Map;
import com.codename1.util.StringUtil;
import com.codenameone.music.FileManager;
import com.codenameone.music.MediaHelper;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class DialogOptions {
    protected StateMachine ui;
    public final static String FORM_NAME = "DialogOptions";

    public DialogOptions(StateMachine ui)
    {
        this.ui = ui;
    }

    public void show(final ArrayList<Command> commands)
    {
        int popupWidth = Display.getInstance().getCurrent().getWidth() - StateMachine.getPixelFromMM(10, true);
        if(popupWidth > StateMachine.getPixelFromMM(40,true))
            popupWidth = StateMachine.getPixelFromMM(40,true);
        
        final Dialog dia = new Dialog();
        dia.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        dia.getDialogStyle().setBgTransparency(0);
        
        Command backCommand = new Command(null){
            @Override
            public void actionPerformed(ActionEvent ev) {
                dia.dispose();
            }
        };
        dia.setBackCommand(backCommand);

        for (int i = 0; i < commands.size(); i++) {
            Button btn = new Button(commands.get(i).getCommandName(), commands.get(i).getIcon());
            if(i == 0)
                btn.setUIID("ViewOptionsRowFirst");
            else
                btn.setUIID("ViewOptionsRow");
            btn.setGap(20);
            btn.setPreferredW(popupWidth);
            btn.setEndsWith3Points(true);
            final int cmdint = i;
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    dia.dispose();
                    commands.get(cmdint).actionPerformed(evt);
                }
            });
            dia.addComponent(btn);
        }
        
        Container ctn = new Container(new LayeredLayout());

        Button closebg = new Button();
        closebg.setUIID("ViewOptionsRowLast");
        closebg.getStyle().setBgTransparency(0);
        closebg.setPreferredW(popupWidth);
        ctn.addComponent(closebg);

        Container ctnButtons = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ctnButtons.getStyle().setMargin(StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, true), StateMachine.getPixelFromMM(2, true));
        ctn.addComponent(ctnButtons);
        
        Button close = new Button(ui.translate("button_cancel", "[default] Cancel"));
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dia.dispose();
            }
        });
        close.setUIID("ViewOptionsRowClose");
        close.setVerticalAlignment(Label.CENTER);
        
        ctnButtons.addComponent(close);
        dia.addComponent(ctn);

        dia.showPacked(BorderLayout.CENTER, true);
    }

    public void showTrackOptions(final Map item)
    {
        ArrayList<Command> commands = new ArrayList<Command>();

        Image iconPlaylist = StateMachine.getResourceFile().getImage("popup_playlist_icon.png");
        iconPlaylist.lock();
        commands.add(new Command(ui.translate("command_add_item_to_playlist", "[default]Add to playlist"), iconPlaylist) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ui.playlistOverviewView.show(item);
            }
        });
        
        Image iconQueue = StateMachine.getResourceFile().getImage("popup_playqueue_icon.png");
        iconQueue.lock();
        commands.add(new Command(ui.translate("command_add_item_to_queue", "[default]Add to queue"), iconQueue) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ui.player.addToQueue(item);
            }
        });
        
        if (FileManager.getInstance().isReady() && MediaHelper.getState(item) == MediaHelper.NOT_DOWNLOADED) {
            Image iconDownload = StateMachine.getResourceFile().getImage("popup_download_icon.png");
            iconDownload.lock();
            commands.add(new Command(ui.translate("command_download_item", "[default]Download this item"), iconDownload) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    downloadSingleItem(item);
                }
            });
        }

        /*
        if(Display.getInstance().isNativeShareSupported())
        {
            getShareCommand(item);
        }*/

        Image iconAlbum = StateMachine.getResourceFile().getImage("popup_album_icon.png");
        iconAlbum.lock();
        commands.add(new Command(ui.translate("command_go_to_album", "[default]Go to album"), iconAlbum) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ui.browseView.show(MediaHelper.getParentId(item));
            }
        });
        
        /*Image iconPerformer = StateMachine.getResourceFile().getImage("popup_album_icon.png");
        iconPerformer.lock();
        commands.add(new Command(ui.translate("command_go_to_performer", "[default]Go to performer"), iconPerformer) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ui.performerView.show();
            }
        });
                */
        show(commands);
    }
    
    private void downloadSingleItem(final Map item) {
        if (FileManager.getInstance().isReady()) {
            ToastView.show(ui.translate("toast_item_downloading", "[default] Item is downloading"), Display.getInstance().getCurrent());
            Display.getInstance().callSerially(new Runnable() {
                public void run() {
                    MediaHelper.download(item, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (((NetworkEvent)evt).getProgressPercentage() == 100) {
                                ToastView.show(StringUtil.replaceAll(ui.translate("toast_item_downloaded", "[default] Item is downloaded: '{track.title}'"), "{track.title}", (String)item.get("lblTitle")), Display.getInstance().getCurrent());
                            }
                        }
                    });
                }
            });
        } else
            DialogConfirm.show(ui.translate("dialog_filesystem_not_found_message", "[default]You may have removed the storage the files for this app were saved on. Do you want to find a new one?"), new Command(null) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    FileManager.getInstance().resetRoot();

                    downloadSingleItem(item);
                }
            }, new Command(null) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                }
            });
    }

    public static Command getShareCommand(final Map item)
    {
        Image iconShare = StateMachine.getResourceFile().getImage("popup_facebook_icon.png");
        return new Command(StateMachine._translate("command_share_item", "[default]Share"), iconShare) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String shareText;
                //When item is a performer
                if(item.get("name") != null)
                    shareText = "I listened to " + item.get("name") + " on Music Player";
                //When item is a category
                else if(item.get("categories") != null)
                    shareText = "I visited the category " + item.get("title") + " on Music Player";
                //When item is an album
                else if(item.get("tracks") != null)
                    shareText = "I listened to the album " + item.get("title") + " on Music Player";
                //When item is a track
                else
                    shareText = "I listened to " + item.get("lblTitle") + " - " + item.get("lblSecondaryTitle") + " on Music Player";

                Display.getInstance().share(shareText, null, "text/plain");
            }
        };
    }
}
