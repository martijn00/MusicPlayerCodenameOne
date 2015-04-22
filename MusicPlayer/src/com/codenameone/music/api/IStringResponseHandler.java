/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.api;

import java.util.Map;

/**
 *
 * @author Martijn00
 */
public interface IStringResponseHandler extends IResponseHandler {

    public void onSuccess(Map<String, String> headers);
}
