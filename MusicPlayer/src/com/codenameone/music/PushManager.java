/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music;

import com.codename1.io.Log;
import com.codename1.push.Push;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import java.util.Hashtable;

/**
 *
 * @author mvandijk
 */
public class PushManager {
    private static final PushManager INSTANCE = new PushManager();
    
    public static PushManager getInstance()
    {
        return INSTANCE;
    }
    
    public void registerForPush() 
    {
        Hashtable meta = new Hashtable();
        meta.put(com.codename1.push.Push.GOOGLE_PUSH_KEY, "49326842176");        
        Display.getInstance().registerPush(meta, true);
        
        Log.p("Push: Register");
    }
    
    public void push(String value) {
        Dialog.show("Push Received", value, "OK", null);
        Log.p("Push: Received " + value);
    }
 
    public void registeredForPush(String deviceId) {
        Dialog.show("Push Registered", "Device ID: " + deviceId + "\nDevice Key: " + Push.getDeviceKey() , "OK", null);
        
        Log.p("Push: Registered " + deviceId);
    }
 
    public void pushRegistrationError(String errorMessage, int errorCode) {
        Dialog.show("Registration Error", "Error " + errorMessage + "\n" + errorCode, "OK", null);
        
        Log.p("Push: Error - " + errorMessage + " - Code:" + errorCode);
    }
}
