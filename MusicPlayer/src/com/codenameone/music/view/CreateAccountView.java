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
public class CreateAccountView {
    protected StateMachine ui;
    public static String FORM_NAME = "ScreenCreateAccount";
    
    public CreateAccountView(StateMachine ui) {
        this.ui = ui;
    }
    
    public void beforeShow(Form f) {
        f.setTitle(ui.translate("view_title_createaccount", "[default]Create account"));
    }
    
    public void show()
    {
        ui.showForm(FORM_NAME, null);
    }
}
