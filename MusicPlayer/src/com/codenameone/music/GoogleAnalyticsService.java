/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.analytics.AnalyticsService;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author jei
 */
public class GoogleAnalyticsService extends AnalyticsService {
    private final String tid;
    private String sr = null;

    public GoogleAnalyticsService(String tid) {
        this.tid = tid;
    }

    @Override
    protected boolean isAnalyticsEnabled() {
        return true;
    }
    
    @Override
    protected void visitPage(String page, String referrer) {
        // https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide#apptracking
        ConnectionRequest req = GetGARequest();
        req.addArgument("t", "appview");        
        req.addArgument("cd", page);
        NetworkManager.getInstance().addToQueue(req);
    }
    
    public void sendCrashReport(Throwable t) {
        // https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide#exception
        final ConnectionRequest req = GetGARequest();
        req.addArgument("t", "exception");
        
        LogHelper logger = new LogHelper();
        logger.logThrowable(t);
        req.addArgument("exd", formatErrorMessage(logger.getLogContents()));
        
        req.addArgument("exf", "1");
        System.out.println("Sending crash report..");
        NetworkManager.getInstance().addToQueue(req);
    }

    private ConnectionRequest GetGARequest() {
        String contentLanguage = "n/a";
        if (Api.getInstance().hasProfile())
            contentLanguage = Api.getInstance().getLanguage();

        ConnectionRequest req = new ConnectionRequest();
        req.setUrl("http://www.google-analytics.com/collect");
        req.setPost(true);
        req.setFailSilently(true);
        req.addArgument("v", "1");
        req.addArgument("tid", tid);
        req.addArgument("cid", String.valueOf(Log.getUniqueDeviceId()));
        req.addArgument("an", "MusicPlayer app");
        req.addArgument("av", Display.getInstance().getProperty("AppVersion", "?"));
        req.addArgument("sr", getScreenResolution());
        req.addArgument("ul", contentLanguage);
        req.addArgument("cd1", Display.getInstance().getPlatformName());
        return req;
    }
    
    private String getScreenResolution() {
        if (sr == null) {
            sr = Display.getInstance().getDisplayWidth() + "x" + Display.getInstance().getDisplayHeight();
        }
        return sr;
    }
    
    private String formatErrorMessage(String message) {
        if (message.indexOf('\n') < 0) {
            return message;
        }
        
        List<String> lines = StringUtil.tokenize(message, '\n');
        StringBuilder sb = new StringBuilder();
        for (int i=1; i<lines.size(); i++) { // Note: Deliberately skip first line
            String line = lines.get(i);
            line = StringUtil.replaceAll(line, "\r", "");
            line = StringUtil.replaceAll(line, "at ", "");
            line = StringUtil.replaceAll(line, "userclasses.", "");
            line = StringUtil.replaceAll(line, "generated.", "");
            line = StringUtil.replaceAll(line, "com.codename1.", "");
            line = StringUtil.replaceAll(line, "com.codenameone.music.", "");
            sb.append(line);
        }
        Log.p(sb.toString());
        return sb.toString();
    }
    
    /*
     * Provides a way to get the stack trace for a Throwable
     */
    class LogHelper extends Log
    {
        private OutputStreamWriter sw;
        private ByteArrayOutputStream stream;
        
        @Override
        protected Writer createWriter() throws IOException {
            
            if (sw == null) {
                stream = new ByteArrayOutputStream();
                sw = new OutputStreamWriter(stream);
            }
            
            return sw;
        }
        
        @Override
        public void logThrowable(Throwable t)
        {
            super.logThrowable(t);
        }
        
        public String getLogContents()
        {
            return stream.toString();
        }
    }
}
