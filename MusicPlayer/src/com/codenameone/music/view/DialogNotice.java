/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class DialogNotice {
    protected StateMachine ui;
    public final static String FORM_NAME = "DialogNotice";

    public DialogNotice(StateMachine ui)
    {
        this.ui = ui;
    }

    public static void show(String title, String text, final Command cmd)
    {        
        int popupWidth = Display.getInstance().getCurrent().getWidth() - StateMachine.getPixelFromMM(10, true);
        if(popupWidth > StateMachine.getPixelFromMM(40,true))
            popupWidth = StateMachine.getPixelFromMM(40,true);
        
        final Dialog dia = new Dialog();
        dia.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        dia.getDialogStyle().setBgTransparency(0);
        
        Command backCommand = new Command(null){
            @Override
            public void actionPerformed(ActionEvent ev) {
                dia.dispose();
            }
        };
        dia.setBackCommand(backCommand);

        SpanLabel lblTitle = new SpanLabel(title);
        lblTitle.setUIID("ViewOptionsRowFirst");
        lblTitle.setTextUIID("ViewOptionsRowFirstText");
        lblTitle.setPreferredW(popupWidth);
        dia.addComponent(lblTitle);
        
        SpanLabel lblText = new SpanLabel(text);
        lblText.setUIID("ViewOptionsRow");
        lblText.setTextUIID("ViewOptionsRowText");
        lblText.setPreferredW(popupWidth);
        dia.addComponent(lblText);
        
        Container ctn = new Container(new LayeredLayout());

        Button closebg = new Button();
        closebg.setUIID("ViewOptionsRowLast");
        closebg.getStyle().setBgTransparency(0);
        closebg.setPreferredW(popupWidth);
        ctn.addComponent(closebg);

        Container ctnButtons = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ctnButtons.getStyle().setMargin(StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, true), StateMachine.getPixelFromMM(2, true));
        ctn.addComponent(ctnButtons);
        
        Button close = new Button(StateMachine._translate("button_ok", "[default] Ok"));
        if (cmd != null && cmd.getCommandName() != null)
            close.setText(cmd.getCommandName());
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (cmd != null)
                    cmd.actionPerformed(evt);
                dia.dispose();
            }
        });
        close.setUIID("ViewOptionsRowClose");
        close.setVerticalAlignment(Label.CENTER);
        
        ctnButtons.addComponent(close);
        dia.addComponent(ctn);
        
        dia.showPacked(BorderLayout.CENTER, true);
    }
}
