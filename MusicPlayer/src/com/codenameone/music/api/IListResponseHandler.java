/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music.api;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Martijn00
 */
public interface IListResponseHandler extends IResponseHandler {

    public void onSuccess(List list, Map<String, String> headers);
}
