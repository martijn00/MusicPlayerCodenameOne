package com.codenameone.music;

import com.codename1.system.URLCallback;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import userclasses.StateMachine;

public class MusicPlayer implements URLCallback {
   
    private Form current;

    public void init(Object context) {
    }

    public void start() {
        if(current != null){
            current.show();

            if (StateMachine.getInstance().player != null) {
                StateMachine.getInstance().player.onWakeUp();
            }

            return;
        }
        new StateMachine("/theme");        
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }
    
    public void headphonesDisconnected() {
        if(StateMachine.getInstance() != null)
            StateMachine.getInstance().headphonesDisconnected();
    }

    public void headphonesConnected() {
        if(StateMachine.getInstance() != null)
            StateMachine.getInstance().headphonesConnected();
    }
    
    public void push(String value) {
        PushManager.getInstance().push(value);
    }
 
    public void registeredForPush(String deviceId) {
        PushManager.getInstance().registeredForPush(deviceId);
    }
 
    public void pushRegistrationError(String errorMessage, int errorCode) {
        PushManager.getInstance().pushRegistrationError(errorMessage, errorCode);
    }

    /**
     * TODO: Needs testing! What do we get, what is what?
     */
    public boolean shouldApplicationHandleURL(String url, String caller) {
        System.out.println("URL:"+url+"|Caller:"+caller);
        return true;
    }
}
