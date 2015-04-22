/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.ConnectionRequest;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.PlaylistFactory;
import com.codenameone.music.api.IListResponseHandler;
import com.codenameone.music.api.IStringResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class PlaylistOverviewView extends ScrollableView {
    public final static String FORM_NAME = "ScreenPlaylistOverview";

    private DefaultListModel<Map> tracklist;
    private Command titleCommand;

    public PlaylistOverviewView(StateMachine ui) {
        this.ui = ui;
    }

    @Override
    List getList(Form f) {
        return ui.findCtlOverviewPlaylistList(f);
    }

    public void show()
    {
        tracklist = null;

        ui.showForm(FORM_NAME, null);
    }

    public void show(Map item)
    {
        ArrayList<Map> tracks = new ArrayList<Map>();
        tracks.add(item);

        show(new DefaultListModel(tracks));
    }

    public void show(DefaultListModel<Map> list)
    {
        tracklist = list;

        ui.showForm(FORM_NAME, null);
    }

    public boolean hasTracklist()
    {
        return tracklist != null;
    }

    public void initPlaylistsModel(final List cmp) {
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                cmp.setModel(new DefaultListModel(parsePlaylists(PlaylistFactory.getInstance().getLocalPlaylists())));
            }
        });
    }

    public void beforeShow(final Form f)
    {
        if (tracklist != null) {
            f.setTitle(ui.translate("addtoplaylist_title", "[default]Add to playlist"));
        } else {
            f.setTitle(ui.translate("view_title_playlists", "[default]My Playlists"));
        }

        f.getTitleComponent().setEndsWith3Points(true);
        f.getContentPane().addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));
        
        ui.hideComponent(ui.findCtnAddPlaylistForm(f));

        if (Api.getInstance().isOnline()) {
            addPlaylistAdd(f);
        }
        
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            public void run() {
                ConnectionRequest request = PlaylistFactory.getInstance().getPlaylists(new IListResponseHandler() {
                    @Override
                    public void onSuccess(java.util.List playlists, Map<String, String> headers) {
                        updateModel(ui.findCtlOverviewPlaylistList(f), playlists);

                        beforeShowAfterModel(f.getComponentForm());
                    }

                    @Override
                    public void onFailure(int code, String message, Map<String, String> headers) {
                    }
                });
                ui.registerRequest(f, request);
            }
        });
        
        ui.findTfdAddPlaylistFormTitle(f).setDoneListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onAddPlaylistSaveAction(f, evt);
            }
        });
    }
    
    private void addPlaylistAdd(final Form f)
    {
        //TODO: Enabled this again when Shai has fixed deleting titlecommands
        //if(titleCommand != null)
        //   f.removeCommand(titleCommand);
        
        ui.removeTitleCommand(f);
        
        Image icon = StateMachine.getResourceFile().getImage("playlist_plus_static.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("playlist_plus_active.png");
        iconPressed.lock();
        titleCommand = new Command(null, icon){
            @Override
            public void actionPerformed(ActionEvent evt) {
                addPlaylistCancel(f);
                ui.resetComponentHeight(ui.findCtnAddPlaylistForm(f));
                ui.findTfdAddPlaylistFormTitle(f).requestFocus();
                Display.getInstance().editString(ui.findTfdAddPlaylistFormTitle(f), 200, TextField.ANY, "");
            }
        };
        titleCommand.setPressedIcon(iconPressed);
        titleCommand.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCommand);
    }
    
    private void addPlaylistCancel(final Form f)
    {
        ui.removeTitleCommand(f);
        
        Image icon = StateMachine.getResourceFile().getImage("playlist_cancel.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("playlist_cancel.png");
        iconPressed.lock();
        titleCommand = new Command(null, icon){
            @Override
            public void actionPerformed(ActionEvent evt) {
                addPlaylistAdd(f);
                ui.hideComponent(ui.findCtnAddPlaylistForm(f));
            }
        };
        titleCommand.setPressedIcon(iconPressed);
        titleCommand.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCommand);
    }

    public void updateModel(final List cmp, java.util.List playlists)
    {
        cmp.setModel(new DefaultListModel(parsePlaylists(playlists)));

        Display.getInstance().callSerially(new Runnable() {
            @Override
            public void run() {
                if (cmp.getComponentForm() != null && cmp.getComponentForm().getContentPane() != null) {
                    BorderLayout bl = (BorderLayout) cmp.getComponentForm().getContentPane().getLayout();
                    if (bl.getNorth() != null) {
                        cmp.getComponentForm().getContentPane().removeComponent(bl.getNorth());
                        cmp.getComponentForm().repaint();
                    }
                }
            }
        });
    }

    private java.util.List parsePlaylists(java.util.List playlists)
    {
        if(playlists.size() > 0 && (Integer)((Map<String, Object>)playlists.get(0)).get("id") == 0)
        {
            playlists.remove(0);
        }

        Map<String, Object> offlinePL = new HashMap<String, Object>();
        offlinePL.put("name", ui.translate("playlist_all_downloaded", "[default] All downloaded items"));
        offlinePL.put("btnMediaIcon", StateMachine.getResourceFile().getImage("playlist_downloaded_items.png"));
        offlinePL.put("id", 0);
        offlinePL.put("track_count", PlaylistFactory.getInstance().getOfflineTracks().size());
        offlinePL.put("offline_available", true);

        playlists.add(0, offlinePL);

        for (Object playlist : playlists) {
            updateListModelItem((Map<String, Object>) playlist);
        }

        return playlists;
    }

    private void updateListModelItem(Map<String, Object> playlist)
    {
        playlist.put("lblPlaylistRowTitle", playlist.get("name"));
        playlist.put("lblPlaylistRowItems", playlist.get("track_count") + ui.translate("playlist_row_items", "[default] items"));

        if ((Boolean)playlist.get("offline_available") && (Integer)playlist.get("id") != 0)
            playlist.put("btnMediaIcon", StateMachine.getResourceFile().getImage("playlist_icon_downloaded.png"));
        else if((Integer)playlist.get("id") != 0)
            playlist.put("btnMediaIcon", StateMachine.getResourceFile().getImage("playlist_icon.png"));
    }

    public void onPlaylistAction(final Component c, ActionEvent event) {
        final List list = (List) event.getSource();
        final Integer playlistId = (Integer)((Map) list.getSelectedItem()).get("id");

        Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
        if (clickedButton != null) {
            // Occurs when touching a button
            if (clickedButton.getName().equals("btnPlaylistActionFixed")) {
                if(playlistId == 0)
                {
                    ArrayList<Command> commands = new ArrayList<Command>();
                    Image icon = StateMachine.getResourceFile().getImage("popup_trash_icon.png");
                    icon.lock();
                    commands.add(new Command(ui.translate("command_delete_all_downloads", "[default]Delete all downloads"), icon) {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            DialogConfirm.show(ui.translate("confirmation_delete_all_downloads", "[default]Do you really want to delete all downloaded items?"), new Command(null) {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    
                                }
                            }, null);
                        }
                    });
                    ui.dialogOptions.show(commands);
                }
                else
                {
                    if (Api.getInstance().isOnline())
                    {
                        ArrayList<Command> commands = new ArrayList<Command>();

                        Image icon = StateMachine.getResourceFile().getImage("popup_edit_icon.png");
                        icon.lock();
                        commands.add(new Command(ui.translate("command_edit_playlist_title", "[default]Edit playlist"), icon) {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                ui.playlistView.show(playlistId, new FormRunnable() {
                                    public void run() {
                                        ui.playlistView.onEditPlaylistTitle(f);
                                    }
                                });
                            }
                        });

                        icon = StateMachine.getResourceFile().getImage("popup_trash_icon.png");
                        icon.lock();
                        commands.add(new Command(ui.translate("command_delete_playlist", "[default]Delete playlist"), icon) {

                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                DialogConfirm.show(ui.translate("confirmation_delete_playlist", "[default]Do you really want to drop the playlist?"), new Command(null) {

                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        c.getComponentForm().getContentPane().addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));

                                        PlaylistFactory.getInstance().deletePlaylist(playlistId, new IStringResponseHandler() {

                                            @Override
                                            public void onSuccess(Map<String, String> headers) {
                                                ConnectionRequest request = PlaylistFactory.getInstance().getPlaylists(new IListResponseHandler() {

                                                    public void onSuccess(java.util.List list, Map<String, String> headers) {
                                                        updateModel(ui.findCtlOverviewPlaylistList(c.getComponentForm()), list);
                                                    }

                                                    public void onFailure(int code, String message, Map<String, String> headers) {
                                                    }
                                                });
                                                ui.registerRequest(c.getComponentForm(), request);
                                            }

                                            @Override
                                            public void onFailure(int code, String message, Map<String, String> headers) {
                                            }
                                        });
                                    }
                                }, null);
                            }
                        });
                        ui.dialogOptions.show(commands);
                    }
                    else
                    {
                        ToastView.show(ui.translate("toast_offline_unavailable", "[default] Unavailable in offline mode"), c.getComponentForm());
                    }
                }
                return;
            }
        }

        onPlaylistAction(playlistId);
    }

    protected void onPlaylistAction(int playlistId) {
        if (tracklist != null) {
            addTracksToPlaylist(playlistId);
        } else {
            ui.playlistView.show(playlistId);
        }
    }

    private void addTracksToPlaylist(final int playlistId)
    {
        ArrayList<Map> list = new ArrayList<Map>();
        for (int i = 0; tracklist.getSize() > i; i++) {
            list.add(tracklist.getItemAt(i));
        }

        Api.getInstance().linkTracksToPlaylist(new IStringResponseHandler() {

            public void onSuccess(Map<String, String> headers) {
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, playlistId, list);

        ui.back(Display.getInstance().getCurrent());
    }

    public void onAddPlaylistSaveAction(final Component c, ActionEvent event) {
        ui.hideComponent(ui.findCtnAddPlaylistForm());
        addPlaylistAdd(c.getComponentForm());

        String text = ui.findTfdAddPlaylistFormTitle().getText();
        if (text.length() > 0) {
            c.getComponentForm().getContentPane().addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));
            
            PlaylistFactory.getInstance().createPlaylist(text, new IStringResponseHandler() {
                @Override
                public void onSuccess(Map<String, String> headers) {
                    ConnectionRequest request = PlaylistFactory.getInstance().getPlaylists(new IListResponseHandler() {

                        public void onSuccess(java.util.List playlists, Map<String, String> headers) {
                            updateModel(ui.findCtlOverviewPlaylistList(c.getComponentForm()), playlists);
                        }

                        public void onFailure(int code, String message, Map<String, String> headers) {
                        }
                    });
                    ui.registerRequest(c.getComponentForm(), request);
                }

                @Override
                public void onFailure(int code, String message, Map<String, String> headers) {
                }
            });
        }
    }
}
