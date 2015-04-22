/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music.view;

import com.codename1.ui.Form;
import java.util.ArrayList;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.api.IJsonResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author mvandijk
 */
public class PerformerView {
    protected StateMachine ui;
    
    public final static String FORM_NAME = "ScreenPerformer";
    
    public PerformerView(StateMachine ui)
    {
        this.ui = ui;
    }
    
     public void show() {
        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(final Form f) {

        Api.getInstance().getPerformer(new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                f.setTitle((String)data.get("name"));
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, 36522 /* Harald Kronstad */);

        Api.getInstance().getPerformersTracks(new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                ArrayList<Map> tracks = (ArrayList<Map>)data.get("root");
                ListModelHelper.prepareTracksForListing(tracks);

                ui.findCtlArtistAllTracks(f).setModel(new TrackListModel(tracks));
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, 36522 /* Harald Kronstad */);
    }
}
