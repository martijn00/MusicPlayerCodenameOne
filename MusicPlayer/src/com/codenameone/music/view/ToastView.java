/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music.view;

import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Martijn00
 */
public class ToastView {
    private static Boolean isVisible = false;
    private static int toastTimeOut = 2000;
    
    public static void show(String text, final Form f)
    {
        isVisible = true;
        
        f.getLayeredPane().setLayout(new BorderLayout());
        final Label l = new Label(text);
        l.setUIID("ToastMessage");
        f.getLayeredPane().addComponent(BorderLayout.SOUTH, l);
        f.repaint();

        Timer exitTimer = new Timer();
        exitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                f.getLayeredPane().removeAll();
                f.repaint();
                isVisible = false;
            }
        }, toastTimeOut);
    }
    
    public static Boolean isToastVisible()
    {
        return isVisible;
    }
    
    public static void setToastTimeOut(int timeout)
    {
        toastTimeOut = timeout;
    }
}
