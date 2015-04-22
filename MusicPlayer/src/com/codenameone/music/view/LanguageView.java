/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.DefaultListModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class LanguageView {
    protected StateMachine ui;
    private final static String FORM_NAME = "ScreenLanguage";
    public final static String LOCATION = "national";
    public final static String DICTIONARY = "Languages";
    
    private ArrayList<String> languageKeys;
    private String current;
    private SettingsLanguageHelper handler;
    private Map<String, String> languageNames;

    public LanguageView(StateMachine ui) {
        this.ui = ui;
    }

    private Map<String, String> getLanguageNames()
    {
        if (languageNames == null) {
            languageNames = StateMachine.getResourceFile().getL10N(DICTIONARY, LOCATION);
        }

        return languageNames;
    }

    public String getLanguageName(String language)
    {
        return getLanguageNames().get(language);
    }

    public void show(ArrayList<String> languageKeys, String current, SettingsLanguageHelper handler)
    {
        this.languageKeys = languageKeys;
        this.current = current;
        this.handler = handler;
        ui.showForm(FORM_NAME, null);
    }

    public void beforeShow(Form f)
    {
        f.setTitle(ui.translate("language_title", "[default]Choose language"));
    }

    public boolean initAllLanguagesModel(List cmp)
    {
        Map selectedLanguage = null;

        Map<String, Map> data = new TreeMap<String, Map>() {};
        for (String languageKey : languageKeys) {
            Map element = new HashMap();

            if (!languageKey.equals("source")) {
                if (current.equals(languageKey)) {
                    Image icon = StateMachine.getResourceFile().getImage("language_check.png");
                    icon.lock();

                    element.put("lblChosen", icon);

                    selectedLanguage = element;
                }
                element.put("shortLanguage", languageKey);
                element.put("longLanguage", getLanguageNames().get(languageKey));
                data.put(getLanguageNames().get(languageKey), element);
            }
        }

        Collection<Map> langauges = data.values();

        int selectedPosition = -1;
        for (Map langauge : langauges) {
            selectedPosition++;

            if (langauge == selectedLanguage) {
                break;
            }
        }

        cmp.setModel(new DefaultListModel<Map>(langauges));

        if (selectedPosition < langauges.size()) {
            cmp.setSelectedIndex(selectedPosition);
        }
        
        return true;
    }

    public void onLanguageAction(Component c, ActionEvent event) {
        List list = (List) event.getSource();
        final Map item = (Map)list.getSelectedItem();
        
        if(current.equals(item.get("shortLanguage").toString()))
            ui.back();
        else
            handler.setLanguage(item.get("shortLanguage").toString());
    }
}
