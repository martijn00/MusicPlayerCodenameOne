/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.view;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.table.TableLayout;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class DialogConfirm {
    protected StateMachine ui;
    public final static String FORM_NAME = "DialogConfirm";

    public DialogConfirm(StateMachine ui)
    {
        this.ui = ui;
    }

    public static void show(String text, Command cmdYes, Command cmdNo)
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

        Container txtContainer = new Container(new BorderLayout());
        txtContainer.setUIID("ViewConfirmRowFirst");
        dia.addComponent(txtContainer);
        
        SpanLabel lbl = new SpanLabel(text);
        lbl.setTextUIID("ViewConfirmRowFirstText");
        lbl.setPreferredW(popupWidth);
        txtContainer.addComponent(BorderLayout.CENTER, lbl);
        
        Container ctn = new Container(new LayeredLayout());
        
        Button closebg = new Button();
        closebg.setUIID("ViewOptionsRowLast");
        closebg.getStyle().setBgTransparency(0);
        closebg.setPreferredW(popupWidth);
        ctn.addComponent(closebg);

        Container ctnButtons = new Container(new TableLayout(1, 2));
        ctnButtons.getStyle().setMargin(StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, false), StateMachine.getPixelFromMM(2, true), StateMachine.getPixelFromMM(2, true));
        ctn.addComponent(ctnButtons);
        
        Button no;
        if(cmdNo != null)
        {
            no = new Button(cmdNo);
            if(cmdNo.getCommandName() != null)
                no.setText(cmdNo.getCommandName());
            else
                no.setText(StateMachine._translate("button_no", "[default] No"));
        }
        else
        {
            no = new Button();
            no.setText(StateMachine._translate("button_no", "[default] No"));
        }
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dia.dispose();
            }
        });
        no.setUIID("ViewOptionsRowClose");
        no.getStyle().setMargin(Component.RIGHT, StateMachine.getPixelFromMM(2, true));
        no.getPressedStyle().setMargin(Component.RIGHT, StateMachine.getPixelFromMM(2, true));
        no.getSelectedStyle().setMargin(Component.RIGHT, StateMachine.getPixelFromMM(2, true));
        no.setVerticalAlignment(Label.CENTER);
        
        TableLayout.Constraint RowConstraint = new TableLayout.Constraint();
        RowConstraint.setWidthPercentage(50);
        ctnButtons.addComponent(RowConstraint, no);
        
        Button yes;
        if(cmdYes != null)
        {
            yes = new Button(cmdYes);
            if(cmdYes.getCommandName() != null)
                yes.setText(cmdYes.getCommandName());
            else
                yes.setText(StateMachine._translate("button_yes", "[default] Yes"));
        }
        else
        {
            yes = new Button();
            yes.setText(StateMachine._translate("button_yes", "[default] Yes"));
        }
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dia.dispose();
            }
        });
        yes.setUIID("ViewOptionsRowClose");
        yes.setVerticalAlignment(Label.CENTER);
        
        ctnButtons.addComponent(RowConstraint, yes);
        
        dia.addComponent(ctn);
        
        dia.showPacked(BorderLayout.CENTER, true);
    }
}
