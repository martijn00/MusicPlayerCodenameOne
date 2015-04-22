/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.DefaultListModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.FileManager;
import com.codenameone.music.Language;
import userclasses.StateMachine;

/**
 *
 * @author jei
 */
public class SettingsView extends ScrollableView {
    public final static String FORM_NAME = "ScreenSettings";

    public SettingsView(StateMachine ui) {
        this.ui = ui;
    }

    @Override
    List getList(Form f) {
        return ui.findLstSettings(f);
    }

    public void show() {
        ui.showForm(FORM_NAME, null);
    }

    public void initSettingsModel(List cmp) {
        cmp.setModel(getSettingsModel());
    }

    public void beforeShow(Form f) {
        f.setTitle(ui.translate("view_title_settings", "[default] Settings"));

        beforeShowAfterModel(f);
    }

    public DefaultListModel getSettingsModel() {
        Image iconArrow = StateMachine.getResourceFile().getImage("arrow.png");
        iconArrow.lock();
        
        ArrayList<Map> data = new ArrayList<Map>();
        Map option;
        
        Image iconCheckboxOnOff;
        if(Api.getInstance().isOnline())
            iconCheckboxOnOff = StateMachine.getResourceFile().getImage("checkbox_inactive.png");
        else
            iconCheckboxOnOff = StateMachine.getResourceFile().getImage("checkbox_active.png");
        iconCheckboxOnOff.lock();

        option = new HashMap();
        option.put("id", "offline");
        option.put("title", ui.translate("settings_rowtitle_offline", "[default] Offline mode"));
        option.put("subTitle", ui.translate("settings_rowsubtitle_offline", "[default] Only downloaded tracks will be available"));
        option.put("value", !Api.getInstance().isOnline());
        option.put("iconOpen", iconCheckboxOnOff);
        data.add(option);

        if(!Storage.getInstance().exists("NetworkDownloadMobileData"))
            Storage.getInstance().writeObject("NetworkDownloadMobileData", false);
        
        Image iconCheckboxDownloadOnOff;
        if(!((Boolean) Storage.getInstance().readObject("NetworkDownloadMobileData")))
            iconCheckboxDownloadOnOff = StateMachine.getResourceFile().getImage("checkbox_inactive.png");
        else
            iconCheckboxDownloadOnOff = StateMachine.getResourceFile().getImage("checkbox_active.png");
        iconCheckboxDownloadOnOff.lock();
        
        if(Api.getInstance().isOnline())
        {
            option = new HashMap();
            option.put("id", "download");
            option.put("title", ui.translate("settings_rowtitle_download", "[default]Download via 3g/4g"));
            option.put("subTitle", ui.translate("settings_rowsubtitle_download", "[default] Allow downloading via 3g/4g"));
            option.put("value", (Boolean)Storage.getInstance().readObject("NetworkDownloadMobileData"));
            option.put("iconOpen", iconCheckboxDownloadOnOff);
            data.add(option);
        }

        option = new HashMap();
        option.put("id", "applanguage");
        option.put("title", ui.translate("settings_rowtitle_applanguage", "[default] App language"));
        option.put("subTitle", ui.languageView.getLanguageName(Language.getInstance().getCurrentLanguage()));
        option.put("iconOpen", iconArrow);

        data.add(option);
        
        if(Api.getInstance().isOnline())
        {
            option = new HashMap();
            option.put("id", "contentlanguage");
            option.put("title", ui.translate("settings_rowtitle_contentlanguage", "[default] Content language"));
            option.put("subTitle", ui.languageView.getLanguageName(Api.getInstance().getLanguage()));
            option.put("iconOpen", iconArrow);
            data.add(option);
        }
        /*
        option = new Map();
        option.put("id", "cleardata");
        option.put("title", ui.translate("settings_rowtitle_cleardata", "[default] Clear cache & data"));
        option.put("subTitle", ui.translate("settings_rowsubtitle_cleardata", "[default] Will delete all MusicPlayer data from this device"));
        option.put("iconOpen", iconArrow);
        data.add(option);
        */
        
        if(Api.getInstance().isOnline())
        {
            option = new HashMap();
            option.put("id", "logout");
            option.put("title", ui.translate("settings_rowtitle_logout", "[default] Logout"));
            option.put("subTitle", ui.translate("settings_rowsubtitle_logout", "[default] You are logged in as: ") + Api.getInstance().getProfile().getFullName());
            option.put("iconOpen", iconArrow);
            data.add(option);
        }
        
        option = new HashMap();
        option.put("id", "copyright");
        option.put("title", ui.translate("settings_rowtitle_copyright", "[default] Copyright"));
        option.put("subTitle", ui.translate("settings_rowsubtitle_copyright", "[default] Stiftelsen Skjulte Skatters Forlag (SSSF)"));
        option.put("iconOpen", iconArrow);
        data.add(option);
        
        option = new HashMap();
        option.put("id", "contact");
        option.put("title", ui.translate("settings_rowtitle_contact", "[default] Contact MusicPlayer"));
        option.put("subTitle", ui.translate("settings_rowsubtitle_contact", "[default] Provide feedback and report errors"));
        option.put("iconOpen", iconArrow);
        data.add(option);

        if (!FileManager.getInstance().deviceSupportsHomeStorageOnly() && Api.getInstance().isOnline()) 
        {
            option = new HashMap();
            option.put("id", "storage");
            option.put("title", ui.translate("settings_rowtitle_storage", "[default] Default storage: ") + FileManager.getInstance().getRoot());
            option.put("subTitle", ui.translate("settings_rowsubtitle_storage", "[default] All downloaded content will be saved here"));
            option.put("iconOpen", iconArrow);
            data.add(option);
        }

        option = new HashMap();
        option.put("id", "appversion");
        option.put("title", ui.translate("settings_rowtitle_appversion", "[default] App version"));
        option.put("subTitle", ui.translate("settings_rowsubtitle_appversion", "[default] Current version: ") + Display.getInstance().getProperty("AppVersion", ""));
        data.add(option);
        
        return new DefaultListModel<Map>(data);
    }

    public void onListAction(final Component c, ActionEvent event) {
        List list = (List) event.getSource();
        Map item = (Map)list.getSelectedItem();

        if(item.get("id") == "offline") 
        {
            if(Api.getInstance().isOnline())
            {
                DialogConfirm.show(ui.translate("confirmation_go_offline", "[default] Do you really want to go offline?"), new Command(null) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        StateMachine.goOffline();
                    }
                }, null);
            }
            else
            {
                StateMachine.goOnline();
            }
        }
        else if(item.get("id") == "download") 
        {
            Storage.getInstance().writeObject("NetworkDownloadMobileData", !(Boolean)Storage.getInstance().readObject("NetworkDownloadMobileData"));
            ui.findLstSettings(c.getComponentForm()).setModel(getSettingsModel());
        }
        else if(item.get("id") == "applanguage") 
        {
            ui.languageView.show(
                Language.getInstance().getSupportedLanguages(),
                Language.getInstance().getCurrentLanguage(),
                new SettingsLanguageHelper() {

                    @Override
                    public void setLanguage(String language) {
                        Language.getInstance().setLanguage(language);
                        ui.back();
                    }
                }
            );
        }
        else if(item.get("id") == "contentlanguage") 
        {
            ui.languageView.show(
                Api.getInstance().getSupportedLanguages(),
                Api.getInstance().getLanguage(),
                new SettingsLanguageHelper() {

                    @Override
                    public void setLanguage(String language) {
                        InfiniteProgress inf = new InfiniteProgress();            
                        final Dialog progress = inf.showInifiniteBlocking();  

                        Api.getInstance().setLanguage(language);

                        progress.dispose();
                        ui.back();
                    }
                }
            );
        }
        else if(item.get("id") == "cleardata") 
        {
            DialogConfirm.show(ui.translate("settings_confirm_cleardata", "[default] Do you really want to delete all data?"), new Command(null) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    //TODO: Delete all cache and data
                    
                    FileManager.getInstance().resetRoot();
                }
            }, null);
        }
        else if(item.get("id") == "copyright") 
        {
            ui.disclaimerView.show();
        }
        else if(item.get("id") == "logout") 
        {
            DialogConfirm.show(ui.translate("settings_confirm_logout", "[default] Do you really want to logout?"), new Command(null) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    ui.player.stop();
                    ui.loginView.show(new Command(ui.translate("command_logout", "[default] Logout")));
                }
            }, null);
        }
        else if(item.get("id") == "contact") 
        {
            Display.getInstance().execute("mailto:someemail@someaddress.com");
        }
        else if(item.get("id") == "storage")
        {
            ArrayList<Command> commands = new ArrayList<Command>();

            FileSystemStorage storage = FileSystemStorage.getInstance();
            String roots[] = storage.getRoots();
            // Detect a root-folder to be used. SD-card is prefered over phone- and unknown storage types.
            int[] rootTypes = { FileSystemStorage.ROOT_TYPE_SDCARD, FileSystemStorage.ROOT_TYPE_MAINSTORAGE, FileSystemStorage.ROOT_TYPE_UNKNOWN };
            for (int rootType : rootTypes) {
                for (final String root1 : roots) {
                    if (storage.getRootType(root1) == rootType) {
                        Image icon = StateMachine.getResourceFile().getImage("popup_storage_icon.png");
                        icon.lock();
                        commands.add(new Command(root1 + "/MusicPlayer-App/", icon) {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                FileManager.getInstance().setRootDirectory(root1);
                                ui.findLstSettings(c.getComponentForm()).setModel(getSettingsModel());
                            }
                        });
                    }
                }
            }
            
            ui.dialogOptions.show(commands);
        }
        else if(item.get("id") == "appversion") 
        {
            
        }
    }
}
