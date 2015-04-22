/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.api;

import java.util.Map;
import com.codenameone.music.UserProfile;

/**
 *
 * @author Martijn00
 */
public interface IProfileResponseHandler extends IResponseHandler {

    public void onSuccess(UserProfile data, Map<String, String> headers);
}
