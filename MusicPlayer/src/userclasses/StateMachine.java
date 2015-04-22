/**
 * Your application code goes here
 */

package userclasses;

import com.codename1.analytics.AnalyticsService;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.list.ContainerList;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.SwipeBackSupport;
import generated.StateMachineBase;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import com.codenameone.music.Api;
import com.codenameone.music.GoogleAnalyticsCrashReporter;
import com.codenameone.music.GoogleAnalyticsService;
import com.codenameone.music.Language;
import com.codenameone.music.MediaPlayer;
import com.codenameone.music.PlayerStatus;
import com.codenameone.music.Playlist;
import com.codenameone.music.PushManager;
import com.codenameone.music.Queue;
import com.codenameone.music.UserProfile;
import com.codenameone.music.api.FileDownloadRequest;
import com.codenameone.music.api.IJsonResponseHandler;
import com.codenameone.music.mediaplayer.NativeAndroid;
import com.codenameone.music.mediaplayer.NativeIOS;
import com.codenameone.music.view.AlbumView;
import com.codenameone.music.view.BrowseView;
import com.codenameone.music.view.CreateAccountView;
import com.codenameone.music.view.DialogConfirm;
import com.codenameone.music.view.DialogNotice;
import com.codenameone.music.view.DialogOptions;
import com.codenameone.music.view.DisclaimerView;
import com.codenameone.music.view.EmbPlayerView;
import com.codenameone.music.view.ForgotPasswordView;
import com.codenameone.music.view.LanguageView;
import com.codenameone.music.view.LoginView;
import com.codenameone.music.view.NewsView;
import com.codenameone.music.view.PerformerView;
import com.codenameone.music.view.PlayerView;
import com.codenameone.music.view.PlaylistOverviewView;
import com.codenameone.music.view.PlaylistView;
import com.codenameone.music.view.QueueView;
import com.codenameone.music.view.SearchView;
import com.codenameone.music.view.SettingsView;
import com.codenameone.music.view.SideMenu;
import com.codenameone.music.view.TrackListModel;

/**
 *
 * @author Martijn00
 */
public class StateMachine extends StateMachineBase {

    private static final int storageVersion = 2;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }
    
    private static StateMachine instance;

    public MediaPlayer player;

    public PlayerView playerView;
    public LoginView loginView;
    public NewsView newsView;
    public BrowseView browseView;
    public SearchView searchView;
    public SettingsView settingsView;
    public PlaylistOverviewView playlistOverviewView;
    public PlaylistView playlistView;
    public LanguageView languageView;
    public SideMenu sideMenu;
    public QueueView queueView;
    public DisclaimerView disclaimerView;
    public DialogConfirm dialogConfirm;
    public DialogOptions dialogOptions;
    public DialogNotice dialogNotice;
    public CreateAccountView createAccountView;
    public ForgotPasswordView forgotPasswordView;
    public EmbPlayerView embPlayer;
    public PerformerView performerView;
    public AlbumView albumView;

    private java.util.List<ConnectionRequest> requests;

    @Override
    protected void setContainerState(Container cnt, Hashtable state) {
        super.setContainerState(cnt, state);
    }

    @Override
    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     * @param res
     */
    @Override
    protected void initVars(Resources res) {
        instance = this;
        
        setKeepResourcesInRam(true);

        if(
            !Storage.getInstance().exists("CleanStorageVersion") ||
            (Integer)Storage.getInstance().readObject("CleanStorageVersion") < storageVersion
        ) {
            Storage.getInstance().clearStorage();
            Storage.getInstance().writeObject("CleanStorageVersion", storageVersion);
        }
        
        Language.getInstance().init();
        
        // Initialize Google Analytics for screen tracking and crash reporting
        GoogleAnalyticsService ga = new GoogleAnalyticsService("UA-xxxxxxx-x");
        AnalyticsService.init(ga);
        GoogleAnalyticsCrashReporter gacr = new GoogleAnalyticsCrashReporter(ga);
        Display.getInstance().setCrashReporter(gacr);
        Display.getInstance().addEdtErrorHandler(gacr);

        // Initialize multiple network-threads and assign the classes to each thread. Now downloading should not block other requests.
        NetworkManager.getInstance().setThreadCount(2);
        if (NetworkManager.getInstance().getThreadCount() > 1) {
            NetworkManager.getInstance().assignToThread(FileDownloadRequest.class, 1);
        }

        Util.register("Playlist", Playlist.class);
        Util.register("UserProfile", UserProfile.class);
        Util.register("Queue", Queue.class);

        player = new MediaPlayer();

        if (Display.getInstance().getPlatformName().equals("ios")) {
            player.addPlayingListener(NativeIOS.getPlayingListener());
            player.addStatusChangeListener(NativeIOS.getStatusChangeListener());
            player.addCoverLoadedListener(NativeIOS.getCoverLoadedListener());
        } else {
            // TODO: Add a proper native player-notification for Android
            //player.addPlayingListener(NativeAndroid.getPlayingListener());
            //player.addStatusChangeListener(NativeAndroid.getStatusChangeListener());
            //player.addCoverLoadedListener(NativeAndroid.getCoverLoadedListener());
        }

        playerView = new PlayerView(this);
        loginView = new LoginView(this);
        browseView = new BrowseView(this);
        newsView = new NewsView(this);
        searchView = new SearchView(this);
        settingsView = new SettingsView(this);
        playlistOverviewView = new PlaylistOverviewView(this);
        playlistView = new PlaylistView(this);
        languageView = new LanguageView(this);
        sideMenu = new SideMenu(this);
        disclaimerView = new DisclaimerView(this);
        dialogConfirm = new DialogConfirm(this);
        dialogOptions = new DialogOptions(this);
        dialogNotice = new DialogNotice(this);
        queueView = new QueueView(this);
        createAccountView = new CreateAccountView(this);
        forgotPasswordView = new ForgotPasswordView(this);
        embPlayer = new EmbPlayerView(this);
        performerView = new PerformerView(this);
        albumView = new AlbumView(this);
    }
    
    public static StateMachine getInstance()
    {
        return instance;
    }
    
    public static Resources getResourceFile()
    {
        return instance.fetchResourceFile();
    }
    
    //This is the splashscreen shown on iOS.
    @Override
    protected boolean processBackground(Form f) {
        try {
            if(Display.getInstance().getPlatformName().equals("ios"))
                Thread.sleep(1500);
        } catch (InterruptedException ex) {
        }
        return true;
    }

    @Override
    protected String getFirstFormName() {
        if (Api.getInstance().getProfile() == null)
            return "ScreenLogin";
        else if (!Api.getInstance().isOnline())
            return "ScreenPlaylistOverview";
        else
            return "ScreenNews";
    }

    public void loadFormByUri(final String urlPath, final boolean loadNewsByDefault)
    {
        if (urlPath.length() > 0 && !urlPath.equals("/")) {
            StringTokenizer tokenizer = new StringTokenizer(urlPath, "/");
            try {
                String type = tokenizer.nextToken();

                if (type.equals("album")) {
                    instance.setHomeForm(BrowseView.FORM_NAME);
                    browseView.show(Integer.parseInt(tokenizer.nextToken()));
                    return;
                }

                if (type.equals("playlist") && tokenizer.nextElement().equals("private")) {
                    instance.setHomeForm(PlaylistView.FORM_NAME);
                    playlistView.show(Integer.parseInt(tokenizer.nextToken()));
                    return;
                }

                if (type.equals("track")) {

                    Integer trackId = Integer.parseInt(tokenizer.nextToken());

                    String language;
                    try {
                        language = tokenizer.nextToken();
                    } catch(NoSuchElementException e) {
                        language = Api.getInstance().getLanguage();
                    }

                    Api.getInstance().getTrack(new IJsonResponseHandler() {
                        @Override
                        public void onSuccess(Map track, Map<String, String> headers) {
                            final TrackListModel trackList = new TrackListModel();
                            trackList.addItem(track);

                            player.setQueueAndPlay(trackList);
                            if (loadNewsByDefault) {
                                instance.setHomeForm(NewsView.FORM_NAME);
                                newsView.show();
                            }

                            // TODO: Update the back-functionality for iPhone first. It doesn't show the menu here.
                            //instance.setHomeForm(BrowseView.FORM_NAME);
                            //browseView.show(MediaHelper.getParentId(track));
                        }

                        @Override
                        public void onFailure(int code, String message, Map<String, String> headers) {
                            if (loadNewsByDefault) {
                                instance.setHomeForm(NewsView.FORM_NAME);
                                newsView.show();
                            }
                        }
                    }, trackId, language, false);

                    return;
                }
            }
            catch (NoSuchElementException ignored) {}
            catch (NumberFormatException ignored) {}

            DialogConfirm.show(this.translate("notice_url_unresolved", "[default] The URL could not be resolved: " + urlPath), new Command(this.translate("command_dialog_retry", "[default] Retry")) {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (loadNewsByDefault) {
                        instance.setHomeForm(NewsView.FORM_NAME);
                        newsView.show();
                    }
                    Display.getInstance().execute("https://github.com/martijn00" + urlPath);
                }
            }, new Command(this.translate("button_ok", "[default] OK")) {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (loadNewsByDefault) {
                        instance.setHomeForm(NewsView.FORM_NAME);
                        newsView.show();
                    }
                }
            });
        } else {
            if (loadNewsByDefault) {
                instance.setHomeForm(NewsView.FORM_NAME);
                newsView.show();
            }
        }
    }

    @Override
    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        Container ctn = super.startApp(res, resPath, loadTheme);

        // If it's not the login-screen ...
        if (Api.getInstance().getProfile() != null) {
            // Check if we have an app-argument
            final String urlPath = Display.getInstance().getProperty("AppArg", "");
            if (urlPath.length() > 0) {
                Display.getInstance().setProperty("AppArg", null);

                if (Api.getInstance().isOnline()) {
                    loadFormByUri(urlPath, false);
                } else {
                    goOnline(new Runnable() {

                        @Override
                        public void run() {
                            loadFormByUri(urlPath, true);
                        }
                    });
                }
            }
        }

        //TODO: enable push when backend is ready
        //PushManager.getInstance().registerForPush();

        return ctn;
    }

    public static void goOnline()
    {
        goOnline(new Runnable() {
            @Override
            public void run() {
                instance.setHomeForm(NewsView.FORM_NAME);
                StateMachine.getInstance().newsView.show();
            }
        });
    }

    public static void goOnline(final Runnable run)
    {
        InfiniteProgress inf = new InfiniteProgress();
        final Dialog progress = inf.showInifiniteBlocking();

        Api.getInstance().goOnline(new IJsonResponseHandler() {

            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                progress.dispose();
                if (Api.getInstance().getProfile() == null)
                {
                    instance.setHomeForm(LoginView.FORM_NAME);
                    StateMachine.getInstance().loginView.show();
                }
                else
                {
                    run.run();
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                progress.dispose();
            }
        });
    }

    public static void goOffline()
    {
        Api.getInstance().goOffline();

        instance.setHomeForm(PlaylistOverviewView.FORM_NAME);
        instance.playlistOverviewView.show();
    }

    /**
     * Set a form as new home-screen and clear the form-history.
     * @param formName 
     */
    @Override
    public void setHomeForm(String formName)
    {
        super.setHomeForm(formName);
        clearViewHistory();
    }

    /*
     *  ------------------ MediaPlayer - Utility methods -------------------
     */
    
    public void headphonesDisconnected() {
        player.pause();
    }

    public void headphonesConnected() {
        if(player.getStatus() == PlayerStatus.PAUSED)
            player.resume();
    }

    /*
     *  ----------------- General overrides for all views ------------------
     */

    @Override
    protected void beforeShow(Form f) {
        super.beforeShow(f);

        if (!(f instanceof Dialog) && !(Boolean.TRUE).equals(f.getClientProperty("menuDisabled"))) {
            sideMenu.init(f);
            embPlayer.init(f);
        }
        
        f.addGameKeyListener(Display.MEDIA_KEY_PLAY_STOP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                player.playPause();
            }
        });
        f.addGameKeyListener(Display.MEDIA_KEY_SKIP_BACK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                player.playPrevious();
            }
        });
        f.addGameKeyListener(Display.MEDIA_KEY_SKIP_FORWARD, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                player.playNext();
            }
        });
        
        /*
        if(f.getBackCommand() != null) {
            SwipeBackSupport.bindBack(f, createBackLazyValue(f));
        }*/
    }

    @Override
    protected void exitForm(Form f) {
        super.exitForm(f);

        requests = (java.util.List<ConnectionRequest>) f.getClientProperty("requests");

        if (requests == null) {
            return;
        }

        for(ConnectionRequest request : requests) {
            Log.p("Killed request " + request.getUrl());

            request.kill();
        }

        requests.clear();

        f.putClientProperty("requests", requests);
    }

    /*
     *  ------------------ UI manipulation - Utility methods -------------------
     */

    public void hideComponent(Component c)
    {
        c.setHeight(0);
        c.setPreferredH(0);

        if (c.getParent() != null) {
            c.getParent().revalidate();
        }

        c.setVisible(false);
    }

    public void resetComponentHeight(Component c)
    {
        c.setPreferredSize(null);
        c.setVisible(true);

        if (c.getParent() != null) {
            c.getParent().revalidate();
        }
    }
    
    public static Container createContainer(String id)
    {
        return instance.createContainer(instance.fetchResourceFile(), id);
    }
    
    public static int getPixelFromMM(int mm, Boolean horizontal)
    {
        return Display.getInstance().convertToPixels(mm, horizontal);
    }
    
    //TODO: Remove this when Shai has fixed deleting titlecommands
    public void removeTitleCommand(Form f)
    {
        removeTitleCommandFromComponent(f, f.getTitleArea());

        // Some trigger deletes the back-button when deleting the title-command...
        // @TODO: Check what trigger removes the back-command here.
        if (sideMenu.hasBackCommand(f))
            sideMenu.addBackCommand(f);
    }
    
    //TODO: Remove this when Shai has fixed deleting titlecommands
    private void removeTitleCommandFromComponent(Form f, Container c)
    {
        // Some trigger deletes a bunch of components. I have to always keep the count updated.
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component current = c.getComponentAt(i);
            
            if (current instanceof Container) {
                removeTitleCommandFromComponent(f, (Container) current);
            }

            if (current instanceof Button && current.getClientProperty("TitleCommand") != null) {
                Button btn = (Button)current;
                c.removeComponent(current);
                c.revalidate();
                f.removeCommand(btn.getCommand());
                i--;
            }
        }
    }

    /*
     *  ------------------ General Utility methods -----------------------------
     */

    /**
     * To have this method available in our view-classes
     * @return 
     */
    
    @Override
    protected String getBackCommandText(String previousFormTitle) {
         return "";
    }

    /**
     * @param key
     * @param defaultValue
     * @return 
     */
    public String translate(String key, String defaultValue)
    {
        return UIManager.getInstance().localize(key, defaultValue).equals("") ? defaultValue : UIManager.getInstance().localize(key, defaultValue);
    }

    public static String _translate(String key, String defaultValue)
    {
        return instance.translate(key, defaultValue);
    }
    
    /**
     * Clearing the view-history, used when calling the back() method.
     */
    public void clearViewHistory()
    {
        setBackCommandEnabled(false);
        setBackCommandEnabled(true);
    }

    public void registerRequest(Form f, ConnectionRequest request)
    {
        if (request == null) {
            return;
        }

        requests = (java.util.List<ConnectionRequest>) f.getClientProperty("requests");

        if (requests == null) {
            requests = new ArrayList<ConnectionRequest>();
        }

        requests.add(request);

        f.putClientProperty("requests", requests);
    }

    /*
     * ------------------ ScreenLogin -------------------------------------
     */

    @Override
    protected void beforeScreenLogin(Form f) {
        f.putClientProperty("menuDisabled", true);
        loginView.beforeShow(f);
    }
    
    @Override
    protected void onScreenLogin_BtnLoginLoginAction(Component c, ActionEvent event) {
        loginView.onLoginAction(c.getComponentForm());
    }

    @Override
    protected void onScreenLogin_BtnLoginCreateAccountAction(Component c, ActionEvent event) {
        loginView.onCreateAccountAction();
    }

    @Override
    protected void onScreenLogin_BtnLoginForgotPasswordAction(Component c, ActionEvent event) {
        loginView.onForgotPasswordAction();
    }
    
    @Override
    protected void onScreenLogin_BtnLoginOfflineAction(Component c, ActionEvent event) 
    {
        loginView.onLoginOfflineAction();
    }

    /*
     * ------------------ ScreenNews -----------------------------------
     */

    @Override
    protected void beforeScreenNews(Form f) {
        newsView.beforeShow(f);
    }

    @Override
    protected boolean initListModelCtlNewsList(List cmp) {
        newsView.initTrackList(cmp);
        return true;
    }

    @Override
    protected void onScreenNews_CtlNewsListAction(Component c, ActionEvent event) {
        newsView.onTrackAction(c, event);
    }

    @Override
    protected void setStateScreenNews(Form f, Hashtable state) {
        newsView.setState(f, state);
    }

    @Override
    protected void getStateScreenNews(Form f, Hashtable h) {
        newsView.getState(f, h);
    }

    /*
     * ---------- ScreenPlaylistOverview / ScreenChoosePlaylist ----------------
     */

    @Override
    protected void beforeScreenPlaylistOverview(Form f) {
        playlistOverviewView.beforeShow(f);
    }

    @Override
    protected boolean initListModelCtlOverviewPlaylistList(List cmp) {
        playlistOverviewView.initPlaylistsModel(cmp);
        return true;
    }

    @Override
    protected void onScreenPlaylistOverview_CtlOverviewPlaylistListAction(Component c, ActionEvent event) {
        playlistOverviewView.onPlaylistAction(c, event);
    }
    
    @Override
    protected void onScreenPlaylistOverview_BtnAddPlaylistFormSaveAction(Component c, ActionEvent event) {
        playlistOverviewView.onAddPlaylistSaveAction(c, event);
    }

    @Override
    protected void setStateScreenPlaylistOverview(Form f, Hashtable state) {
        playlistOverviewView.setState(f, state);
    }

    @Override
    protected void getStateScreenPlaylistOverview(Form f, Hashtable h) {
        playlistOverviewView.getState(f, h);
    }

    /*
     * ------------------ ScreenPlaylist ---------------------------------------
     */

    @Override
    protected void beforeScreenPlaylist(Form f) {
        playlistView.beforeShow(f);
    }

    @Override
    protected void onScreenPlaylist_CtlPlaylistMediaItemsAction(Component c, ActionEvent event) {
        playlistView.onPlaylistAction(c, event);
    }

    @Override
    protected void onScreenPlaylist_ChkPlaylistIsOfflineAvailableAction(Component c, ActionEvent event) {
        playlistView.onIsOfflineAvailableAction(c, event);
    }

    @Override
    protected void onScreenPlaylist_BtnEditingConfirmAction(Component c, ActionEvent event) {
        playlistView.onEditPlaylistTitleConfirm(c.getComponentForm());
    }

    @Override
    protected void setStateScreenPlaylist(Form f, Hashtable state) {
        playlistView.setState(f, state);
    }

    @Override
    protected void getStateScreenPlaylist(Form f, Hashtable h) {
        playlistView.getState(f, h);
    }

    /*
     * ------------------ ScreenBrowse --------------------------------
     */

    @Override
    protected void beforeScreenBrowse(Form f) {
        browseView.beforeShow(f);
    }
    
    @Override
    protected void onScreenBrowse_CtlBrowseItemsAction(Component c, ActionEvent event) {
        browseView.onBrowseAction(c, event);
    }

    @Override
    protected void setStateScreenBrowse(Form f, Hashtable state) {
        browseView.setState(f, state);
    }

    @Override
    protected void getStateScreenBrowse(Form f, Hashtable h) {
        browseView.getState(f, h);
    }
    
    /*
     * ------------------ ScreenPlayer -------------------------------------
     */

    @Override
    protected void beforeScreenPlayer(Form f) {
        playerView.beforeShow(f);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerPreviousAction(Component c, ActionEvent event) {
        playerView.onPreviewsAction(c, event);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerPlayPauseAction(Component c, ActionEvent event) {
        playerView.onPlayPauseAction(c, event);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerNextAction(Component c, ActionEvent event) {
        playerView.onNextAction(c, event);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerRepeatAction(Component c, ActionEvent event) {
        playerView.onRepeatAction(c, event);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerShuffleAction(Component c, ActionEvent event) {
        playerView.onShuffleAction(c, event);
    }

    @Override
    protected void onScreenPlayer_BtnPlayerActionAction(Component c, ActionEvent event) 
    {
        playerView.onOptionAction(c, event);
    }
    
    @Override
    protected void onScreenPlayer_BtnPlayerPlaylistAction(Component c, ActionEvent event) {
        playerView.onPlaylistAction(c, event);
    }

    /*
     * ------------------ ScreenSettings -------------------------------------
     */

    @Override
    protected void beforeScreenSettings(Form f) {
        settingsView.beforeShow(f);
    }

    @Override
    protected boolean initListModelLstSettings(List cmp) {
        settingsView.initSettingsModel(cmp);
        return true;
    }

    @Override
    protected void onScreenSettings_LstSettingsAction(Component c, ActionEvent event) {
        settingsView.onListAction(c, event);
    }

    @Override
    protected void setStateScreenSettings(Form f, Hashtable state) {
        settingsView.setState(f, state);
    }

    @Override
    protected void getStateScreenSettings(Form f, Hashtable h) {
        settingsView.getState(f, h);
    }

    /*
     * ------------------ ScreenSearch -------------------------------------
     */

    @Override
    protected void beforeScreenSearch(Form f) {
        searchView.beforeShow(f);
    }
    
        @Override
    protected void postScreenSearch(Form f) {
        searchView.postShow(f);
    }

    @Override
    protected void onScreenSearch_CtlSearchResultAction(Component c, ActionEvent event) {
        searchView.onMediaAction(c, event);
    }

    @Override
    protected void onScreenSearch_BtnAbortSearchAction(Component c, ActionEvent event) {
        searchView.onAbortSearchAction(c, event);
    }

    @Override
    protected void onScreenSearch_BtnSearchFilterAllAction(Component c, ActionEvent event) {
        searchView.onFilterButtonAction(c, event);
    }

    @Override
    protected void onScreenSearch_BtnSearchFilterSongAction(Component c, ActionEvent event) {
        searchView.onFilterButtonAction(c, event);
    }

    @Override
    protected void onScreenSearch_BtnSearchFilterSpeechAction(Component c, ActionEvent event) {
        searchView.onFilterButtonAction(c, event);
    }

    @Override
    protected void onScreenSearch_BtnSearchFilterEbookAction(Component c, ActionEvent event) {
        searchView.onFilterButtonAction(c, event);
    }

    @Override
    protected void setStateScreenSearch(Form f, Hashtable state) {
        searchView.setState(f, state);
    }

    @Override
    protected void getStateScreenSearch(Form f, Hashtable h) {
        searchView.getState(f, h);
    }

    /*
     * ------------------ ScreenLanguage -------------------------------------
     */

    @Override
    protected void beforeScreenLanguage(Form f) {
        languageView.beforeShow(f);
    }
    
    @Override
    protected boolean initListModelCtlAllLanguages(List cmp) {
        return languageView.initAllLanguagesModel(cmp);
    }

    @Override
    protected void onScreenLanguage_CtlAllLanguagesAction(Component c, ActionEvent event) {
        languageView.onLanguageAction(c, event);
    }

    /*
     * ------------------ ScreenDisclaimer -------------------------------------
     */

    @Override
    protected void beforeScreenDisclaimer(Form f) {
        disclaimerView.beforeShow(f);
    }

    /*
     * ------------------ ScreenQueue -------------------------------------
     */

    @Override
    protected void beforeScreenQueue(Form f) {
        queueView.beforeShow(f);
    }

    @Override
    protected void onScreenQueue_CtlPlayerQueueAction(Component c, ActionEvent event) {
        queueView.onQueueAction(c, event);
    }

    @Override
    protected boolean initListModelCtlPlayerQueue(List cmp) {
        return queueView.initQueueModel(cmp);
    }
    
    /*
     * ------------------ ScreenForgotPassword -------------------------------------
     */
    
    @Override
    protected void beforeScreenForgotPassword(Form f) {
        forgotPasswordView.beforeShow(f);
    }
    
    /*
     * ------------------ ScreenCreateAccount -------------------------------------
     */

    @Override
    protected void beforeScreenCreateAccount(Form f) {
        createAccountView.beforeShow(f);
    }

    /*
     * ------------------ ScreenArtist -----------------------------------------
     */
    
    @Override
    protected void beforeScreenPerformer(Form f) {
        performerView.beforeShow(f);
    }
    
    @Override
    protected boolean initListModelCtlArtistAppearsIn(ContainerList cmp) {
        return true;
    }

    @Override
    protected boolean initListModelCtlArtistAllTracks(ContainerList cmp) {
        return true;
    }
    
    /*
     * ------------------ ScreenAlbum -----------------------------------------
     */

    @Override
    protected void beforeScreenAlbum(Form f) {
        albumView.beforeShow(f);
    }
}
