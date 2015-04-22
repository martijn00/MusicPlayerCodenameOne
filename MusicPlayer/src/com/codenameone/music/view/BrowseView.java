/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.ConnectionRequest;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.list.ListCellRenderer;
import java.util.ArrayList;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.MediaHelper;
import com.codenameone.music.api.IJsonResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class BrowseView extends ScrollableView {
    private int id;
    private String view;
    private String title;
    private java.util.List<String> key;

    public static String FORM_NAME = "ScreenBrowse";

    public BrowseView(StateMachine ui) {
        this.ui = ui;
    }

    @Override
    List getList(Form f) {
        return ui.findCtlBrowseItems(f);
    }

    public void show(Integer id)
    {
        // Put this into the form as soon as possible, so we have just one storage for that we rely on.
        this.id = id;
        this.view = "album";
        this.key = new ArrayList<String>();

        ui.showForm(FORM_NAME, null);
    }

    public void show(String view, java.util.List<String> key, String title)
    {
        // Put this into the form as soon as possible, so we have just one storage for that we rely on.
        this.id = 0;
        this.view = view;
        this.key = key;
        this.title = title;

        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(final Form f) {
        // If the category-id isn't set (f.e. if you're creating a new form - called it by showForm()), set it.
        if (f.getClientProperty("album-id") == null)
            f.putClientProperty("album-id", id);

        if (f.getClientProperty("view") == null)
            f.putClientProperty("view", view);

        if (f.getClientProperty("key") == null)
            f.putClientProperty("key", key);

        if (f.getClientProperty("title") == null)
            f.putClientProperty("title", title);
        
        if (title != null)
            f.setTitle(title);

        f.getContentPane().addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));

        // Initial loading of news and reloading every time you refresh (by pulling down the list)
        ui.findCtlBrowseItems(f).addPullToRefresh(new Runnable() {
            public void run() {
                reloadModel(f, false);
            }
        });
        reloadModel(f, true);
    }

    protected void beforeShowAfterModel(Form f) {
        f.getTitleComponent().setEndsWith3Points(true);

        BorderLayout bl = (BorderLayout)f.getContentPane().getLayout();
        if(bl.getNorth() != null)
        {
            f.getContentPane().removeComponent(bl.getNorth());
            f.repaint();
        }

        super.beforeShowAfterModel(f);
    }

    protected void reloadModel(final Form f, boolean allowCache)
    {
        if (ui.findCtlBrowseItems(f).getModel().getSize() > 0 && allowCache) {
            beforeShowAfterModel(f);

            return;
        }

        IJsonResponseHandler responseHandler = new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                setDataToModel(f, data);
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        };


        if ("album".equals(f.getClientProperty("view"))) {
            ConnectionRequest request = Api.getInstance().getCategory(responseHandler, (Integer)f.getClientProperty("album-id"), allowCache);
            ui.registerRequest(f, request);
        } else {
            java.util.List<String> keys = (java.util.List<String>)f.getClientProperty("key");

            if ("tag".equals(f.getClientProperty("view"))) {
                ConnectionRequest request = Api.getInstance().getTracksByTags(responseHandler, keys, allowCache);
                ui.registerRequest(f, request);
            } else if ("content-type".equals(f.getClientProperty("view"))) {
                ConnectionRequest request = Api.getInstance().getCategoriesByContentTypes(responseHandler, keys, allowCache);
                ui.registerRequest(f, request);
            }
        }
    }

    private void setDataToModel(Form f, Map data)
    {
        if (ui.findCtlBrowseItems(f).getModel().getSize() == 0) {
            beforeShowAfterModel(f);
        }

        ArrayList<Map> children;
        if (data.get("root") != null) {
            children = (ArrayList<Map>)data.get("root");
        } else {
            f.setTitle((String) data.get("title"));
            children = (ArrayList<Map>)data.get("children");
        }

        ListCellRenderer lcr;

        if(children != null && !children.isEmpty())
        {
            if ("album".equals(children.get(0).get("type"))) {
                ListModelHelper.prepareAlbumsForListing(children);
                ui.findCtlBrowseItems(f).setModel(new DefaultListModel<Map>(children));
                lcr = new GenericListCellRenderer(StateMachine.createContainer("ViewCategoryRow"), StateMachine.createContainer("ViewCategoryRow"));
            } else {
                ListModelHelper.prepareTracksForListing(children);
                ui.findCtlBrowseItems(f).setModel(new TrackListModel(children));
                lcr = new GenericListCellRenderer(StateMachine.createContainer("ViewTrackRow"), StateMachine.createContainer("ViewTrackRow"));
                addAddToPlaylistCommand(f);
            }

            ui.findCtlBrowseItems(f).setRenderer(lcr);
        } else {
            // What to do if it's empty? Should never happen - but may it does ;)
        }
    }

    private void addAddToPlaylistCommand(final Form f)
    {
        ui.removeTitleCommand(f);
        
        Image icon = StateMachine.getResourceFile().getImage("topbar_options_static.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("topbar_options_active.png");
        iconPressed.lock();
        Command titleCmd = new Command(null, icon) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ArrayList<Command> commands = new ArrayList<Command>();

                Image icon = StateMachine.getResourceFile().getImage("popup_playlist_icon.png");
                icon.lock();
                commands.add(new Command(ui.translate("command_add_all_playlist_title", "[default]Add all to playlist"), icon) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ui.playlistOverviewView.show((TrackListModel)ui.findCtlBrowseItems(f).getModel());
                    }
                });
/*
                if(Display.getInstance().isNativeShareSupported())
                {
                    commands.add(DialogOptions.getShareCommand(item));
                }*/
                ui.dialogOptions.show(commands);
            }
        };
        titleCmd.setPressedIcon(iconPressed);
        titleCmd.putClientProperty("TitleCommand", Boolean.TRUE);
        f.addCommand(titleCmd);
    }

    /**
     * Called when the form is created to set the current state.
     */
    public void setState(Form f, Map state) {
        f.putClientProperty("album-id", state.get("album-id"));
        f.putClientProperty("view", state.get("view"));
        f.putClientProperty("key", state.get("key"));
        f.putClientProperty("title", state.get("title"));

        // Reset cached values
        Map cacheData = null;
        if ("album".equals(f.getClientProperty("view"))) {
            cacheData = Api.getInstance().getCategoryCache((Integer) f.getClientProperty("album-id"));
        } else {
            java.util.List<String> keys = (java.util.List<String>)f.getClientProperty("key");

            if ("tag".equals(f.getClientProperty("view"))) {
                cacheData = Api.getInstance().getTracksByTagsCache(keys);
            } else if ("content-type".equals(f.getClientProperty("view"))) {
                cacheData = Api.getInstance().getCategoriesByContentTypesCache(keys);
            }
        }

        if (cacheData != null) {
            setDataToModel(f, cacheData);
        }

        super.setState(f, state);
    }

    /**
     * Called when the form is closed to get the current state and store it.
     */
    public void getState(Form f, Map h) {
        h.put("album-id", f.getClientProperty("album-id"));
        h.put("view", f.getClientProperty("view"));
        h.put("key", f.getClientProperty("key"));
        h.put("title", String.valueOf(f.getClientProperty("title")));

        super.getState(f, h);
    }
    
    public void onBrowseAction(final Component c, ActionEvent event) {
        final List list = (List) event.getSource();
        final Map item = (Map) list.getSelectedItem();
        
        if("album".equals(item.get("type")))
        {
            show(MediaHelper.getId(item));
        }
        /*
        else if("video".equals(item.get("subtype")))
        {
            String url = null;
            for (Map itemMedia : (ArrayList<Map>)item.get("media")) {
                if ("video".equals(itemMedia.get("type"))) {
                    Map file = ((ArrayList<Map>)itemMedia.get("files")).get(0);
                    url = (String)file.get("url");
                }
            }
            
            if(Display.getInstance().getPlatformName().equals("ios"))
            {
                try {
                    Media m = MediaManager.createMedia(url, true);
                    m.setNativePlayerMode(true);
                    m.play();
                } catch (IOException ex) {

                }
            }
            else
                Display.getInstance().execute(url);
        }
                */
        else
        {
            Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
            if (clickedButton != null) {
                // Occurs when touching a button
                if (clickedButton.getName().equals("btnMediaActionFixed")) {
                    ui.dialogOptions.showTrackOptions(item);
                    return;
                }
            }

            ui.player.setQueueAndPlay((TrackListModel)list.getModel());
        }
    }
}
