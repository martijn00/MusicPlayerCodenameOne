/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.ConnectionRequest;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import java.util.ArrayList;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.FileManager;
import com.codenameone.music.MediaHelper;
import com.codenameone.music.Playlist;
import com.codenameone.music.PlaylistFactory;
import com.codenameone.music.PlaylistHelper;
import com.codenameone.music.api.IStringResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class PlaylistView extends ScrollableView {
    protected Integer playlistId;
    protected FormRunnable onReady;
    public static final String FORM_NAME = "ScreenPlaylist";

    private Command titleCommand;
    
    public PlaylistView(StateMachine ui)
    {
        this.ui = ui;
    }

    @Override
    List getList(Form f) {
        return ui.findCtlPlaylistMediaItems(f);
    }

    public Form show(int playlistId)
    {
        this.playlistId = playlistId;
        return ui.showForm(FORM_NAME, null);
    }

    public Form show(int playlistId, FormRunnable runnable)
    {
        this.playlistId = playlistId;
        this.onReady = runnable;
        return ui.showForm(FORM_NAME, null);
    }

    public Integer getPlaylistId()
    {
        return playlistId;
    }

    public void beforeShow(final Form f)
    {
        ui.hideComponent(ui.findCtnTopbarEditing(f));
        ui.hideComponent(ui.findCtnOfflineAvailable(f));
        ui.hideComponent(ui.findCtnPlaylistProgress(f));
        ui.findCtnPlaylistProgress(f).setHeight(StateMachine.getPixelFromMM(1,false));
        ui.findCtnPlaylistItems(f).addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));
        
        ui.findTxtEditing(f).setDoneListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onEditPlaylistTitleConfirm(f);
            }
        });

        // If the onReady-handler is set, link the form to it.
        if (onReady != null) {
            onReady.setForm(f);
        }

        if(playlistId == 0)
        {
            Playlist pl = new Playlist();
            for (Map<String, Object> playlist : PlaylistFactory.getInstance().getOfflineTracks()) {
                pl.addItem(playlist);
            }
            updateView(f, pl);

            ui.findCtlPlaylistMediaItems(f).addPullToRefresh(new Runnable() {
                public void run() {
                    Playlist pl = new Playlist();
                    java.util.List<Map> newPl = PlaylistFactory.getInstance().getOfflineTracks(true);
                    for (Map<String, Object> playlist : newPl) {
                        pl.addItem(playlist);
                    }
                    updateView(f, pl);
                    
                    f.setTitle(ui.translate("playlist_all_downloaded", "[default] All downloaded items"));
                    ui.hideComponent(ui.findCtnPlaylistProgress(f));
                    ui.hideComponent(ui.findCtnTopbarEditing(f));
                    ui.hideComponent(ui.findCtnOfflineAvailable(f));
                }
            });
            
            f.setTitle(ui.translate("playlist_all_downloaded", "[default] All downloaded items"));
            ui.hideComponent(ui.findCtnPlaylistProgress(f));
            ui.hideComponent(ui.findCtnTopbarEditing(f));
            ui.hideComponent(ui.findCtnOfflineAvailable(f));

            beforeShowAfterModel(f);
        }
        else
        {
            ConnectionRequest request = PlaylistFactory.getInstance().getPlaylist(playlistId, new PlaylistFactory.IPlaylistResponseHandler() {
                @Override
                public void onSuccess(final Playlist playlist, Map<String, String> headers) {
                    updateView(f, playlist);

                    // Show edit-playlist-title only after playlist has been loaded.
                    if (Api.getInstance().isOnline()) {
                        addPlaylistEdit(f);
                    }

                    // Playlist is offline available, isn't downloading and the response is uncached
                    if (playlist.getOfflineAvailable() && !PlaylistHelper.isPlaylistDownloading(playlist) && headers.size() > 0) {
                        boolean hasOnlineTracks = false;
                        for (int i = 0; i < playlist.getSize(); i++) {
                            if (MediaHelper.getState(playlist.getItemAt(i)) == MediaHelper.NOT_DOWNLOADED) {
                                hasOnlineTracks = true;
                                break;
                            }
                        }

                        if (hasOnlineTracks) {
                            DialogConfirm.show(StateMachine.getInstance().translate("dialog_playlist_resume_downloading", "[default]Do you want to continue downloading the playlist?"), new Command(null) {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    PlaylistHelper.downloadPlaylist(playlist, ui.findCtnPlaylistProgress(f));
                                }
                            }, new Command(null) {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                }
                            });
                        }
                    }

                    beforeShowAfterModel(f);
                }

                @Override
                public void onFailure(int code, String message, Map<String, String> headers) {
                }
            });
            ui.registerRequest(f, request);
        }
    }
    
    private void addPlaylistEdit(final Form f)
    {
        //TODO: Enabled this again when Shai has fixed deleting titlecommands
        //if(titleCommand != null)
        //    f.removeCommand(titleCommand);
        
        ui.removeTitleCommand(f);
        
        // Add command to change playlist-name
        Image icon = StateMachine.getResourceFile().getImage("playlist_edit_static.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("playlist_edit_active.png");
        iconPressed.lock();
        titleCommand = new Command(null, icon){
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                addPlaylistCancel(f);
                
                onEditPlaylistTitle(f);
            }
        };
        titleCommand.setPressedIcon(iconPressed);
        titleCommand.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCommand);
    }
    
    private void addPlaylistCancel(final Form f)
    {
        //f.removeCommand(titleCommand);
        ui.removeTitleCommand(f);
        
        
        // Add command to change playlist-name
        Image icon = StateMachine.getResourceFile().getImage("playlist_cancel.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("playlist_cancel.png");
        iconPressed.lock();
        titleCommand = new Command(null, icon){
            @Override
            public void actionPerformed(ActionEvent evt) {
                addPlaylistEdit(f);
                hideTopbarEditing(f);
            }
        };
        titleCommand.setPressedIcon(iconPressed);
        titleCommand.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCommand);
    }
    
    public void updateView(final Form f, Playlist playlist)
    {
        // Prepare the playlist for the viewmodel
        ListModelHelper.preparePlaylistForListing(playlist);

        // Update the viewmodel
        ui.findCtlPlaylistMediaItems(f).setModel(playlist);

        f.setTitle(playlist.getName());
        ui.hideComponent(ui.findCtnPlaylistProgress(f));

        if (ui.findCtlPlaylistMediaItems(f).getModel().getSize() > 0) {
            if (Api.getInstance().canDownload()) {
                // Show the download-available container incl. checkbox
                ui.findChkPlaylistIsOfflineAvailable(f).setSelected(playlist.getOfflineAvailable());
                ui.resetComponentHeight(ui.findCtnOfflineAvailable(f));

                // Hide the download-status if it isn't running
                if (PlaylistHelper.isPlaylistDownloading(playlist)) {
                    ui.resetComponentHeight(ui.findCtnPlaylistProgress(f));
                    ui.findCtnPlaylistProgress(f).setHeight(StateMachine.getPixelFromMM(1,false));
                    ui.findSldPlaylistDownloadingProgress(f).setProgress(
                            PlaylistHelper.getPlaylistDownloadingProgress(playlist)
                    );
                }
                
                // Show is loading is in progress or already done
                ui.findSldPlaylistDownloadingProgress(f).setMaxValue(playlist.getSize()*100);
            }
            else
            {
                ui.hideComponent(ui.findCtnTopbarEditing(f));
            }
            
            BorderLayout bl = (BorderLayout)ui.findCtnPlaylistItems(f).getLayout();
            if(bl.getNorth() != null)
            {
                ui.findCtnPlaylistItems(f).removeComponent(bl.getNorth());
                ui.findCtnPlaylistItems(f).repaint();
            }
        } 
        else 
        {
            // If the locally saved version of the playlist contained items, but the remote (loaded later) does not
            ui.hideComponent(ui.findCtnTopbarEditing(f));
            ui.hideComponent(ui.findCtnOfflineAvailable(f));
            ui.findCtnPlaylistItems(f).addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewEmptyPlaylist"));
        }

        if (onReady != null) {
            Display.getInstance().callSerially(onReady);
            onReady = null;
        }
    }

    public void onPlaylistAction(Component c, ActionEvent event)
    {
        event.consume();
        final List list = (List) event.getSource();
        final Playlist playlist = (Playlist)list.getModel();
        final Map item = (Map)list.getSelectedItem();

        Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
        if (clickedButton != null) {
            // Occurs when touching a button

            if (clickedButton.getName().equals("btnMediaActionFixed")) {
                ArrayList<Command> commands = new ArrayList<Command>();

                if (Api.getInstance().isOnline()) {
                    Image icon = StateMachine.getResourceFile().getImage("popup_trash_icon.png");
                    icon.lock();
                    commands.add(new Command(ui.translate("command_remove_item_from_playlist", "[default]Remove item"), icon) {

                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            DialogConfirm.show(ui.translate("confirmation_remove_item_from_playlist", "[default]Do you really want to delete this item from the playlist?"), new Command(null) {

                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    playlist.removeItem(list.getSelectedIndex());
                                    PlaylistFactory.getInstance().savePlaylist(playlist, null);
                                }
                            }, null);
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
                    
                    ui.dialogOptions.show(commands);
                }
                else
                {
                    ToastView.show(ui.translate("toast_offline_unavailable", "[default] Unavailable in offline mode"), c.getComponentForm());
                }
                return;
            }
        }

        if (Api.getInstance().isOnline() || MediaHelper.getState(playlist.getItemAt(list.getSelectedIndex())) == MediaHelper.DOWNLOADED) {
            ui.player.setQueueAndPlay(playlist);
        }
        else
        {
            ToastView.show(ui.translate("toast_offline_unavailable", "[default] Unavailable in offline mode"), c.getComponentForm());
        }
    }

    public void onIsOfflineAvailableAction(final Component c, final ActionEvent event) {
        final Playlist playlist = (Playlist)ui.findCtlPlaylistMediaItems(c.getComponentForm()).getModel();

        if (((CheckBox)c).isSelected()) {
            PlaylistFactory.getInstance().setPlaylistOfflineAvailable(playlist.getId(), true);

            if (FileManager.getInstance().isReady())
            {
                PlaylistHelper.downloadPlaylist(playlist, ui.findCtnPlaylistProgress(c.getComponentForm()));
            }
            else
                DialogConfirm.show(ui.translate("dialog_filesystem_not_found_message", "[default]You may have removed the storage the files for this app were saved on. Do you want to find a new one?"), new Command(null) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        FileManager.getInstance().resetRoot();

                        onIsOfflineAvailableAction(c, event);
                    }
                }, new Command(null) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ((CheckBox)c).setSelected(false);

                        Container ctn = ui.findCtnPlaylistProgress(c.getComponentForm());
                        if (ctn != null) {
                            ui.hideComponent(ctn);
                        }
                    }
                });
        } else {
            if (PlaylistHelper.isPlaylistDownloading(playlist)) {
                DialogConfirm.show(ui.translate("dialog_playlist_abort_downloading", "[default] Are you sure you want to abort downloading this playlist?"),new Command(null){
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        // Check if the playlist is still loading. It could have finished while the dialog was opened.
                        if (PlaylistHelper.isPlaylistDownloading(playlist)) {
                            PlaylistHelper.killPlaylistDownloadingProgress(playlist);
                        }
                        deletingDownloadedFiles(playlist, c);
                    }
                },new Command(null) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ((CheckBox)c).setSelected(true);
                        updateView(c.getComponentForm(), playlist);
                    }
                });
            } else {
                deletingDownloadedFiles(playlist, c);
            }
        }
    }

    private void deletingDownloadedFiles(final Playlist playlist, final Component c)
    {
        DialogConfirm.show(ui.translate("dialog_playlist_delete_offline", "[default] Are you sure you want to delete all offline files for this playlist?"),new Command(null){
            @Override
            public void actionPerformed(ActionEvent evt) {
                PlaylistFactory.getInstance().setPlaylistOfflineAvailable(playlist.getId(), false);
                updateView(c.getComponentForm(), playlist);
            }
        },new Command(null) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ((CheckBox)c).setSelected(true);
                updateView(c.getComponentForm(), playlist);
            }
        });
    }

    public void onEditPlaylistTitle(Form f) {
        Playlist playlist = (Playlist)ui.findCtlPlaylistMediaItems(f).getModel();

        ui.resetComponentHeight(ui.findCtnTopbarEditing(f));
        ui.findTxtEditing(f).requestFocus();
        Display.getInstance().editString(ui.findTxtEditing(f), 9999, TextField.ANY, playlist.getName());
    }

    public void onEditPlaylistTitleConfirm(Form f) {
        Playlist playlist = (Playlist)ui.findCtlPlaylistMediaItems(f).getModel();

        String text = ui.findTxtEditing(f).getText();
        if(!playlist.getName().equals(text))
        {
            if (text.length() > 0) {
                playlist.setName(text);
                PlaylistFactory.getInstance().savePlaylist(playlist, new IStringResponseHandler() {

                    public void onSuccess(Map<String, String> headers) {
                        // Update local list
                        PlaylistFactory.getInstance().getPlaylists(null);
                    }

                    public void onFailure(int code, String message, Map<String, String> headers) {
                    }
                });
            }

            f.setTitle(playlist.getName());
        }
        hideTopbarEditing(f);
        addPlaylistEdit(f);
    }
    
    private void hideTopbarEditing(Form f)
    {
        ui.hideComponent(ui.findCtnTopbarEditing(f));
    }
}
