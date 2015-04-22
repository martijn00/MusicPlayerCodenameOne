/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import com.codenameone.music.view.TrackListModel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martijn00
 */
public class Playlist extends TrackListModel implements Externalizable {
 
    private Integer id;
    private String name;
    private ArrayList access;
    private boolean offline_available;

    /**
     * Should only be used by de-serializer
     * @deprecated
     */
    public Playlist()
    {
        super();
    }

    public Playlist(String name)
    {
        super();

        this.name = name;
    }

    public Playlist(Map data)
    {
        super();

        if (data.get("tracks") != null) {
            List<Map> tracks = (List<Map>)data.get("tracks");
            for (Map track : tracks) {
                addItem(track);
            }
        }

        if (data.get("id") instanceof Double) {
            this.id = ((Double)data.get("id")).intValue();
        } else {
            this.id = Integer.parseInt(data.get("id").toString());
        }

        this.name = (String)data.get("name");
        this.access = (ArrayList)data.get("access");
        this.offline_available = false;
    }

    public boolean getOfflineAvailable()
    {
        return this.offline_available;
    }

    public void setOfflineAvailable(boolean offline_available)
    {
        this.offline_available = offline_available;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList getAccessList()
    {
        return access;
    }

    @Override
    public Map getItemAt(int index)
    {
        return (Map)super.getItemAt(index);
    }

    @Override
    public int getVersion() {
        return 4;
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {
        ArrayList<Map> data = new ArrayList<Map>();
        for (int i = 0; i < getSize(); i++)
        {
            data.add(getItemAt(i));
        }

        Util.writeObject(id, out);
        Util.writeUTF(name, out);
        Util.writeObject(access, out);
        Util.writeObject(offline_available, out);
        Util.writeObject(data, out);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        ArrayList<Map> data;
        switch (version) {
            case 4:
                id = (Integer)Util.readObject(in);
                name = Util.readUTF(in);
                access = (ArrayList)Util.readObject(in);
                offline_available = (Boolean)Util.readObject(in);
                data = (ArrayList<Map>)Util.readObject(in);

                for (Map item : data)
                {
                    addItem(item);
                }
                break;

            case 3:
                id = (Integer)Util.readObject(in);
                name = Util.readUTF(in);
                access = (ArrayList)Util.readObject(in);
                data = (ArrayList<Map>)Util.readObject(in);

                for (Map item : data)
                {
                    addItem(item);
                }
                break;

            case 2:
                name = Util.readUTF(in);
                data = (ArrayList<Map>)Util.readObject(in);

                for (Map item : data)
                {
                    addItem(item);
                }
                break;
        }
    }
    
    @Override
    public String getObjectId() {
        return "Playlist";
    }
}