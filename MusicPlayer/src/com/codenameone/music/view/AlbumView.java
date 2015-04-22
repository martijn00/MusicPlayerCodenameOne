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
public class AlbumView {
    protected StateMachine ui;
    
    public final static String FORM_NAME = "ScreenAlbum";
    
    public AlbumView(StateMachine ui)
    {
        this.ui = ui;
    }
    
     public void show() {
        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(Form f) {
        f.setTitle(ui.translate("view_title_album", "[default]Album"));
        
        Api.getInstance().getCategory(new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                //f.getTitleComponent().setEndsWith3Points(true);
                //f.setTitle((String) data.get("title"));
                ArrayList<Map> tracks = (ArrayList<Map>)data.get("tracks");
                ListModelHelper.prepareTracksForListing(tracks);
                
            }
            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, 84906, false);
    }
}
