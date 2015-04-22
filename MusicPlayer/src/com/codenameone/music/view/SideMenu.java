/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.codenameone.music.Api;
import com.codenameone.music.Language;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class SideMenu {
    protected StateMachine ui;
    
    private static String menuAppLanguage = "";
    
    static ArrayList<Command> onlineSideCommands = new ArrayList<Command>();
    static ArrayList<Command> offlineSideCommands = new ArrayList<Command>();
    
    private static final ArrayList<Integer> libraryItems = new ArrayList<Integer>();
    private static final List<String> baseViewNames = new ArrayList<String>();
    
    public SideMenu(StateMachine ui)
    {
        this.ui = ui;
        libraryItems.add(82819);

        baseViewNames.add(SearchView.FORM_NAME);
        baseViewNames.add(NewsView.FORM_NAME);
        baseViewNames.add(SettingsView.FORM_NAME);
    }

    private boolean shouldBuildMenu(Form f)
    {
        if (Display.getInstance().getPlatformName().equals("ios"))
        {
            // If the view is one of the views you can access from the menu, excepted from browsing.
            if (baseViewNames.contains(f.getName()))
                return true;

            // If the playlist-overview-view has no playlist (if it has, it's used to add tracks to a playlist)
            if (f.getName().equals(PlaylistOverviewView.FORM_NAME) && !ui.playlistOverviewView.hasTracklist())
                return true;

            // If the view is browse-view and it is not an album-view or the browse-id is in the library (is a root-category)
            return 
                    f.getName().equals(BrowseView.FORM_NAME) &&
                    (
                        libraryItems.contains((Integer)f.getClientProperty("album-id")) ||
                        !"album".equals(f.getClientProperty("view"))
                    )
            ;
        }
        else
            return true;
    }

    public void init(final Form f)
    {
        f.removeAllCommands();
        if(f.getName().equals(LoginView.FORM_NAME) && !(f instanceof Dialog))
        {
            Command backCommand = new Command(null){
                @Override
                public void actionPerformed(ActionEvent ev) {
                    Display.getInstance().exitApplication();
                }
            };
            f.setBackCommand(backCommand);
        }
        else if (f.getName().equals(CreateAccountView.FORM_NAME) || f.getName().equals(ForgotPasswordView.FORM_NAME))
        {
            addBackCommand(f);
        }
        else if (shouldBuildMenu(f))
        {
            if (Api.getInstance().isOnline())
            {
                buildOnlineMenu(f);
            }
            else
            {
                buildOfflineMenu(f);
            }
            
            if(f.getName().equals(ui.getHomeForm()) && Display.getInstance().getPlatformName().equals("and"))
            {
                Command backCommand = new Command(null){
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        if(ToastView.isToastVisible())
                            Display.getInstance().exitApplication();
                        else
                            ToastView.show(ui.translate("toast_exit_app", "[default] Press back again to exit app"), f);
                    }
                };
                f.setBackCommand(backCommand);
            }
            else if(
                    f.getName().equals(BrowseView.FORM_NAME) &&
                    (
                        libraryItems.contains((Integer)f.getClientProperty("album-id")) ||
                        !"album".equals(f.getClientProperty("view"))
                    )
            ) {
                Command backCommand = new Command(null){
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            ui.back();
                        }
                    };
                f.setBackCommand(backCommand);
            }
        }
        else if(f.getName().equals(QueueView.FORM_NAME) || f.getName().equals(PlayerView.FORM_NAME))
        {
            Image icon = StateMachine.getResourceFile().getImage("arrow_down_static.png");
            icon.lock();
            Image iconPressed = StateMachine.getResourceFile().getImage("arrow_down_active.png");
            iconPressed.lock();

            Command backCommand = new Command(null, icon){
                @Override
                public void actionPerformed(ActionEvent ev) {
                    if(f.getName().equals(QueueView.FORM_NAME))
                    {
                        Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createEmpty());
                        ui.back();
                    }
                    ui.back();
                }
            };
            backCommand.setPressedIcon(iconPressed);
            f.setBackCommand(backCommand);
        }
        else if (Display.getInstance().getPlatformName().equals("ios") && !f.getName().equals(QueueView.FORM_NAME))
        {
            addBackCommand(f);
        }
    }

    public boolean hasBackCommand(Form f)
    {
        return (!shouldBuildMenu(f) && Display.getInstance().getPlatformName().equals("ios") && !f.getName().equals(QueueView.FORM_NAME));
    }

    public void addBackCommand(Form f)
    {
        Image icon = StateMachine.getResourceFile().getImage("back_static.png");
        icon.lock();
        Image iconPressed = StateMachine.getResourceFile().getImage("back_active.png");
        iconPressed.lock();

        Command backCommand = new Command(null, icon){
            @Override
            public void actionPerformed(ActionEvent ev) {
                ui.back();
            }
        };
        backCommand.setPressedIcon(iconPressed);
        f.setBackCommand(backCommand);
    }

    /**
     * Build the menu by adding each command to it.
     * @param f 
     */
    private void buildOnlineMenu(final Form f)
    {
        if(onlineSideCommands.isEmpty() || !Language.getInstance().getCurrentLanguage().equals(menuAppLanguage))
        {
            onlineSideCommands.clear();
            menuAppLanguage = Language.getInstance().getCurrentLanguage();

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_search", "[default]Search MusicPlayer"),
                    "nav_search.png",
                    SearchView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.searchView.show();
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_news", "[default]News"),
                    "nav_news.png",
                    NewsView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.newsView.show();
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_playlists", "[default]My Playlists"),
                    "nav_playlist.png",
                    PlaylistOverviewView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.playlistOverviewView.show();
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_mp3kilden", "[default]MP3 Kilden"),
                    "nav_mp3.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("mp3-kilden");
                            ui.browseView.show("tag", keys, ui.translate("menu_mp3kilden", "[default]MP3 Kilden"));
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_childfavourites", "[default]Child favourites"),
                    "nav_barnas.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("child-favorites");
                            ui.browseView.show("tag", keys, ui.translate("menu_childfavourites", "[default]Child favourites"));
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_edification", "[default]Edification"),
                    "nav_edification.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("speech");
                            ui.browseView.show("content-type", keys, ui.translate("menu_edification", "[default]Edification"));
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_audiobook", "[default]Audiobooks"),
                    "nav_soundbook.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("audiobook");
                            ui.browseView.show("content-type", keys, ui.translate("menu_audiobook", "[default]Audiobooks"));
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_music", "[default]Music"),
                    "nav_album.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("song");
                            ui.browseView.show("content-type", keys, ui.translate("menu_music", "[default]Music"));
                        }
                    }
            ));
            /*
            TODO: Enable video when it's ready
            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_video", "[default]Video's"),
                    "nav_album.png",
                    null,
                    new Runnable() {
                        public void run() {
                            List<String> keys = new ArrayList<String>();
                            keys.add("video");
                            ui.browseView.show("content-type", keys, ui.translate("menu_video", "[default]Video's"));
                        }
                    }
            ));
            */
            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_norwegian_course", "[default]Norwegian course"),
                    "nav_kurs.png",
                    null,
                    new Runnable() {
                        public void run() {
                            ui.browseView.show(82819);
                        }
                    }
            ));

            onlineSideCommands.add(buildCommand(
                    ui.translate("menu_settings", "[default]Settings"),
                    "nav_settings.png",
                    SettingsView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.settingsView.show();
                        }
                    }
            ));
        }
        
        for(Command cmd : onlineSideCommands)
        {
            f.addCommand(cmd);
        }
    }
    
    private void buildOfflineMenu(final Form f)
    {
        if(offlineSideCommands.isEmpty() || !Language.getInstance().getCurrentLanguage().equals(menuAppLanguage))
        {
            offlineSideCommands.clear();
            menuAppLanguage = Language.getInstance().getCurrentLanguage();

            offlineSideCommands.add(buildCommand(
                    ui.translate("menu_playlists", "[default]My Playlists"),
                    "nav_playlist.png",
                    PlaylistOverviewView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.playlistOverviewView.show();
                        }
                    }
            ));

            offlineSideCommands.add(buildCommand(
                    ui.translate("menu_settings", "[default]Settings"),
                    "nav_settings.png",
                    SettingsView.FORM_NAME,
                    new Runnable() {
                        public void run() {
                            ui.settingsView.show();
                        }
                    }
            ));

            String offlineText;
            if(Api.getInstance().hasProfile())
                offlineText = ui.translate("menu_go_online", "[default] Go online again");
            else
                offlineText = ui.translate("login_button_login", "[default] Log in");
            
            offlineSideCommands.add(buildCommand(
                    offlineText,
                    "nav_go_online.png",
                    null,
                    new Runnable() {
                        public void run() {
                            StateMachine.goOnline();
                        }
                    }
            ));
        }
        
        for(Command cmd : offlineSideCommands)
        {
            f.addCommand(cmd);
        }
    }

    private Command buildCommand(String text, String iconKey, final String formName, final Runnable runnable)
    {
        Command cmd = new Command(text, StateMachine.getResourceFile().getImage(iconKey)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                SideMenuBar.closeCurrentMenu(new Runnable() {
                    @Override
                    public void run() {
                        if(formName == null || (Display.getInstance().getCurrent() != null && !formName.equals(Display.getInstance().getCurrent().getName())))
                        {
                            Display.getInstance().getCurrent().setTransitionOutAnimator(CommonTransitions.createEmpty());
                            runnable.run();
                        }
                    }
                });
            }
        };

        cmd.putClientProperty("uiid", "NavigationButton");
        cmd.putClientProperty("iconGap", 20);

        return cmd;
    }

    public SideMenuBar getInstance()
    {
        if (SideMenuBar.isShowing())
            return (SideMenuBar)Display.getInstance().getCurrent().getClientProperty("cn1$sideMenuParent");
        else
            return null;
    }
}