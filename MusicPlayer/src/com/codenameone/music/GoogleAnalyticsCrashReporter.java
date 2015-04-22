/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.system.CrashReport;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

/**
 *
 * @author jei
 */
public class GoogleAnalyticsCrashReporter implements CrashReport, ActionListener {
    private final GoogleAnalyticsService googleAnalytics;

    public GoogleAnalyticsCrashReporter(GoogleAnalyticsService googleAnalytics) {
        this.googleAnalytics = googleAnalytics;
    }
    
    @Override
    public void exception(Throwable t) {
        // this can cause a crash
        if (!Display.isInitialized()) {
            return;
        }
        
        googleAnalytics.sendCrashReport(t);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        evt.consume();
        googleAnalytics.sendCrashReport((Throwable) evt.getSource());
    }
    
}
