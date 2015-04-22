/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.ui.Form;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class ForgotPasswordView {
    protected StateMachine ui;
    public static String FORM_NAME = "ScreenForgotPassword";
    
    public ForgotPasswordView(StateMachine ui) {
        this.ui = ui;
    }
    
    public void beforeShow(Form f) {
        f.setTitle(ui.translate("view_title_forgotpassword", "[default]Forgot password"));
    }
    
    public void show()
    {
        ui.showForm(FORM_NAME, null);
    }
}
