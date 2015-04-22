/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.ConnectionRequest;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import java.util.ArrayList;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.api.IJsonResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class NewsView extends ScrollableView {
    public static String FORM_NAME = "ScreenNews";

    public NewsView(StateMachine ui) {
        this.ui = ui;
    }

    @Override
    List getList(Form f) {
        return ui.findCtlNewsList(f);
    }

    public void show()
    {
        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(final Form f) {
        f.setTitle(ui.translate("view_title_news", "[default]News"));
        
        // Initial loading of news and reloading every time you refresh (by pulling down the list)
        ui.findCtlNewsList(f).addPullToRefresh(new Runnable() {
            public void run() {
                reloadModel(f, false);
            }
        });

        if (ui.findCtlNewsList(f).getModel().getSize() == 0) {
            reloadModel(f, true);
        } else {
            beforeShowAfterModel(f);
        }
    }

    private int newsFrom = 0;
    private int newsSize = 100;
    private TrackListModel newsTracks;
    
    protected void reloadModel(final Form f, boolean allowCache)
    {
        if(allowCache)
        {
            ui.findCtnListLoading(f).addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));
            f.repaint();
        }

        ConnectionRequest request = Api.getInstance().getNews(new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map news, Map<String, String> headers) {
                if (ui.findCtlNewsList(f).getModel().getSize() == 0) {
                    beforeShowAfterModel(f);
                }

                ArrayList<Map> tracks = (ArrayList<Map>)news.get("root");
                ListModelHelper.prepareTracksForListing(tracks);

                newsTracks = new TrackListModel(tracks);

                ui.findCtlNewsList(f).setModel(newsTracks);
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, allowCache, newsFrom, newsSize);
        ui.registerRequest(f, request);
    }

    protected void beforeShowAfterModel(Form f) {
        BorderLayout bl = (BorderLayout)ui.findCtnListLoading(f).getLayout();
        if(bl.getNorth() != null)
        {
            ui.findCtnListLoading(f).removeComponent(bl.getNorth());
            f.repaint();
        }
        else if(bl.getSouth() != null)
        {
            ui.findCtnListLoading(f).removeComponent(bl.getSouth());
            f.repaint();
        }

        super.beforeShowAfterModel(f);
    }

    public void onTrackAction(Component c, ActionEvent event) {
        final List list = (List) event.getSource();
        final Map item = (Map)list.getSelectedItem();

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

    public void initTrackList(final List cmp) {
        Map news = Api.getInstance().getNewsCache();

        if (news != null) {
            ArrayList<Map> tracks = (ArrayList<Map>) news.get("root");
            ListModelHelper.prepareTracksForListing(tracks);

            cmp.setModel(new TrackListModel(tracks));
        }
    }
}
