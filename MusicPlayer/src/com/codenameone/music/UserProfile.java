/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jei
 */
public class UserProfile implements Externalizable {

    private String username;
    private String token;

    private String firstName;
    private String lastName;
    private List<String> languages;

    public UserProfile() {}

    public UserProfile(Map data)
    {
        username = (String) data.get("username");
        token = (String) data.get("token");

        firstName = (String) data.get("first_name");
        lastName = (String) data.get("last_name");
        languages = (ArrayList<String>) data.get("languages");
    }

    public String getUsername()
    {
        return username;
    }

    public String getToken()
    {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLanguages() {
        return languages;
    }

    @Override
    public String toString() {
        return "UserProfile{" + "firstName=" + getFirstName() + ", lastName=" + getLastName() + '}';
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public int getVersion() {
        return 3;
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {
        Util.writeUTF(username, out);
        Util.writeUTF(token, out);

        Util.writeUTF(firstName, out);
        Util.writeUTF(lastName,  out);
        Util.writeObject(languages, out);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        switch (version) {
            case 3:
                username = Util.readUTF(in);
                token = Util.readUTF(in);

                firstName = Util.readUTF(in);
                lastName =  Util.readUTF(in);
                languages = (ArrayList<String>)Util.readObject(in);
                break;
            case 2:
                firstName = Util.readUTF(in);
                lastName =  Util.readUTF(in);
                languages = (ArrayList<String>)Util.readObject(in);
                break;
        }
    }

    @Override
    public String getObjectId() {
        return "UserProfile";
    }
}
