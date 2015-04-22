/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.MediaHelper;
import com.codenameone.music.api.IJsonResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class SearchView extends ScrollableView {
    protected Boolean[] isActiveFilterArray;
    public static String FORM_NAME = "ScreenSearch";

    public SearchView(StateMachine ui) {
        this.ui = ui;

        isActiveFilterArray = new Boolean[4];
        Arrays.fill(isActiveFilterArray, Boolean.FALSE);
        isActiveFilterArray[0] = Boolean.TRUE;
    }

    @Override
    List getList(Form f) {
        return ui.findCtlSearchResult(f);
    }

    public void show()
    {
        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(final Form f)
    {
        f.setTitle(ui.translate("view_title_search", "[default]Search MusicPlayer"));
        updateFilters(f);
        
        ui.findTfdSearch(f).getHintLabel().setUIID("TextFieldSearchHint");
        ui.findTfdSearch(f).putClientProperty("searchField", Boolean.TRUE);
        
        ui.hideComponent(ui.findCtnSearchEmptyResult(f));
        
        Display.getInstance().callSerially(new Runnable() {
            public void run() {
                if (f.getClientProperty("search-term") != null && ((String)f.getClientProperty("search-term")).length() > 0) {
                    String term = (String)f.getClientProperty("search-term");

                    Map data = Api.getInstance().getSearchCache(term, getFilters());
                    if (data == null) {
                        search(term, f);
                    }

                    ui.findTfdSearch(f).setText(term);
                }
                else if (Storage.getInstance().exists("SearchHistory"))
                {
                    ArrayList<Map> searchHistory = (ArrayList<Map>)Storage.getInstance().readObject("SearchHistory");
                    ui.findCtlSearchResult(f).setRenderer(new GenericListCellRenderer(StateMachine.createContainer("ViewSearchHistoryRow"), StateMachine.createContainer("ViewSearchHistoryRow")));
                    ui.findCtlSearchResult(f).setModel(new DefaultListModel<Map>(searchHistory));
                }
            }
        });
        
        ui.findTfdSearch(f).setDoneListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                search(ui.findTfdSearch(f).getText(), f);
            }
        });

        beforeShowAfterModel(f);
    }
    
    public void postShow(Form f)
    {   
        if(f.getClientProperty("search-term") == null)
        {
            ui.findTfdSearch(f).requestFocus();
            Display.getInstance().editString(ui.findTfdSearch(f), 9999, TextField.ANY, "");
        }
    }

    public void search(String search)
    {
        search(search, Display.getInstance().getCurrent());
    }

    private ArrayList<String> getFilters()
    {
        final ArrayList<String> filters = new ArrayList<String>();

        // isActiveFilterArray[0] is for ALL, but you don't need to add filter for that.
        if (isActiveFilterArray[1])
            filters.add("song");
        if (isActiveFilterArray[2])
            filters.add("speech");
        if (isActiveFilterArray[3])
            filters.add("audiobook");

        return filters;
    }

    private void search(final String search, final Form f)
    {
        if (search.length() == 0)
            return;
        
        ui.findCtnSearchResult(f).addComponent(BorderLayout.NORTH, StateMachine.createContainer("ViewLoadingRow"));
        resetSearchResult(f);

        final ArrayList<String> filters = getFilters();

        ConnectionRequest request = Api.getInstance().search(new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                
                if (data.get("root") != null && !((ArrayList)data.get("root")).isEmpty()) 
                {
                    BorderLayout bl = (BorderLayout)ui.findCtnSearchResult(f).getLayout();
                    if(bl.getNorth() != null)
                    {
                        ui.findCtnSearchResult(f).removeComponent(bl.getNorth());
                        f.repaint();
                    }

                    ArrayList<Map> documents = (ArrayList<Map>)data.get("root");
                    ListModelHelper.prepareDocumentsForListing(documents);

                    ui.findCtlSearchResult(f).setRenderer(new GenericListCellRenderer(StateMachine.createContainer("ViewTrackRow"), StateMachine.createContainer("ViewTrackRow")));
                    ui.findCtlSearchResult(f).setModel(new TrackListModel(documents));
                    ui.findCtlSearchResult(f).putClientProperty("type", "documents");
                    ui.hideComponent(ui.findCtnSearchEmptyResult(f));
                    
                    if(!Storage.getInstance().exists("SearchHistory"))
                    {
                        ArrayList<Map> searchHistory = new ArrayList<Map>();
                        Storage.getInstance().writeObject("SearchHistory", searchHistory);
                    }
                    ArrayList<Map> searchHistory = (ArrayList<Map>)Storage.getInstance().readObject("SearchHistory");
                    Map searchTerm = new HashMap();
                    searchTerm.put("lblSearchHistoryTitle", search);
                    
                    if(!searchHistory.contains(searchTerm))
                        searchHistory.add(0, searchTerm);
                    
                    if(searchHistory.size() > 10)
                        searchHistory.remove(searchHistory.size() - 1);
                    Storage.getInstance().writeObject("SearchHistory", searchHistory);
                    
                } else {
                    ConnectionRequest suggestionRequest = Api.getInstance().suggest(new IJsonResponseHandler() {

                        public void onSuccess(Map data, Map<String, String> headers) {
                            BorderLayout bl = (BorderLayout)ui.findCtnSearchResult(f).getLayout();
                            if(bl.getNorth() != null)
                            {
                                ui.findCtnSearchResult(f).removeComponent(bl.getNorth());
                                f.repaint();
                            }

                            ArrayList<Map> suggestions = (ArrayList<Map>)data.get("root");

                            if(suggestions == null || suggestions.isEmpty())
                            {
                                ui.resetComponentHeight(ui.findCtnSearchEmptyResult(f));
                                ui.hideComponent(ui.findLblSearchEmptyResultSuggestionTitle(f));
                            }    
                            else
                            {
                                ui.resetComponentHeight(ui.findCtnSearchEmptyResult(f));
                                ui.resetComponentHeight(ui.findLblSearchEmptyResultSuggestionTitle(f));

                                ui.findCtlSearchResult(f).setRenderer(new GenericListCellRenderer(StateMachine.createContainer("ViewSuggestionRow"), StateMachine.createContainer("ViewSuggestionRow")));
                                ui.findCtlSearchResult(f).setModel(new DefaultListModel<Map>(suggestions));
                                ui.findCtlSearchResult(f).putClientProperty("type", "suggestion");
                            }

                            ui.findLblSearchEmptyResultKeyword(f).setText(search);
                        }

                        public void onFailure(int code, String message, Map<String, String> headers) {
                        }
                    }, search, filters);
                    ui.registerRequest(f, suggestionRequest);
                }
            }
            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
            }
        }, search, filters);
        ui.registerRequest(f, request);
    }

    private void resetSearchResult(Form f)
    {
        ui.hideComponent(ui.findCtnSearchEmptyResult(f));
        ui.findCtlSearchResult(f).setModel(new DefaultListModel<Map>(new ArrayList<Map>()));
        f.revalidate();
    }

    private void updateFilters() {
        updateFilters(Display.getInstance().getCurrent());
    }

    private void updateFilters(Form f)
    {
        Image iconAll;
      
        if (isActiveFilterArray[0])
            iconAll = StateMachine.getResourceFile().getImage("search_filter_all_active.png");
        else
            iconAll = StateMachine.getResourceFile().getImage("search_filter_all_static.png");
        
        iconAll.lock();
        ui.findBtnSearchFilterAll(f).setIcon(iconAll);
        Image iconSong;
        
        if (isActiveFilterArray[1])
            iconSong = StateMachine.getResourceFile().getImage("search_filter_song_active.png");
        else
            iconSong = StateMachine.getResourceFile().getImage("search_filter_song_static.png");

        iconSong.lock();
        ui.findBtnSearchFilterSong(f).setIcon(iconSong);
        Image iconSpeech;
        
        if (isActiveFilterArray[2])
            iconSpeech = StateMachine.getResourceFile().getImage("search_filter_speech_active.png");
        else
            iconSpeech = StateMachine.getResourceFile().getImage("search_filter_speech_static.png");

        iconSpeech.lock();
        ui.findBtnSearchFilterSpeech(f).setIcon(iconSpeech);
        Image iconBook;
        
        if (isActiveFilterArray[3])
            iconBook = StateMachine.getResourceFile().getImage("search_filter_audiobook_active.png");
        else
            iconBook = StateMachine.getResourceFile().getImage("search_filter_audiobook_static.png");
        
        iconBook.lock();
        ui.findBtnSearchFilterEbook(f).setIcon(iconBook);

        // Rerun search after updating the filters.
        search((ui.findTfdSearch(f)).getText());
    }
    
    public void onFilterButtonAction(Component c, ActionEvent event)
    {
        String filterName = c.getName();

        if (filterName.equals("btnSearchFilterAll")) {
            isActiveFilterArray[0] = !isActiveFilterArray[0];
            isActiveFilterArray[1] = false;
            isActiveFilterArray[2] = false;
            isActiveFilterArray[3] = false;
        } else if (filterName.equals("btnSearchFilterSong")) {
            isActiveFilterArray[0] = false;
            isActiveFilterArray[1] = !isActiveFilterArray[1];
        } else if (filterName.equals("btnSearchFilterSpeech")) {
            isActiveFilterArray[0] = false;
            isActiveFilterArray[2] = !isActiveFilterArray[2];
        } else if (filterName.equals("btnSearchFilterEbook")) {
            isActiveFilterArray[0] = false;
            isActiveFilterArray[3] = !isActiveFilterArray[3];
        }

        updateFilters();
    }

    public void onMediaAction(Component c, ActionEvent event) {
        final List list = (List) event.getSource();
        
        if("documents".equals(c.getClientProperty("type")))
        {
            final Map document = (Map)list.getSelectedItem();
            Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
            if (clickedButton != null) {
                // Occurs when touching a button
                if (clickedButton.getName().equals("btnMediaActionFixed")) {
                    ui.dialogOptions.showTrackOptions(document);
                    return;
                }
            }

            if ("track".equals(document.get("type"))) {
                ui.player.setQueueAndPlay((TrackListModel) list.getModel());
                ui.playerView.show();
            } else if ("album".equals(document.get("type"))) {
                ui.browseView.show(MediaHelper.getId(document));
            }
        }
        else if("suggestion".equals(c.getClientProperty("type")))
        {
            String query = (String)list.getSelectedItem();
            ui.findTfdSearch().setText(query);
            search(query);
        }
        else
        {
            Button clickedButton = ((GenericListCellRenderer)list.getRenderer()).extractLastClickedComponent();
            if (clickedButton != null) {
                // Occurs when touching a button
                if (clickedButton.getName().equals("btnSearchHistoryOptionFixed")) {
                    ArrayList<Map> searchHistory = (ArrayList<Map>)Storage.getInstance().readObject("SearchHistory");
                    searchHistory.remove(list.getSelectedIndex());
                    Storage.getInstance().writeObject("SearchHistory", searchHistory);
                    
                    ui.findCtlSearchResult(c.getComponentForm()).getModel().removeItem(list.getSelectedIndex());
                    return;
                }
            }
            
            Map query = (Map)list.getSelectedItem();
            ui.findTfdSearch(c.getComponentForm()).setText((String)query.get("lblSearchHistoryTitle"));
            search((String)query.get("lblSearchHistoryTitle"));
        }
    }

    public void onAbortSearchAction(Component c, ActionEvent event) {
        ui.findTfdSearch(c.getComponentForm()).setText("");
        resetSearchResult(c.getComponentForm());
        ui.findCtlSearchResult(c.getComponentForm()).putClientProperty("type", "");
        
        if (Storage.getInstance().exists("SearchHistory"))
        {
            ArrayList<Map> searchHistory = (ArrayList<Map>)Storage.getInstance().readObject("SearchHistory");
            ui.findCtlSearchResult(c.getComponentForm()).setRenderer(new GenericListCellRenderer(StateMachine.createContainer("ViewSearchHistoryRow"), StateMachine.createContainer("ViewSearchHistoryRow")));
            ui.findCtlSearchResult(c.getComponentForm()).setModel(new DefaultListModel<Map>(searchHistory));
        }
    }

    public void setState(Form f, Map state) {
        f.putClientProperty("search-term", state.get("search-term"));

        String term = (String)state.get("search-term");
        if (term != null && !"".equals(term)) {
            Map data = Api.getInstance().getSearchCache(term, getFilters());
            if (data != null) {
                ArrayList<Map> tracks = (ArrayList<Map>) data.get("root");
                ListModelHelper.prepareDocumentsForListing(tracks);

                ui.findCtlSearchResult(f).setRenderer(new GenericListCellRenderer(StateMachine.createContainer("ViewTrackRow"), StateMachine.createContainer("ViewTrackRow")));
                ui.findCtlSearchResult(f).setModel(new TrackListModel(tracks));
                ui.findCtlSearchResult(f).putClientProperty("type", "documents");
            }
        }

        super.setState(f, state);
    }

    public void getState(Form f, Map h) {
        h.put("search-term", ui.findTfdSearch(f).getText());

        super.getState(f, h);
    }
}
