/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.Cookie;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.PlaylistFactory;
import com.codenameone.music.UserProfile;
import com.codenameone.music.api.IJsonResponseHandler;
import com.codenameone.music.api.IProfileResponseHandler;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class LoginView {
    protected StateMachine ui;
    public final static String FORM_NAME = "ScreenLogin";

    // Just to prevent initialisation without parameters ...
    private LoginView() {}

    public LoginView(StateMachine ui) {
        this.ui = ui;
    }
    
    public void beforeShow(final Form f) {
        if(!PlaylistFactory.getInstance().hasLocalPlaylists()) {
            ui.hideComponent(ui.findBtnLoginOffline(f));
        }
        
        if(Display.getInstance().getPlatformName().equals("ios"))
        {
            ui.hideComponent(ui.findBtnLoginCreateAccount(f));
            ui.hideComponent(ui.findBtnLoginForgotPassword(f));
        }

        if(Display.getInstance().getPlatformName().equals("ios") && Display.getInstance().getDisplayHeight() > 960)
            ui.findCtnLoginScroll(f).setScrollableY(false);
        
        ui.findTfdLoginPassword(f).putClientProperty("goButton", Boolean.TRUE);
        ui.findTfdLoginPassword(f).setDoneListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onLoginAction(f);
            }
        });
    }
    
    public void show()
    {
        show(null);
    }

    public void show(Command cmd)
    {
        Cookie.clearCookiesFromStorage();
        Api.getInstance().logout();
        ui.setHomeForm(FORM_NAME);
        ui.showForm(FORM_NAME, cmd);
    }

    public void onLoginAction(Form f)
    {
        final TextField passwordField = ui.findTfdLoginPassword(f);
        final TextField usernameField = ui.findTfdLoginUsername(f);
        final String username = ui.findTfdLoginUsername(f).getText();
        final String password = passwordField.getText();

        ui.newsView.show();
        /*
        if(!username.equals("") && !password.equals(""))
        {
            InfiniteProgress inf = new InfiniteProgress();            
            final Dialog progress = inf.showInifiniteBlocking();
            progress.setName(FORM_NAME);
            
            if(!Api.getInstance().isOnline())
            {
                Api.getInstance().goOnline(new IJsonResponseHandler() {

                    @Override
                    public void onSuccess(Map data, Map<String, String> headers) {
                        authenticate(username, password, progress, passwordField);
                    }

                    @Override
                    public void onFailure(int code, String message, Map<String, String> headers) {
                    }
                });
            }
            else
            {
                authenticate(username, password, progress, passwordField);
            }
        }
        else
        {
            DialogNotice.show(ui.translate("dialog_login_failed_title", "[default]Cannot log in"), ui.translate("dialog_login_failed_message", "[default]Please check your username and password"), new Command(null) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Display.getInstance().callSerially(new Runnable() {
                        @Override
                        public void run() {
                            if(username.equals(""))
                            {
                                usernameField.requestFocus();
                                Display.getInstance().editString(usernameField, 9999, TextField.ANY, "");
                            }
                            else
                            {
                                passwordField.requestFocus();
                                Display.getInstance().editString(passwordField, 9999, TextField.PASSWORD, "");
                            }
                        }
                    });
                }
            });
        }*/
    }

    private void authenticate(String username, String password, final Dialog progress, final TextField passwordField)
    {
        Api.getInstance().authenticate(new IProfileResponseHandler() {
            @Override
            public void onSuccess(UserProfile data, Map<String, String> headers) {
                progress.dispose();

                // Get the app-argument
                final String urlPath = Display.getInstance().getProperty("AppArg", "");
                Display.getInstance().setProperty("AppArg", null);

                ui.loadFormByUri(urlPath, true);
            }
            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                progress.dispose();
                DialogNotice.show(ui.translate("dialog_login_failed_title", "[default]Cannot log in"), ui.translate("dialog_login_failed_message", "[default]Please check your username and password"), new Command(null) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        Display.getInstance().callSerially(new Runnable() {
                            @Override
                            public void run() {
                                passwordField.requestFocus();
                                passwordField.setText("");
                                Display.getInstance().editString(passwordField, 9999, TextField.PASSWORD, "");
                            }
                        });
                    }
                });
            }
        }, username, password);
    }

    public void onCreateAccountAction()
    {
        ui.createAccountView.show();
    }
    
    public void onForgotPasswordAction()
    {
        ui.forgotPasswordView.show();
    }
    
    public void onLoginOfflineAction()
    {
        StateMachine.goOffline();
    }
}
