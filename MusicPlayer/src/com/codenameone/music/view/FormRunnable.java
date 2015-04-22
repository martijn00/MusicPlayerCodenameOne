/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music.view;

import com.codename1.ui.Form;

/**
 *
 * @author Martijn00
 */
public abstract class FormRunnable implements Runnable
{
    protected Form f;

    public void setForm(Form f) {
        this.f = f;
    }
}
