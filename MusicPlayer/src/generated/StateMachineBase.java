/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import java.util.Hashtable;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("ImageViewer", com.codename1.components.ImageViewer.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("Slider", com.codename1.ui.Slider.class);
        UIBuilder.registerCustomComponent("WebBrowser", com.codename1.components.WebBrowser.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("SpanLabel", com.codename1.components.SpanLabel.class);
        UIBuilder.registerCustomComponent("List", com.codename1.ui.List.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("ContainerList", com.codename1.ui.list.ContainerList.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "ScreenLogin";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("ImageViewer", com.codename1.components.ImageViewer.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("Slider", com.codename1.ui.Slider.class);
        UIBuilder.registerCustomComponent("WebBrowser", com.codename1.components.WebBrowser.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("SpanLabel", com.codename1.components.SpanLabel.class);
        UIBuilder.registerCustomComponent("List", com.codename1.ui.List.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("ContainerList", com.codename1.ui.list.ContainerList.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "ScreenLogin");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.components.ImageViewer findImgArtistImage(Component root) {
        return (com.codename1.components.ImageViewer)findByName("imgArtistImage", root);
    }

    public com.codename1.components.ImageViewer findImgArtistImage() {
        com.codename1.components.ImageViewer cmp = (com.codename1.components.ImageViewer)findByName("imgArtistImage", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.ImageViewer)findByName("imgArtistImage", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlPlaylistMediaItems(Component root) {
        return (com.codename1.ui.List)findByName("ctlPlaylistMediaItems", root);
    }

    public com.codename1.ui.List findCtlPlaylistMediaItems() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlPlaylistMediaItems", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlPlaylistMediaItems", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnLoginScroll(Component root) {
        return (com.codename1.ui.Container)findByName("ctnLoginScroll", root);
    }

    public com.codename1.ui.Container findCtnLoginScroll() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnLoginScroll", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnLoginScroll", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnSearchEmptyResult(Component root) {
        return (com.codename1.ui.Container)findByName("ctnSearchEmptyResult", root);
    }

    public com.codename1.ui.Container findCtnSearchEmptyResult() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnSearchEmptyResult", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnSearchEmptyResult", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerPrevious(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerPrevious", root);
    }

    public com.codename1.ui.Button findBtnPlayerPrevious() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerPrevious", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerPrevious", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnLoginCreateAccount(Component root) {
        return (com.codename1.ui.Button)findByName("btnLoginCreateAccount", root);
    }

    public com.codename1.ui.Button findBtnLoginCreateAccount() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnLoginCreateAccount", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnLoginCreateAccount", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewOptionsRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewOptionsRow", root);
    }

    public com.codename1.ui.Container findViewOptionsRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewOptionsRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewOptionsRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchFilterEbook(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchFilterEbook", root);
    }

    public com.codename1.ui.Button findBtnSearchFilterEbook() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchFilterEbook", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchFilterEbook", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlPlayerQueue(Component root) {
        return (com.codename1.ui.List)findByName("ctlPlayerQueue", root);
    }

    public com.codename1.ui.List findCtlPlayerQueue() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlPlayerQueue", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlPlayerQueue", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblTitle", root);
    }

    public com.codename1.ui.Label findLblTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerAction(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerAction", root);
    }

    public com.codename1.ui.Button findBtnPlayerAction() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerAction", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerAction", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnMediaIcon(Component root) {
        return (com.codename1.ui.Button)findByName("btnMediaIcon", root);
    }

    public com.codename1.ui.Button findBtnMediaIcon() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnMediaIcon", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnMediaIcon", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewCheckBoxRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewCheckBoxRow", root);
    }

    public com.codename1.ui.Container findViewCheckBoxRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewCheckBoxRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewCheckBoxRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnLoginForgotPassword(Component root) {
        return (com.codename1.ui.Button)findByName("btnLoginForgotPassword", root);
    }

    public com.codename1.ui.Button findBtnLoginForgotPassword() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnLoginForgotPassword", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnLoginForgotPassword", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.InfiniteProgress findSpnPlayerLoading(Component root) {
        return (com.codename1.components.InfiniteProgress)findByName("spnPlayerLoading", root);
    }

    public com.codename1.components.InfiniteProgress findSpnPlayerLoading() {
        com.codename1.components.InfiniteProgress cmp = (com.codename1.components.InfiniteProgress)findByName("spnPlayerLoading", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.InfiniteProgress)findByName("spnPlayerLoading", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchFilterAll(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchFilterAll", root);
    }

    public com.codename1.ui.Button findBtnSearchFilterAll() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchFilterAll", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchFilterAll", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerNext(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerNext", root);
    }

    public com.codename1.ui.Button findBtnPlayerNext() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerNext", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerNext", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnMediaActionFixed(Component root) {
        return (com.codename1.ui.Button)findByName("btnMediaActionFixed", root);
    }

    public com.codename1.ui.Button findBtnMediaActionFixed() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnMediaActionFixed", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnMediaActionFixed", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlaylist(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlaylist", root);
    }

    public com.codename1.ui.Container findCtnPlaylist() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlaylist", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlaylist", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnSearchFilter(Component root) {
        return (com.codename1.ui.Container)findByName("ctnSearchFilter", root);
    }

    public com.codename1.ui.Container findCtnSearchFilter() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnSearchFilter", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnSearchFilter", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlaylistItems(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlaylistItems", root);
    }

    public com.codename1.ui.Container findCtnPlaylistItems() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlaylistItems", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlaylistItems", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTfdSearch(Component root) {
        return (com.codename1.ui.TextField)findByName("tfdSearch", root);
    }

    public com.codename1.ui.TextField findTfdSearch() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("tfdSearch", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("tfdSearch", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlaylistProgress(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlaylistProgress", root);
    }

    public com.codename1.ui.Container findCtnPlaylistProgress() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlaylistProgress", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlaylistProgress", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtEditing(Component root) {
        return (com.codename1.ui.TextField)findByName("txtEditing", root);
    }

    public com.codename1.ui.TextField findTxtEditing() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtEditing", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtEditing", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewPlaylistRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewPlaylistRow", root);
    }

    public com.codename1.ui.Container findViewPlaylistRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewPlaylistRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewPlaylistRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewSuggestionRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewSuggestionRow", root);
    }

    public com.codename1.ui.Container findViewSuggestionRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewSuggestionRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewSuggestionRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSearchSuggestionText(Component root) {
        return (com.codename1.ui.Label)findByName("lblSearchSuggestionText", root);
    }

    public com.codename1.ui.Label findLblSearchSuggestionText() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSearchSuggestionText", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSearchSuggestionText", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnLoginLogin(Component root) {
        return (com.codename1.ui.Button)findByName("btnLoginLogin", root);
    }

    public com.codename1.ui.Button findBtnLoginLogin() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnLoginLogin", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnLoginLogin", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSettingsSecondTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblSettingsSecondTitle", root);
    }

    public com.codename1.ui.Label findLblSettingsSecondTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSettingsSecondTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSettingsSecondTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findTitle(Component root) {
        return (com.codename1.ui.Label)findByName("title", root);
    }

    public com.codename1.ui.Label findTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("title", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("title", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlayerTitleName(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlayerTitleName", root);
    }

    public com.codename1.ui.Label findLblPlayerTitleName() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlayerTitleName", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlayerTitleName", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewSettingsCheckboxRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewSettingsCheckboxRow", root);
    }

    public com.codename1.ui.Container findViewSettingsCheckboxRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewSettingsCheckboxRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewSettingsCheckboxRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainerMediaRow(Component root) {
        return (com.codename1.ui.Container)findByName("ContainerMediaRow", root);
    }

    public com.codename1.ui.Container findContainerMediaRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ContainerMediaRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ContainerMediaRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnListLoading(Component root) {
        return (com.codename1.ui.Container)findByName("ctnListLoading", root);
    }

    public com.codename1.ui.Container findCtnListLoading() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnListLoading", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnListLoading", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTfdAddPlaylistFormTitle(Component root) {
        return (com.codename1.ui.TextField)findByName("tfdAddPlaylistFormTitle", root);
    }

    public com.codename1.ui.TextField findTfdAddPlaylistFormTitle() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("tfdAddPlaylistFormTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("tfdAddPlaylistFormTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnOpenPlayer(Component root) {
        return (com.codename1.ui.Button)findByName("btnOpenPlayer", root);
    }

    public com.codename1.ui.Button findBtnOpenPlayer() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnOpenPlayer", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnOpenPlayer", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlayerCover(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlayerCover", root);
    }

    public com.codename1.ui.Container findCtnPlayerCover() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlayerCover", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlayerCover", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewCategoryRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewCategoryRow", root);
    }

    public com.codename1.ui.Container findViewCategoryRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewCategoryRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewCategoryRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSearchNoResult(Component root) {
        return (com.codename1.ui.Label)findByName("lblSearchNoResult", root);
    }

    public com.codename1.ui.Label findLblSearchNoResult() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSearchNoResult", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSearchNoResult", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlayerTotalTime(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlayerTotalTime", root);
    }

    public com.codename1.ui.Label findLblPlayerTotalTime() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlayerTotalTime", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlayerTotalTime", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTfdLoginUsername(Component root) {
        return (com.codename1.ui.TextField)findByName("tfdLoginUsername", root);
    }

    public com.codename1.ui.TextField findTfdLoginUsername() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("tfdLoginUsername", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("tfdLoginUsername", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSearchHistoryTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblSearchHistoryTitle", root);
    }

    public com.codename1.ui.Label findLblSearchHistoryTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSearchHistoryTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSearchHistoryTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnLoginOffline(Component root) {
        return (com.codename1.ui.Button)findByName("btnLoginOffline", root);
    }

    public com.codename1.ui.Button findBtnLoginOffline() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnLoginOffline", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnLoginOffline", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlAllLanguages(Component root) {
        return (com.codename1.ui.List)findByName("ctlAllLanguages", root);
    }

    public com.codename1.ui.List findCtlAllLanguages() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlAllLanguages", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlAllLanguages", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnSearchResult(Component root) {
        return (com.codename1.ui.Container)findByName("ctnSearchResult", root);
    }

    public com.codename1.ui.Container findCtnSearchResult() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnSearchResult", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnSearchResult", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel(Component root) {
        return (com.codename1.ui.Label)findByName("Label", root);
    }

    public com.codename1.ui.Label findLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchHistoryOptionFixed(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchHistoryOptionFixed", root);
    }

    public com.codename1.ui.Button findBtnSearchHistoryOptionFixed() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchHistoryOptionFixed", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchHistoryOptionFixed", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlaylistRowTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlaylistRowTitle", root);
    }

    public com.codename1.ui.Label findLblPlaylistRowTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlaylistRowTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlaylistRowTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerPlaylist(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerPlaylist", root);
    }

    public com.codename1.ui.Button findBtnPlayerPlaylist() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerPlaylist", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerPlaylist", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer4(Component root) {
        return (com.codename1.ui.Container)findByName("Container4", root);
    }

    public com.codename1.ui.Container findContainer4() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container4", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container4", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer3(Component root) {
        return (com.codename1.ui.Container)findByName("Container3", root);
    }

    public com.codename1.ui.Container findContainer3() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container3", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnOfflineAvailable(Component root) {
        return (com.codename1.ui.Container)findByName("ctnOfflineAvailable", root);
    }

    public com.codename1.ui.Container findCtnOfflineAvailable() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnOfflineAvailable", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnOfflineAvailable", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer2(Component root) {
        return (com.codename1.ui.Container)findByName("Container2", root);
    }

    public com.codename1.ui.Container findContainer2() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.CheckBox findChkPlaylistIsOfflineAvailable(Component root) {
        return (com.codename1.ui.CheckBox)findByName("chkPlaylistIsOfflineAvailable", root);
    }

    public com.codename1.ui.CheckBox findChkPlaylistIsOfflineAvailable() {
        com.codename1.ui.CheckBox cmp = (com.codename1.ui.CheckBox)findByName("chkPlaylistIsOfflineAvailable", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.CheckBox)findByName("chkPlaylistIsOfflineAvailable", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnTopbarEditing(Component root) {
        return (com.codename1.ui.Container)findByName("ctnTopbarEditing", root);
    }

    public com.codename1.ui.Container findCtnTopbarEditing() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnTopbarEditing", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnTopbarEditing", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPLayerCoverMask(Component root) {
        return (com.codename1.ui.Label)findByName("lblPLayerCoverMask", root);
    }

    public com.codename1.ui.Label findLblPLayerCoverMask() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPLayerCoverMask", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPLayerCoverMask", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer1(Component root) {
        return (com.codename1.ui.Container)findByName("Container1", root);
    }

    public com.codename1.ui.Container findContainer1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnEditingConfirm(Component root) {
        return (com.codename1.ui.Button)findByName("btnEditingConfirm", root);
    }

    public com.codename1.ui.Button findBtnEditingConfirm() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnEditingConfirm", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnEditingConfirm", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchSuggestionIcon(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchSuggestionIcon", root);
    }

    public com.codename1.ui.Button findBtnSearchSuggestionIcon() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchSuggestionIcon", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchSuggestionIcon", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlNewsList(Component root) {
        return (com.codename1.ui.List)findByName("ctlNewsList", root);
    }

    public com.codename1.ui.List findCtlNewsList() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlNewsList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlNewsList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlayerButtons(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlayerButtons", root);
    }

    public com.codename1.ui.Container findCtnPlayerButtons() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlayerButtons", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlayerButtons", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlOverviewPlaylistList(Component root) {
        return (com.codename1.ui.List)findByName("ctlOverviewPlaylistList", root);
    }

    public com.codename1.ui.List findCtlOverviewPlaylistList() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlOverviewPlaylistList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlOverviewPlaylistList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlaylistActionFixed(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlaylistActionFixed", root);
    }

    public com.codename1.ui.Button findBtnPlaylistActionFixed() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlaylistActionFixed", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlaylistActionFixed", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findLstSettings(Component root) {
        return (com.codename1.ui.List)findByName("lstSettings", root);
    }

    public com.codename1.ui.List findLstSettings() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("lstSettings", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("lstSettings", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnAbortSearch(Component root) {
        return (com.codename1.ui.Button)findByName("btnAbortSearch", root);
    }

    public com.codename1.ui.Button findBtnAbortSearch() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnAbortSearch", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnAbortSearch", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnOptionsRowIcon(Component root) {
        return (com.codename1.ui.Button)findByName("btnOptionsRowIcon", root);
    }

    public com.codename1.ui.Button findBtnOptionsRowIcon() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnOptionsRowIcon", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnOptionsRowIcon", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findSubTitle(Component root) {
        return (com.codename1.ui.Label)findByName("subTitle", root);
    }

    public com.codename1.ui.Label findSubTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("subTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("subTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton(Component root) {
        return (com.codename1.ui.Button)findByName("Button", root);
    }

    public com.codename1.ui.Button findButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel1(Component root) {
        return (com.codename1.ui.Label)findByName("Label1", root);
    }

    public com.codename1.ui.Label findLabel1() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSettingsRow(Component root) {
        return (com.codename1.ui.Label)findByName("lblSettingsRow", root);
    }

    public com.codename1.ui.Label findLblSettingsRow() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSettingsRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSettingsRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnAddPlaylistFormSave(Component root) {
        return (com.codename1.ui.Button)findByName("btnAddPlaylistFormSave", root);
    }

    public com.codename1.ui.Button findBtnAddPlaylistFormSave() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnAddPlaylistFormSave", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnAddPlaylistFormSave", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerRepeat(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerRepeat", root);
    }

    public com.codename1.ui.Button findBtnPlayerRepeat() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerRepeat", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerRepeat", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnTopbar(Component root) {
        return (com.codename1.ui.Container)findByName("ctnTopbar", root);
    }

    public com.codename1.ui.Container findCtnTopbar() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnTopbar", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnTopbar", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchHistoryIconFixed(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchHistoryIconFixed", root);
    }

    public com.codename1.ui.Button findBtnSearchHistoryIconFixed() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchHistoryIconFixed", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchHistoryIconFixed", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.CheckBox findCbxSettings(Component root) {
        return (com.codename1.ui.CheckBox)findByName("cbxSettings", root);
    }

    public com.codename1.ui.CheckBox findCbxSettings() {
        com.codename1.ui.CheckBox cmp = (com.codename1.ui.CheckBox)findByName("cbxSettings", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.CheckBox)findByName("cbxSettings", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlaylistRowItems(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlaylistRowItems", root);
    }

    public com.codename1.ui.Label findLblPlaylistRowItems() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlaylistRowItems", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlaylistRowItems", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlayerControls(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlayerControls", root);
    }

    public com.codename1.ui.Container findCtnPlayerControls() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlayerControls", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlayerControls", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlaylistRowText(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlaylistRowText", root);
    }

    public com.codename1.ui.Container findCtnPlaylistRowText() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlaylistRowText", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlaylistRowText", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblArtistTrackCount(Component root) {
        return (com.codename1.ui.Label)findByName("lblArtistTrackCount", root);
    }

    public com.codename1.ui.Label findLblArtistTrackCount() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblArtistTrackCount", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblArtistTrackCount", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findIconOpen(Component root) {
        return (com.codename1.ui.Button)findByName("iconOpen", root);
    }

    public com.codename1.ui.Button findIconOpen() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("iconOpen", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("iconOpen", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlSearchResult(Component root) {
        return (com.codename1.ui.List)findByName("ctlSearchResult", root);
    }

    public com.codename1.ui.List findCtlSearchResult() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlSearchResult", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlSearchResult", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlayerButtons1(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlayerButtons1", root);
    }

    public com.codename1.ui.Container findCtnPlayerButtons1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlayerButtons1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlayerButtons1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnContent(Component root) {
        return (com.codename1.ui.Container)findByName("ctnContent", root);
    }

    public com.codename1.ui.Container findCtnContent() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnContent", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnContent", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerShuffle(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerShuffle", root);
    }

    public com.codename1.ui.Button findBtnPlayerShuffle() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerShuffle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerShuffle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlayerPerformerName(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlayerPerformerName", root);
    }

    public com.codename1.ui.Label findLblPlayerPerformerName() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlayerPerformerName", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlayerPerformerName", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblLogin(Component root) {
        return (com.codename1.ui.Label)findByName("lblLogin", root);
    }

    public com.codename1.ui.Label findLblLogin() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblLogin", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblLogin", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchFilterSpeech(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchFilterSpeech", root);
    }

    public com.codename1.ui.Button findBtnSearchFilterSpeech() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchFilterSpeech", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchFilterSpeech", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnSearchEmptyResultMessage(Component root) {
        return (com.codename1.ui.Container)findByName("ctnSearchEmptyResultMessage", root);
    }

    public com.codename1.ui.Container findCtnSearchEmptyResultMessage() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnSearchEmptyResultMessage", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnSearchEmptyResultMessage", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnAddPlaylistForm(Component root) {
        return (com.codename1.ui.Container)findByName("ctnAddPlaylistForm", root);
    }

    public com.codename1.ui.Container findCtnAddPlaylistForm() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnAddPlaylistForm", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnAddPlaylistForm", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblOptionsRowText(Component root) {
        return (com.codename1.ui.Label)findByName("lblOptionsRowText", root);
    }

    public com.codename1.ui.Label findLblOptionsRowText() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblOptionsRowText", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblOptionsRowText", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLongLanguage(Component root) {
        return (com.codename1.ui.Label)findByName("longLanguage", root);
    }

    public com.codename1.ui.Label findLongLanguage() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("longLanguage", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("longLanguage", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Slider findSldPlaylistDownloadingProgress(Component root) {
        return (com.codename1.ui.Slider)findByName("sldPlaylistDownloadingProgress", root);
    }

    public com.codename1.ui.Slider findSldPlaylistDownloadingProgress() {
        com.codename1.ui.Slider cmp = (com.codename1.ui.Slider)findByName("sldPlaylistDownloadingProgress", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Slider)findByName("sldPlaylistDownloadingProgress", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewTrackRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewTrackRow", root);
    }

    public com.codename1.ui.Container findViewTrackRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewTrackRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewTrackRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.WebBrowser findWebBrowser(Component root) {
        return (com.codename1.components.WebBrowser)findByName("WebBrowser", root);
    }

    public com.codename1.components.WebBrowser findWebBrowser() {
        com.codename1.components.WebBrowser cmp = (com.codename1.components.WebBrowser)findByName("WebBrowser", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.WebBrowser)findByName("WebBrowser", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPLayerCover(Component root) {
        return (com.codename1.ui.Label)findByName("lblPLayerCover", root);
    }

    public com.codename1.ui.Label findLblPLayerCover() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPLayerCover", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPLayerCover", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblPlayerCurrentTime(Component root) {
        return (com.codename1.ui.Label)findByName("lblPlayerCurrentTime", root);
    }

    public com.codename1.ui.Label findLblPlayerCurrentTime() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblPlayerCurrentTime", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblPlayerCurrentTime", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSearchFilterSong(Component root) {
        return (com.codename1.ui.Button)findByName("btnSearchFilterSong", root);
    }

    public com.codename1.ui.Button findBtnSearchFilterSong() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSearchFilterSong", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSearchFilterSong", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findCtnPlayerText(Component root) {
        return (com.codename1.ui.Container)findByName("ctnPlayerText", root);
    }

    public com.codename1.ui.Container findCtnPlayerText() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ctnPlayerText", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ctnPlayerText", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.SpanLabel findSpanLabel(Component root) {
        return (com.codename1.components.SpanLabel)findByName("SpanLabel", root);
    }

    public com.codename1.components.SpanLabel findSpanLabel() {
        com.codename1.components.SpanLabel cmp = (com.codename1.components.SpanLabel)findByName("SpanLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.SpanLabel)findByName("SpanLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findEmbPlayerBottom(Component root) {
        return (com.codename1.ui.Container)findByName("EmbPlayerBottom", root);
    }

    public com.codename1.ui.Container findEmbPlayerBottom() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("EmbPlayerBottom", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("EmbPlayerBottom", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer(Component root) {
        return (com.codename1.ui.Container)findByName("Container", root);
    }

    public com.codename1.ui.Container findContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewLoadingRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewLoadingRow", root);
    }

    public com.codename1.ui.Container findViewLoadingRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewLoadingRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewLoadingRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPlayerPlayPause(Component root) {
        return (com.codename1.ui.Button)findByName("btnPlayerPlayPause", root);
    }

    public com.codename1.ui.Button findBtnPlayerPlayPause() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPlayerPlayPause", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPlayerPlayPause", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnSettingsLanguageIcon(Component root) {
        return (com.codename1.ui.Button)findByName("btnSettingsLanguageIcon", root);
    }

    public com.codename1.ui.Button findBtnSettingsLanguageIcon() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnSettingsLanguageIcon", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnSettingsLanguageIcon", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress(Component root) {
        return (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", root);
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress() {
        com.codename1.components.InfiniteProgress cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSecondaryTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblSecondaryTitle", root);
    }

    public com.codename1.ui.Label findLblSecondaryTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSecondaryTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSecondaryTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.List findCtlBrowseItems(Component root) {
        return (com.codename1.ui.List)findByName("ctlBrowseItems", root);
    }

    public com.codename1.ui.List findCtlBrowseItems() {
        com.codename1.ui.List cmp = (com.codename1.ui.List)findByName("ctlBrowseItems", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.List)findByName("ctlBrowseItems", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnMediaIconFixed(Component root) {
        return (com.codename1.ui.Button)findByName("btnMediaIconFixed", root);
    }

    public com.codename1.ui.Button findBtnMediaIconFixed() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnMediaIconFixed", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnMediaIconFixed", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTfdLoginPassword(Component root) {
        return (com.codename1.ui.TextField)findByName("tfdLoginPassword", root);
    }

    public com.codename1.ui.TextField findTfdLoginPassword() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("tfdLoginPassword", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("tfdLoginPassword", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findLblDisclaimerText(Component root) {
        return (com.codename1.ui.TextArea)findByName("lblDisclaimerText", root);
    }

    public com.codename1.ui.TextArea findLblDisclaimerText() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("lblDisclaimerText", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("lblDisclaimerText", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSongTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblSongTitle", root);
    }

    public com.codename1.ui.Label findLblSongTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSongTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSongTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewEmptyPlaylist(Component root) {
        return (com.codename1.ui.Container)findByName("ViewEmptyPlaylist", root);
    }

    public com.codename1.ui.Container findViewEmptyPlaylist() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewEmptyPlaylist", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewEmptyPlaylist", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Slider findSldPlayerTime(Component root) {
        return (com.codename1.ui.Slider)findByName("sldPlayerTime", root);
    }

    public com.codename1.ui.Slider findSldPlayerTime() {
        com.codename1.ui.Slider cmp = (com.codename1.ui.Slider)findByName("sldPlayerTime", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Slider)findByName("sldPlayerTime", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblArtistName(Component root) {
        return (com.codename1.ui.Label)findByName("lblArtistName", root);
    }

    public com.codename1.ui.Label findLblArtistName() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblArtistName", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblArtistName", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSearchEmptyResultKeyword(Component root) {
        return (com.codename1.ui.Label)findByName("lblSearchEmptyResultKeyword", root);
    }

    public com.codename1.ui.Label findLblSearchEmptyResultKeyword() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSearchEmptyResultKeyword", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSearchEmptyResultKeyword", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblSearchEmptyResultSuggestionTitle(Component root) {
        return (com.codename1.ui.Label)findByName("lblSearchEmptyResultSuggestionTitle", root);
    }

    public com.codename1.ui.Label findLblSearchEmptyResultSuggestionTitle() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblSearchEmptyResultSuggestionTitle", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblSearchEmptyResultSuggestionTitle", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.ContainerList findCtlArtistAllTracks(Component root) {
        return (com.codename1.ui.list.ContainerList)findByName("ctlArtistAllTracks", root);
    }

    public com.codename1.ui.list.ContainerList findCtlArtistAllTracks() {
        com.codename1.ui.list.ContainerList cmp = (com.codename1.ui.list.ContainerList)findByName("ctlArtistAllTracks", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.ContainerList)findByName("ctlArtistAllTracks", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.ContainerList findCtlArtistAppearsIn(Component root) {
        return (com.codename1.ui.list.ContainerList)findByName("ctlArtistAppearsIn", root);
    }

    public com.codename1.ui.list.ContainerList findCtlArtistAppearsIn() {
        com.codename1.ui.list.ContainerList cmp = (com.codename1.ui.list.ContainerList)findByName("ctlArtistAppearsIn", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.ContainerList)findByName("ctlArtistAppearsIn", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLblChosen(Component root) {
        return (com.codename1.ui.Label)findByName("lblChosen", root);
    }

    public com.codename1.ui.Label findLblChosen() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("lblChosen", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("lblChosen", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewSearchHistoryRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewSearchHistoryRow", root);
    }

    public com.codename1.ui.Container findViewSearchHistoryRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewSearchHistoryRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewSearchHistoryRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findViewLanguageRow(Component root) {
        return (com.codename1.ui.Container)findByName("ViewLanguageRow", root);
    }

    public com.codename1.ui.Container findViewLanguageRow() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("ViewLanguageRow", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("ViewLanguageRow", aboutToShowThisContainer);
        }
        return cmp;
    }

    protected void exitForm(Form f) {
        if("ViewOptionsRow".equals(f.getName())) {
            exitViewOptionsRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(f.getName())) {
            exitScreenNews(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(f.getName())) {
            exitViewCheckBoxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(f.getName())) {
            exitScreenPlaylistOverview(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(f.getName())) {
            exitScreenPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(f.getName())) {
            exitScreenLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(f.getName())) {
            exitScreenCreateAccount(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(f.getName())) {
            exitViewPlaylistRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(f.getName())) {
            exitViewSuggestionRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(f.getName())) {
            exitScreenBrowse(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(f.getName())) {
            exitScreenLanguage(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(f.getName())) {
            exitViewSettingsCheckboxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(f.getName())) {
            exitScreenSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(f.getName())) {
            exitViewCategoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(f.getName())) {
            exitScreenDisclaimer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(f.getName())) {
            exitScreenQueue(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(f.getName())) {
            exitScreenSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(f.getName())) {
            exitScreenPerformer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(f.getName())) {
            exitViewTrackRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(f.getName())) {
            exitScreenAlbum(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(f.getName())) {
            exitScreenForgotPassword(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(f.getName())) {
            exitEmbPlayerBottom(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(f.getName())) {
            exitScreenPlayer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(f.getName())) {
            exitViewLoadingRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(f.getName())) {
            exitViewEmptyPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(f.getName())) {
            exitViewSearchHistoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(f.getName())) {
            exitViewLanguageRow(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void exitViewOptionsRow(Form f) {
    }


    protected void exitScreenNews(Form f) {
    }


    protected void exitViewCheckBoxRow(Form f) {
    }


    protected void exitScreenPlaylistOverview(Form f) {
    }


    protected void exitScreenPlaylist(Form f) {
    }


    protected void exitScreenLogin(Form f) {
    }


    protected void exitScreenCreateAccount(Form f) {
    }


    protected void exitViewPlaylistRow(Form f) {
    }


    protected void exitViewSuggestionRow(Form f) {
    }


    protected void exitScreenBrowse(Form f) {
    }


    protected void exitScreenLanguage(Form f) {
    }


    protected void exitViewSettingsCheckboxRow(Form f) {
    }


    protected void exitScreenSearch(Form f) {
    }


    protected void exitViewCategoryRow(Form f) {
    }


    protected void exitScreenDisclaimer(Form f) {
    }


    protected void exitScreenQueue(Form f) {
    }


    protected void exitScreenSettings(Form f) {
    }


    protected void exitScreenPerformer(Form f) {
    }


    protected void exitViewTrackRow(Form f) {
    }


    protected void exitScreenAlbum(Form f) {
    }


    protected void exitScreenForgotPassword(Form f) {
    }


    protected void exitEmbPlayerBottom(Form f) {
    }


    protected void exitScreenPlayer(Form f) {
    }


    protected void exitViewLoadingRow(Form f) {
    }


    protected void exitViewEmptyPlaylist(Form f) {
    }


    protected void exitViewSearchHistoryRow(Form f) {
    }


    protected void exitViewLanguageRow(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("ViewOptionsRow".equals(f.getName())) {
            beforeViewOptionsRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(f.getName())) {
            beforeScreenNews(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(f.getName())) {
            beforeViewCheckBoxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(f.getName())) {
            beforeScreenPlaylistOverview(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(f.getName())) {
            beforeScreenPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(f.getName())) {
            beforeScreenLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(f.getName())) {
            beforeScreenCreateAccount(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(f.getName())) {
            beforeViewPlaylistRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(f.getName())) {
            beforeViewSuggestionRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(f.getName())) {
            beforeScreenBrowse(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(f.getName())) {
            beforeScreenLanguage(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(f.getName())) {
            beforeViewSettingsCheckboxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(f.getName())) {
            beforeScreenSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(f.getName())) {
            beforeViewCategoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(f.getName())) {
            beforeScreenDisclaimer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(f.getName())) {
            beforeScreenQueue(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(f.getName())) {
            beforeScreenSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(f.getName())) {
            beforeScreenPerformer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(f.getName())) {
            beforeViewTrackRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(f.getName())) {
            beforeScreenAlbum(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(f.getName())) {
            beforeScreenForgotPassword(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(f.getName())) {
            beforeEmbPlayerBottom(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(f.getName())) {
            beforeScreenPlayer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(f.getName())) {
            beforeViewLoadingRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(f.getName())) {
            beforeViewEmptyPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(f.getName())) {
            beforeViewSearchHistoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(f.getName())) {
            beforeViewLanguageRow(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeViewOptionsRow(Form f) {
    }


    protected void beforeScreenNews(Form f) {
    }


    protected void beforeViewCheckBoxRow(Form f) {
    }


    protected void beforeScreenPlaylistOverview(Form f) {
    }


    protected void beforeScreenPlaylist(Form f) {
    }


    protected void beforeScreenLogin(Form f) {
    }


    protected void beforeScreenCreateAccount(Form f) {
    }


    protected void beforeViewPlaylistRow(Form f) {
    }


    protected void beforeViewSuggestionRow(Form f) {
    }


    protected void beforeScreenBrowse(Form f) {
    }


    protected void beforeScreenLanguage(Form f) {
    }


    protected void beforeViewSettingsCheckboxRow(Form f) {
    }


    protected void beforeScreenSearch(Form f) {
    }


    protected void beforeViewCategoryRow(Form f) {
    }


    protected void beforeScreenDisclaimer(Form f) {
    }


    protected void beforeScreenQueue(Form f) {
    }


    protected void beforeScreenSettings(Form f) {
    }


    protected void beforeScreenPerformer(Form f) {
    }


    protected void beforeViewTrackRow(Form f) {
    }


    protected void beforeScreenAlbum(Form f) {
    }


    protected void beforeScreenForgotPassword(Form f) {
    }


    protected void beforeEmbPlayerBottom(Form f) {
    }


    protected void beforeScreenPlayer(Form f) {
    }


    protected void beforeViewLoadingRow(Form f) {
    }


    protected void beforeViewEmptyPlaylist(Form f) {
    }


    protected void beforeViewSearchHistoryRow(Form f) {
    }


    protected void beforeViewLanguageRow(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if("ViewOptionsRow".equals(c.getName())) {
            beforeContainerViewOptionsRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(c.getName())) {
            beforeContainerScreenNews(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(c.getName())) {
            beforeContainerViewCheckBoxRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(c.getName())) {
            beforeContainerScreenPlaylistOverview(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(c.getName())) {
            beforeContainerScreenPlaylist(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(c.getName())) {
            beforeContainerScreenLogin(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(c.getName())) {
            beforeContainerScreenCreateAccount(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(c.getName())) {
            beforeContainerViewPlaylistRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(c.getName())) {
            beforeContainerViewSuggestionRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(c.getName())) {
            beforeContainerScreenBrowse(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(c.getName())) {
            beforeContainerScreenLanguage(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(c.getName())) {
            beforeContainerViewSettingsCheckboxRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(c.getName())) {
            beforeContainerScreenSearch(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(c.getName())) {
            beforeContainerViewCategoryRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(c.getName())) {
            beforeContainerScreenDisclaimer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(c.getName())) {
            beforeContainerScreenQueue(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(c.getName())) {
            beforeContainerScreenSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(c.getName())) {
            beforeContainerScreenPerformer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(c.getName())) {
            beforeContainerViewTrackRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(c.getName())) {
            beforeContainerScreenAlbum(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(c.getName())) {
            beforeContainerScreenForgotPassword(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(c.getName())) {
            beforeContainerEmbPlayerBottom(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(c.getName())) {
            beforeContainerScreenPlayer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(c.getName())) {
            beforeContainerViewLoadingRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(c.getName())) {
            beforeContainerViewEmptyPlaylist(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(c.getName())) {
            beforeContainerViewSearchHistoryRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(c.getName())) {
            beforeContainerViewLanguageRow(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeContainerViewOptionsRow(Container c) {
    }


    protected void beforeContainerScreenNews(Container c) {
    }


    protected void beforeContainerViewCheckBoxRow(Container c) {
    }


    protected void beforeContainerScreenPlaylistOverview(Container c) {
    }


    protected void beforeContainerScreenPlaylist(Container c) {
    }


    protected void beforeContainerScreenLogin(Container c) {
    }


    protected void beforeContainerScreenCreateAccount(Container c) {
    }


    protected void beforeContainerViewPlaylistRow(Container c) {
    }


    protected void beforeContainerViewSuggestionRow(Container c) {
    }


    protected void beforeContainerScreenBrowse(Container c) {
    }


    protected void beforeContainerScreenLanguage(Container c) {
    }


    protected void beforeContainerViewSettingsCheckboxRow(Container c) {
    }


    protected void beforeContainerScreenSearch(Container c) {
    }


    protected void beforeContainerViewCategoryRow(Container c) {
    }


    protected void beforeContainerScreenDisclaimer(Container c) {
    }


    protected void beforeContainerScreenQueue(Container c) {
    }


    protected void beforeContainerScreenSettings(Container c) {
    }


    protected void beforeContainerScreenPerformer(Container c) {
    }


    protected void beforeContainerViewTrackRow(Container c) {
    }


    protected void beforeContainerScreenAlbum(Container c) {
    }


    protected void beforeContainerScreenForgotPassword(Container c) {
    }


    protected void beforeContainerEmbPlayerBottom(Container c) {
    }


    protected void beforeContainerScreenPlayer(Container c) {
    }


    protected void beforeContainerViewLoadingRow(Container c) {
    }


    protected void beforeContainerViewEmptyPlaylist(Container c) {
    }


    protected void beforeContainerViewSearchHistoryRow(Container c) {
    }


    protected void beforeContainerViewLanguageRow(Container c) {
    }

    protected void postShow(Form f) {
        if("ViewOptionsRow".equals(f.getName())) {
            postViewOptionsRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(f.getName())) {
            postScreenNews(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(f.getName())) {
            postViewCheckBoxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(f.getName())) {
            postScreenPlaylistOverview(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(f.getName())) {
            postScreenPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(f.getName())) {
            postScreenLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(f.getName())) {
            postScreenCreateAccount(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(f.getName())) {
            postViewPlaylistRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(f.getName())) {
            postViewSuggestionRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(f.getName())) {
            postScreenBrowse(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(f.getName())) {
            postScreenLanguage(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(f.getName())) {
            postViewSettingsCheckboxRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(f.getName())) {
            postScreenSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(f.getName())) {
            postViewCategoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(f.getName())) {
            postScreenDisclaimer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(f.getName())) {
            postScreenQueue(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(f.getName())) {
            postScreenSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(f.getName())) {
            postScreenPerformer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(f.getName())) {
            postViewTrackRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(f.getName())) {
            postScreenAlbum(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(f.getName())) {
            postScreenForgotPassword(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(f.getName())) {
            postEmbPlayerBottom(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(f.getName())) {
            postScreenPlayer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(f.getName())) {
            postViewLoadingRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(f.getName())) {
            postViewEmptyPlaylist(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(f.getName())) {
            postViewSearchHistoryRow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(f.getName())) {
            postViewLanguageRow(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postViewOptionsRow(Form f) {
    }


    protected void postScreenNews(Form f) {
    }


    protected void postViewCheckBoxRow(Form f) {
    }


    protected void postScreenPlaylistOverview(Form f) {
    }


    protected void postScreenPlaylist(Form f) {
    }


    protected void postScreenLogin(Form f) {
    }


    protected void postScreenCreateAccount(Form f) {
    }


    protected void postViewPlaylistRow(Form f) {
    }


    protected void postViewSuggestionRow(Form f) {
    }


    protected void postScreenBrowse(Form f) {
    }


    protected void postScreenLanguage(Form f) {
    }


    protected void postViewSettingsCheckboxRow(Form f) {
    }


    protected void postScreenSearch(Form f) {
    }


    protected void postViewCategoryRow(Form f) {
    }


    protected void postScreenDisclaimer(Form f) {
    }


    protected void postScreenQueue(Form f) {
    }


    protected void postScreenSettings(Form f) {
    }


    protected void postScreenPerformer(Form f) {
    }


    protected void postViewTrackRow(Form f) {
    }


    protected void postScreenAlbum(Form f) {
    }


    protected void postScreenForgotPassword(Form f) {
    }


    protected void postEmbPlayerBottom(Form f) {
    }


    protected void postScreenPlayer(Form f) {
    }


    protected void postViewLoadingRow(Form f) {
    }


    protected void postViewEmptyPlaylist(Form f) {
    }


    protected void postViewSearchHistoryRow(Form f) {
    }


    protected void postViewLanguageRow(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("ViewOptionsRow".equals(c.getName())) {
            postContainerViewOptionsRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(c.getName())) {
            postContainerScreenNews(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(c.getName())) {
            postContainerViewCheckBoxRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(c.getName())) {
            postContainerScreenPlaylistOverview(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(c.getName())) {
            postContainerScreenPlaylist(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(c.getName())) {
            postContainerScreenLogin(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(c.getName())) {
            postContainerScreenCreateAccount(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(c.getName())) {
            postContainerViewPlaylistRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(c.getName())) {
            postContainerViewSuggestionRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(c.getName())) {
            postContainerScreenBrowse(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(c.getName())) {
            postContainerScreenLanguage(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(c.getName())) {
            postContainerViewSettingsCheckboxRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(c.getName())) {
            postContainerScreenSearch(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(c.getName())) {
            postContainerViewCategoryRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(c.getName())) {
            postContainerScreenDisclaimer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(c.getName())) {
            postContainerScreenQueue(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(c.getName())) {
            postContainerScreenSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(c.getName())) {
            postContainerScreenPerformer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(c.getName())) {
            postContainerViewTrackRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(c.getName())) {
            postContainerScreenAlbum(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(c.getName())) {
            postContainerScreenForgotPassword(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(c.getName())) {
            postContainerEmbPlayerBottom(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(c.getName())) {
            postContainerScreenPlayer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(c.getName())) {
            postContainerViewLoadingRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(c.getName())) {
            postContainerViewEmptyPlaylist(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(c.getName())) {
            postContainerViewSearchHistoryRow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(c.getName())) {
            postContainerViewLanguageRow(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postContainerViewOptionsRow(Container c) {
    }


    protected void postContainerScreenNews(Container c) {
    }


    protected void postContainerViewCheckBoxRow(Container c) {
    }


    protected void postContainerScreenPlaylistOverview(Container c) {
    }


    protected void postContainerScreenPlaylist(Container c) {
    }


    protected void postContainerScreenLogin(Container c) {
    }


    protected void postContainerScreenCreateAccount(Container c) {
    }


    protected void postContainerViewPlaylistRow(Container c) {
    }


    protected void postContainerViewSuggestionRow(Container c) {
    }


    protected void postContainerScreenBrowse(Container c) {
    }


    protected void postContainerScreenLanguage(Container c) {
    }


    protected void postContainerViewSettingsCheckboxRow(Container c) {
    }


    protected void postContainerScreenSearch(Container c) {
    }


    protected void postContainerViewCategoryRow(Container c) {
    }


    protected void postContainerScreenDisclaimer(Container c) {
    }


    protected void postContainerScreenQueue(Container c) {
    }


    protected void postContainerScreenSettings(Container c) {
    }


    protected void postContainerScreenPerformer(Container c) {
    }


    protected void postContainerViewTrackRow(Container c) {
    }


    protected void postContainerScreenAlbum(Container c) {
    }


    protected void postContainerScreenForgotPassword(Container c) {
    }


    protected void postContainerEmbPlayerBottom(Container c) {
    }


    protected void postContainerScreenPlayer(Container c) {
    }


    protected void postContainerViewLoadingRow(Container c) {
    }


    protected void postContainerViewEmptyPlaylist(Container c) {
    }


    protected void postContainerViewSearchHistoryRow(Container c) {
    }


    protected void postContainerViewLanguageRow(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("ViewOptionsRow".equals(rootName)) {
            onCreateViewOptionsRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(rootName)) {
            onCreateScreenNews();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(rootName)) {
            onCreateViewCheckBoxRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(rootName)) {
            onCreateScreenPlaylistOverview();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(rootName)) {
            onCreateScreenPlaylist();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(rootName)) {
            onCreateScreenLogin();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(rootName)) {
            onCreateScreenCreateAccount();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(rootName)) {
            onCreateViewPlaylistRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(rootName)) {
            onCreateViewSuggestionRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(rootName)) {
            onCreateScreenBrowse();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(rootName)) {
            onCreateScreenLanguage();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(rootName)) {
            onCreateViewSettingsCheckboxRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(rootName)) {
            onCreateScreenSearch();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(rootName)) {
            onCreateViewCategoryRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(rootName)) {
            onCreateScreenDisclaimer();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(rootName)) {
            onCreateScreenQueue();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(rootName)) {
            onCreateScreenSettings();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(rootName)) {
            onCreateScreenPerformer();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(rootName)) {
            onCreateViewTrackRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(rootName)) {
            onCreateScreenAlbum();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(rootName)) {
            onCreateScreenForgotPassword();
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(rootName)) {
            onCreateEmbPlayerBottom();
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(rootName)) {
            onCreateScreenPlayer();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(rootName)) {
            onCreateViewLoadingRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(rootName)) {
            onCreateViewEmptyPlaylist();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(rootName)) {
            onCreateViewSearchHistoryRow();
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(rootName)) {
            onCreateViewLanguageRow();
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void onCreateViewOptionsRow() {
    }


    protected void onCreateScreenNews() {
    }


    protected void onCreateViewCheckBoxRow() {
    }


    protected void onCreateScreenPlaylistOverview() {
    }


    protected void onCreateScreenPlaylist() {
    }


    protected void onCreateScreenLogin() {
    }


    protected void onCreateScreenCreateAccount() {
    }


    protected void onCreateViewPlaylistRow() {
    }


    protected void onCreateViewSuggestionRow() {
    }


    protected void onCreateScreenBrowse() {
    }


    protected void onCreateScreenLanguage() {
    }


    protected void onCreateViewSettingsCheckboxRow() {
    }


    protected void onCreateScreenSearch() {
    }


    protected void onCreateViewCategoryRow() {
    }


    protected void onCreateScreenDisclaimer() {
    }


    protected void onCreateScreenQueue() {
    }


    protected void onCreateScreenSettings() {
    }


    protected void onCreateScreenPerformer() {
    }


    protected void onCreateViewTrackRow() {
    }


    protected void onCreateScreenAlbum() {
    }


    protected void onCreateScreenForgotPassword() {
    }


    protected void onCreateEmbPlayerBottom() {
    }


    protected void onCreateScreenPlayer() {
    }


    protected void onCreateViewLoadingRow() {
    }


    protected void onCreateViewEmptyPlaylist() {
    }


    protected void onCreateViewSearchHistoryRow() {
    }


    protected void onCreateViewLanguageRow() {
    }

    protected Hashtable getFormState(Form f) {
        Hashtable h = super.getFormState(f);
        if("ViewOptionsRow".equals(f.getName())) {
            getStateViewOptionsRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenNews".equals(f.getName())) {
            getStateScreenNews(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewCheckBoxRow".equals(f.getName())) {
            getStateViewCheckBoxRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenPlaylistOverview".equals(f.getName())) {
            getStateScreenPlaylistOverview(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenPlaylist".equals(f.getName())) {
            getStateScreenPlaylist(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenLogin".equals(f.getName())) {
            getStateScreenLogin(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenCreateAccount".equals(f.getName())) {
            getStateScreenCreateAccount(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewPlaylistRow".equals(f.getName())) {
            getStateViewPlaylistRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewSuggestionRow".equals(f.getName())) {
            getStateViewSuggestionRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenBrowse".equals(f.getName())) {
            getStateScreenBrowse(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenLanguage".equals(f.getName())) {
            getStateScreenLanguage(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewSettingsCheckboxRow".equals(f.getName())) {
            getStateViewSettingsCheckboxRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenSearch".equals(f.getName())) {
            getStateScreenSearch(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewCategoryRow".equals(f.getName())) {
            getStateViewCategoryRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenDisclaimer".equals(f.getName())) {
            getStateScreenDisclaimer(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenQueue".equals(f.getName())) {
            getStateScreenQueue(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenSettings".equals(f.getName())) {
            getStateScreenSettings(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenPerformer".equals(f.getName())) {
            getStateScreenPerformer(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewTrackRow".equals(f.getName())) {
            getStateViewTrackRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenAlbum".equals(f.getName())) {
            getStateScreenAlbum(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenForgotPassword".equals(f.getName())) {
            getStateScreenForgotPassword(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("EmbPlayerBottom".equals(f.getName())) {
            getStateEmbPlayerBottom(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ScreenPlayer".equals(f.getName())) {
            getStateScreenPlayer(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewLoadingRow".equals(f.getName())) {
            getStateViewLoadingRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewEmptyPlaylist".equals(f.getName())) {
            getStateViewEmptyPlaylist(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewSearchHistoryRow".equals(f.getName())) {
            getStateViewSearchHistoryRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ViewLanguageRow".equals(f.getName())) {
            getStateViewLanguageRow(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

            return h;
    }


    protected void getStateViewOptionsRow(Form f, Hashtable h) {
    }


    protected void getStateScreenNews(Form f, Hashtable h) {
    }


    protected void getStateViewCheckBoxRow(Form f, Hashtable h) {
    }


    protected void getStateScreenPlaylistOverview(Form f, Hashtable h) {
    }


    protected void getStateScreenPlaylist(Form f, Hashtable h) {
    }


    protected void getStateScreenLogin(Form f, Hashtable h) {
    }


    protected void getStateScreenCreateAccount(Form f, Hashtable h) {
    }


    protected void getStateViewPlaylistRow(Form f, Hashtable h) {
    }


    protected void getStateViewSuggestionRow(Form f, Hashtable h) {
    }


    protected void getStateScreenBrowse(Form f, Hashtable h) {
    }


    protected void getStateScreenLanguage(Form f, Hashtable h) {
    }


    protected void getStateViewSettingsCheckboxRow(Form f, Hashtable h) {
    }


    protected void getStateScreenSearch(Form f, Hashtable h) {
    }


    protected void getStateViewCategoryRow(Form f, Hashtable h) {
    }


    protected void getStateScreenDisclaimer(Form f, Hashtable h) {
    }


    protected void getStateScreenQueue(Form f, Hashtable h) {
    }


    protected void getStateScreenSettings(Form f, Hashtable h) {
    }


    protected void getStateScreenPerformer(Form f, Hashtable h) {
    }


    protected void getStateViewTrackRow(Form f, Hashtable h) {
    }


    protected void getStateScreenAlbum(Form f, Hashtable h) {
    }


    protected void getStateScreenForgotPassword(Form f, Hashtable h) {
    }


    protected void getStateEmbPlayerBottom(Form f, Hashtable h) {
    }


    protected void getStateScreenPlayer(Form f, Hashtable h) {
    }


    protected void getStateViewLoadingRow(Form f, Hashtable h) {
    }


    protected void getStateViewEmptyPlaylist(Form f, Hashtable h) {
    }


    protected void getStateViewSearchHistoryRow(Form f, Hashtable h) {
    }


    protected void getStateViewLanguageRow(Form f, Hashtable h) {
    }

    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
        if("ViewOptionsRow".equals(f.getName())) {
            setStateViewOptionsRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenNews".equals(f.getName())) {
            setStateScreenNews(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCheckBoxRow".equals(f.getName())) {
            setStateViewCheckBoxRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylistOverview".equals(f.getName())) {
            setStateScreenPlaylistOverview(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlaylist".equals(f.getName())) {
            setStateScreenPlaylist(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLogin".equals(f.getName())) {
            setStateScreenLogin(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenCreateAccount".equals(f.getName())) {
            setStateScreenCreateAccount(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewPlaylistRow".equals(f.getName())) {
            setStateViewPlaylistRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSuggestionRow".equals(f.getName())) {
            setStateViewSuggestionRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenBrowse".equals(f.getName())) {
            setStateScreenBrowse(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenLanguage".equals(f.getName())) {
            setStateScreenLanguage(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSettingsCheckboxRow".equals(f.getName())) {
            setStateViewSettingsCheckboxRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSearch".equals(f.getName())) {
            setStateScreenSearch(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewCategoryRow".equals(f.getName())) {
            setStateViewCategoryRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenDisclaimer".equals(f.getName())) {
            setStateScreenDisclaimer(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenQueue".equals(f.getName())) {
            setStateScreenQueue(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenSettings".equals(f.getName())) {
            setStateScreenSettings(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPerformer".equals(f.getName())) {
            setStateScreenPerformer(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewTrackRow".equals(f.getName())) {
            setStateViewTrackRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenAlbum".equals(f.getName())) {
            setStateScreenAlbum(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenForgotPassword".equals(f.getName())) {
            setStateScreenForgotPassword(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("EmbPlayerBottom".equals(f.getName())) {
            setStateEmbPlayerBottom(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ScreenPlayer".equals(f.getName())) {
            setStateScreenPlayer(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLoadingRow".equals(f.getName())) {
            setStateViewLoadingRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewEmptyPlaylist".equals(f.getName())) {
            setStateViewEmptyPlaylist(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewSearchHistoryRow".equals(f.getName())) {
            setStateViewSearchHistoryRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ViewLanguageRow".equals(f.getName())) {
            setStateViewLanguageRow(f, state);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void setStateViewOptionsRow(Form f, Hashtable state) {
    }


    protected void setStateScreenNews(Form f, Hashtable state) {
    }


    protected void setStateViewCheckBoxRow(Form f, Hashtable state) {
    }


    protected void setStateScreenPlaylistOverview(Form f, Hashtable state) {
    }


    protected void setStateScreenPlaylist(Form f, Hashtable state) {
    }


    protected void setStateScreenLogin(Form f, Hashtable state) {
    }


    protected void setStateScreenCreateAccount(Form f, Hashtable state) {
    }


    protected void setStateViewPlaylistRow(Form f, Hashtable state) {
    }


    protected void setStateViewSuggestionRow(Form f, Hashtable state) {
    }


    protected void setStateScreenBrowse(Form f, Hashtable state) {
    }


    protected void setStateScreenLanguage(Form f, Hashtable state) {
    }


    protected void setStateViewSettingsCheckboxRow(Form f, Hashtable state) {
    }


    protected void setStateScreenSearch(Form f, Hashtable state) {
    }


    protected void setStateViewCategoryRow(Form f, Hashtable state) {
    }


    protected void setStateScreenDisclaimer(Form f, Hashtable state) {
    }


    protected void setStateScreenQueue(Form f, Hashtable state) {
    }


    protected void setStateScreenSettings(Form f, Hashtable state) {
    }


    protected void setStateScreenPerformer(Form f, Hashtable state) {
    }


    protected void setStateViewTrackRow(Form f, Hashtable state) {
    }


    protected void setStateScreenAlbum(Form f, Hashtable state) {
    }


    protected void setStateScreenForgotPassword(Form f, Hashtable state) {
    }


    protected void setStateEmbPlayerBottom(Form f, Hashtable state) {
    }


    protected void setStateScreenPlayer(Form f, Hashtable state) {
    }


    protected void setStateViewLoadingRow(Form f, Hashtable state) {
    }


    protected void setStateViewEmptyPlaylist(Form f, Hashtable state) {
    }


    protected void setStateViewSearchHistoryRow(Form f, Hashtable state) {
    }


    protected void setStateViewLanguageRow(Form f, Hashtable state) {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("ctlPlaylistMediaItems".equals(listName)) {
            return initListModelCtlPlaylistMediaItems(cmp);
        }
        if("ctlPlayerQueue".equals(listName)) {
            return initListModelCtlPlayerQueue(cmp);
        }
        if("ctlAllLanguages".equals(listName)) {
            return initListModelCtlAllLanguages(cmp);
        }
        if("ctlNewsList".equals(listName)) {
            return initListModelCtlNewsList(cmp);
        }
        if("ctlOverviewPlaylistList".equals(listName)) {
            return initListModelCtlOverviewPlaylistList(cmp);
        }
        if("lstSettings".equals(listName)) {
            return initListModelLstSettings(cmp);
        }
        if("ctlSearchResult".equals(listName)) {
            return initListModelCtlSearchResult(cmp);
        }
        if("ctlBrowseItems".equals(listName)) {
            return initListModelCtlBrowseItems(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelCtlPlaylistMediaItems(List cmp) {
        return false;
    }

    protected boolean initListModelCtlPlayerQueue(List cmp) {
        return false;
    }

    protected boolean initListModelCtlAllLanguages(List cmp) {
        return false;
    }

    protected boolean initListModelCtlNewsList(List cmp) {
        return false;
    }

    protected boolean initListModelCtlOverviewPlaylistList(List cmp) {
        return false;
    }

    protected boolean initListModelLstSettings(List cmp) {
        return false;
    }

    protected boolean initListModelCtlSearchResult(List cmp) {
        return false;
    }

    protected boolean initListModelCtlBrowseItems(List cmp) {
        return false;
    }

    protected boolean setListModel(com.codename1.ui.list.ContainerList cmp) {
        String listName = cmp.getName();
        if("ctlArtistAllTracks".equals(listName)) {
            return initListModelCtlArtistAllTracks(cmp);
        }
        if("ctlArtistAppearsIn".equals(listName)) {
            return initListModelCtlArtistAppearsIn(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelCtlArtistAllTracks(com.codename1.ui.list.ContainerList cmp) {
        return false;
    }

    protected boolean initListModelCtlArtistAppearsIn(com.codename1.ui.list.ContainerList cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        Container leadParentContainer = c.getParent().getLeadParent();
        if(leadParentContainer != null && leadParentContainer.getClass() != Container.class) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("ViewOptionsRow")) {
            if("btnOptionsRowIcon".equals(c.getName())) {
                onViewOptionsRow_BtnOptionsRowIconAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenNews")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenNews_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenNews_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlNewsList".equals(c.getName())) {
                onScreenNews_CtlNewsListAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewCheckBoxRow")) {
            if("cbxSettings".equals(c.getName())) {
                onViewCheckBoxRow_CbxSettingsAction(c, event);
                return;
            }
            if("btnSettingsLanguageIcon".equals(c.getName())) {
                onViewCheckBoxRow_BtnSettingsLanguageIconAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenPlaylistOverview")) {
            if("tfdAddPlaylistFormTitle".equals(c.getName())) {
                onScreenPlaylistOverview_TfdAddPlaylistFormTitleAction(c, event);
                return;
            }
            if("btnAddPlaylistFormSave".equals(c.getName())) {
                onScreenPlaylistOverview_BtnAddPlaylistFormSaveAction(c, event);
                return;
            }
            if("btnPlaylistActionFixed".equals(c.getName())) {
                onScreenPlaylistOverview_BtnPlaylistActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenPlaylistOverview_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlOverviewPlaylistList".equals(c.getName())) {
                onScreenPlaylistOverview_CtlOverviewPlaylistListAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenPlaylist")) {
            if("btnEditingConfirm".equals(c.getName())) {
                onScreenPlaylist_BtnEditingConfirmAction(c, event);
                return;
            }
            if("txtEditing".equals(c.getName())) {
                onScreenPlaylist_TxtEditingAction(c, event);
                return;
            }
            if("chkPlaylistIsOfflineAvailable".equals(c.getName())) {
                onScreenPlaylist_ChkPlaylistIsOfflineAvailableAction(c, event);
                return;
            }
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenPlaylist_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenPlaylist_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlPlaylistMediaItems".equals(c.getName())) {
                onScreenPlaylist_CtlPlaylistMediaItemsAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenLogin")) {
            if("tfdLoginUsername".equals(c.getName())) {
                onScreenLogin_TfdLoginUsernameAction(c, event);
                return;
            }
            if("tfdLoginPassword".equals(c.getName())) {
                onScreenLogin_TfdLoginPasswordAction(c, event);
                return;
            }
            if("btnLoginLogin".equals(c.getName())) {
                onScreenLogin_BtnLoginLoginAction(c, event);
                return;
            }
            if("btnLoginOffline".equals(c.getName())) {
                onScreenLogin_BtnLoginOfflineAction(c, event);
                return;
            }
            if("btnLoginCreateAccount".equals(c.getName())) {
                onScreenLogin_BtnLoginCreateAccountAction(c, event);
                return;
            }
            if("btnLoginForgotPassword".equals(c.getName())) {
                onScreenLogin_BtnLoginForgotPasswordAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewPlaylistRow")) {
            if("btnPlaylistActionFixed".equals(c.getName())) {
                onViewPlaylistRow_BtnPlaylistActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onViewPlaylistRow_BtnMediaIconAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewSuggestionRow")) {
            if("btnSearchSuggestionIcon".equals(c.getName())) {
                onViewSuggestionRow_BtnSearchSuggestionIconAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenBrowse")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenBrowse_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenBrowse_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlBrowseItems".equals(c.getName())) {
                onScreenBrowse_CtlBrowseItemsAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenLanguage")) {
            if("ctlAllLanguages".equals(c.getName())) {
                onScreenLanguage_CtlAllLanguagesAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewSettingsCheckboxRow")) {
            if("iconOpen".equals(c.getName())) {
                onViewSettingsCheckboxRow_IconOpenAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenSearch")) {
            if("btnAbortSearch".equals(c.getName())) {
                onScreenSearch_BtnAbortSearchAction(c, event);
                return;
            }
            if("tfdSearch".equals(c.getName())) {
                onScreenSearch_TfdSearchAction(c, event);
                return;
            }
            if("btnSearchFilterAll".equals(c.getName())) {
                onScreenSearch_BtnSearchFilterAllAction(c, event);
                return;
            }
            if("btnSearchFilterSong".equals(c.getName())) {
                onScreenSearch_BtnSearchFilterSongAction(c, event);
                return;
            }
            if("btnSearchFilterSpeech".equals(c.getName())) {
                onScreenSearch_BtnSearchFilterSpeechAction(c, event);
                return;
            }
            if("btnSearchFilterEbook".equals(c.getName())) {
                onScreenSearch_BtnSearchFilterEbookAction(c, event);
                return;
            }
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenSearch_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenSearch_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlSearchResult".equals(c.getName())) {
                onScreenSearch_CtlSearchResultAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewCategoryRow")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onViewCategoryRow_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIconFixed".equals(c.getName())) {
                onViewCategoryRow_BtnMediaIconFixedAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenDisclaimer")) {
            if("lblDisclaimerText".equals(c.getName())) {
                onScreenDisclaimer_LblDisclaimerTextAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenQueue")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenQueue_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenQueue_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlPlayerQueue".equals(c.getName())) {
                onScreenQueue_CtlPlayerQueueAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenSettings")) {
            if("iconOpen".equals(c.getName())) {
                onScreenSettings_IconOpenAction(c, event);
                return;
            }
            if("lstSettings".equals(c.getName())) {
                onScreenSettings_LstSettingsAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenPerformer")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onScreenPerformer_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIconFixed".equals(c.getName())) {
                onScreenPerformer_BtnMediaIconFixedAction(c, event);
                return;
            }
            if("ctlArtistAppearsIn".equals(c.getName())) {
                onScreenPerformer_CtlArtistAppearsInAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onScreenPerformer_BtnMediaIconAction(c, event);
                return;
            }
            if("ctlArtistAllTracks".equals(c.getName())) {
                onScreenPerformer_CtlArtistAllTracksAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewTrackRow")) {
            if("btnMediaActionFixed".equals(c.getName())) {
                onViewTrackRow_BtnMediaActionFixedAction(c, event);
                return;
            }
            if("btnMediaIcon".equals(c.getName())) {
                onViewTrackRow_BtnMediaIconAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("EmbPlayerBottom")) {
            if("btnPlayerPlayPause".equals(c.getName())) {
                onEmbPlayerBottom_BtnPlayerPlayPauseAction(c, event);
                return;
            }
            if("btnOpenPlayer".equals(c.getName())) {
                onEmbPlayerBottom_BtnOpenPlayerAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ScreenPlayer")) {
            if("btnPlayerPrevious".equals(c.getName())) {
                onScreenPlayer_BtnPlayerPreviousAction(c, event);
                return;
            }
            if("btnPlayerPlayPause".equals(c.getName())) {
                onScreenPlayer_BtnPlayerPlayPauseAction(c, event);
                return;
            }
            if("btnPlayerNext".equals(c.getName())) {
                onScreenPlayer_BtnPlayerNextAction(c, event);
                return;
            }
            if("btnPlayerShuffle".equals(c.getName())) {
                onScreenPlayer_BtnPlayerShuffleAction(c, event);
                return;
            }
            if("btnPlayerPlaylist".equals(c.getName())) {
                onScreenPlayer_BtnPlayerPlaylistAction(c, event);
                return;
            }
            if("btnPlayerAction".equals(c.getName())) {
                onScreenPlayer_BtnPlayerActionAction(c, event);
                return;
            }
            if("btnPlayerRepeat".equals(c.getName())) {
                onScreenPlayer_BtnPlayerRepeatAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewEmptyPlaylist")) {
            if("Button".equals(c.getName())) {
                onViewEmptyPlaylist_ButtonAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("ViewSearchHistoryRow")) {
            if("btnSearchHistoryIconFixed".equals(c.getName())) {
                onViewSearchHistoryRow_BtnSearchHistoryIconFixedAction(c, event);
                return;
            }
            if("btnSearchHistoryOptionFixed".equals(c.getName())) {
                onViewSearchHistoryRow_BtnSearchHistoryOptionFixedAction(c, event);
                return;
            }
        }
    }

      protected void onViewOptionsRow_BtnOptionsRowIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenNews_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenNews_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenNews_CtlNewsListAction(Component c, ActionEvent event) {
      }

      protected void onViewCheckBoxRow_CbxSettingsAction(Component c, ActionEvent event) {
      }

      protected void onViewCheckBoxRow_BtnSettingsLanguageIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylistOverview_TfdAddPlaylistFormTitleAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylistOverview_BtnAddPlaylistFormSaveAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylistOverview_BtnPlaylistActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylistOverview_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylistOverview_CtlOverviewPlaylistListAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_BtnEditingConfirmAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_TxtEditingAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_ChkPlaylistIsOfflineAvailableAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlaylist_CtlPlaylistMediaItemsAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_TfdLoginUsernameAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_TfdLoginPasswordAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_BtnLoginLoginAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_BtnLoginOfflineAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_BtnLoginCreateAccountAction(Component c, ActionEvent event) {
      }

      protected void onScreenLogin_BtnLoginForgotPasswordAction(Component c, ActionEvent event) {
      }

      protected void onViewPlaylistRow_BtnPlaylistActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onViewPlaylistRow_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onViewSuggestionRow_BtnSearchSuggestionIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenBrowse_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenBrowse_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenBrowse_CtlBrowseItemsAction(Component c, ActionEvent event) {
      }

      protected void onScreenLanguage_CtlAllLanguagesAction(Component c, ActionEvent event) {
      }

      protected void onViewSettingsCheckboxRow_IconOpenAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnAbortSearchAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_TfdSearchAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnSearchFilterAllAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnSearchFilterSongAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnSearchFilterSpeechAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnSearchFilterEbookAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenSearch_CtlSearchResultAction(Component c, ActionEvent event) {
      }

      protected void onViewCategoryRow_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onViewCategoryRow_BtnMediaIconFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenDisclaimer_LblDisclaimerTextAction(Component c, ActionEvent event) {
      }

      protected void onScreenQueue_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenQueue_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenQueue_CtlPlayerQueueAction(Component c, ActionEvent event) {
      }

      protected void onScreenSettings_IconOpenAction(Component c, ActionEvent event) {
      }

      protected void onScreenSettings_LstSettingsAction(Component c, ActionEvent event) {
      }

      protected void onScreenPerformer_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenPerformer_BtnMediaIconFixedAction(Component c, ActionEvent event) {
      }

      protected void onScreenPerformer_CtlArtistAppearsInAction(Component c, ActionEvent event) {
      }

      protected void onScreenPerformer_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onScreenPerformer_CtlArtistAllTracksAction(Component c, ActionEvent event) {
      }

      protected void onViewTrackRow_BtnMediaActionFixedAction(Component c, ActionEvent event) {
      }

      protected void onViewTrackRow_BtnMediaIconAction(Component c, ActionEvent event) {
      }

      protected void onEmbPlayerBottom_BtnPlayerPlayPauseAction(Component c, ActionEvent event) {
      }

      protected void onEmbPlayerBottom_BtnOpenPlayerAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerPreviousAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerPlayPauseAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerNextAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerShuffleAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerPlaylistAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerActionAction(Component c, ActionEvent event) {
      }

      protected void onScreenPlayer_BtnPlayerRepeatAction(Component c, ActionEvent event) {
      }

      protected void onViewEmptyPlaylist_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onViewSearchHistoryRow_BtnSearchHistoryIconFixedAction(Component c, ActionEvent event) {
      }

      protected void onViewSearchHistoryRow_BtnSearchHistoryOptionFixedAction(Component c, ActionEvent event) {
      }

}
