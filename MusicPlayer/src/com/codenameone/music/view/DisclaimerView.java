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
public class DisclaimerView {
    protected StateMachine ui;
    private final static String FORM_NAME = "ScreenDisclaimer";

    public DisclaimerView(StateMachine ui) {
        this.ui = ui;
    }
    
    public void show()
    {
        ui.showForm(FORM_NAME, null);
    }
    
    public void beforeShow(Form f)
    {
        f.setTitle(ui.translate("disclaimer_title", "[default]Copyright"));
        ui.findLblDisclaimerText(f).setText(ui.translate("disclaimer_text", "[default] © Worldwide Copyright https://github.com/martijn00"));
    }
}
