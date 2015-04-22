/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codenameone.music;

import com.codename1.io.Externalizable;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Display;
import com.codenameone.music.view.TrackListModel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martijn00
 */
public class Queue implements Externalizable {
    private List<Map> previousQueue;
    private Map current;
    private List<Map> userQueue;
    private List<Map> nextQueue;
    private List<Map> nextShuffledQueue;

    private boolean repeat;

    public Queue()
    {
        resetQueue();
    }

    private void resetQueue()
    {
        previousQueue = new ArrayList<Map>();
        current = null;
        userQueue = new ArrayList<Map>();
        nextQueue = new ArrayList<Map>();
        nextShuffledQueue = null;
    }

    private void saveQueue()
    {
        Log.p("saveQueue");
        final Queue q = this;
        // Do this serially, because it takes the most time of all operations here.
        Display.getInstance().scheduleBackgroundTask(new Runnable() {
            @Override
            public void run() {
                Log.p("before writeObject");
                Storage.getInstance().writeObject("queue", q);
                Log.p("after writeObject");
            }
        });
    }

    public void setQueue(TrackListModel playlist)
    {
        resetQueue();
        
        int index = playlist.getSelectedIndex();
        current = playlist.getItemAt(index);
        
        previousQueue.addAll(playlist.getList().subList(0, index));
        nextQueue.addAll(playlist.getList().subList(index + 1, playlist.getSize()));
        
        Collections.reverse(previousQueue);

        saveQueue();
    }

    public List<Map> getPreviousQueue()
    {
        return previousQueue;
    }

    public List<Map> getUserQueue()
    {
        return userQueue;
    }

    /**
     * Get a list of songs playing next (does not include the user list!)
     * @return List<Map>
     */
    public List<Map> getNextQueue()
    {
        if (nextShuffledQueue != null)
            return nextShuffledQueue;
        else
            return nextQueue;
    }

    public Map getCurrent()
    {
        return current;
    }

    public void add(Map item)
    {
        userQueue.add(item);

        saveQueue();
    }

    public boolean hasPrevious()
    {
        return repeat || (previousQueue.size() > 0);
    }

    public boolean hasNext()
    {
        return repeat || (nextQueue.size() > 0 || userQueue.size() > 0);
    }

    protected void setPreviousAsCurrent()
    {
        userQueue.add(0, current);
        if (repeat && previousQueue.size() == 0) {
            if (nextQueue.size() > 0) {
                if (nextShuffledQueue != null) {
                    int lastIndex = nextShuffledQueue.size() - 1;
                    current = nextShuffledQueue.get(lastIndex);
                    nextShuffledQueue.remove(lastIndex);
                } else {
                    int lastIndex = nextQueue.size() - 1;
                    current = nextQueue.get(lastIndex);
                }

                nextQueue.remove(current);

            } else {
                int lastIndex = nextQueue.size() - 1;
                current = userQueue.get(lastIndex);
                userQueue.remove(lastIndex);
            }
        } else {
            current = previousQueue.get(0);
            previousQueue.remove(0);
        }

        saveQueue();
    }

    protected void setNextAsCurrent()
    {
        Log.p("setNextAsCurrent");
        if (current != null) {
            previousQueue.add(0, current);
        }
        Log.p("added current to prev");

        if(userQueue.size() > 0)
        {
            current = userQueue.get(0);
            Log.p("current is set");
            userQueue.remove(0);
        }
        else
        {
            // Add all previously played items if repeat is enabled and the app is trying to play the next song and no song is in the next-list
            if (repeat && nextQueue.size() == 0) {
                Collections.reverse(previousQueue);

                if (nextShuffledQueue != null) {
                    nextShuffledQueue.addAll(previousQueue);
                }
                nextQueue.addAll(previousQueue);

                previousQueue = new ArrayList<Map>();
            }

            if (nextShuffledQueue != null) {
                current = nextShuffledQueue.get(0);
                nextShuffledQueue.remove(0);
            } else {
                current = nextQueue.get(0);
            }

            nextQueue.remove(current);
        }

        saveQueue();
        Log.p("setNextAsCurrent done");
    }

    public boolean getRepeat()
    {
        return repeat;
    }

    public void setRepeat(Boolean repeat)
    {
        this.repeat = repeat;
    }

    public boolean getShuffle()
    {
        return nextShuffledQueue != null;
    }

    public void setShuffle(Boolean shuffle)
    {
        if (shuffle) {
            nextShuffledQueue = new ArrayList<Map>();
            nextShuffledQueue.addAll(nextQueue);

            Collections.shuffle(nextShuffledQueue);
        } else {
            nextShuffledQueue = null;
        }

        saveQueue();
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {
        Util.writeObject(previousQueue, out);
        Util.writeObject(current, out);
        Util.writeObject(userQueue, out);
        Util.writeObject(nextQueue, out);
        Util.writeObject(nextShuffledQueue, out);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        switch (version) {
            case 0:
                previousQueue = (List<Map>)Util.readObject(in);
                current = (Map)Util.readObject(in);
                userQueue = (List<Map>)Util.readObject(in);
                nextQueue = (List<Map>)Util.readObject(in);
                nextShuffledQueue = (List<Map>)Util.readObject(in);
                break;
        }
    }

    @Override
    public String getObjectId() {
        return "Queue";
    }

    public void removeItem(Map item) {
        if (current == item) {
            current = null;
        } else if (userQueue.contains(item)) {
            userQueue.remove(item);
        } else {
            if (nextShuffledQueue != null) {
                nextShuffledQueue.remove(item);
            }
            nextQueue.remove(item);
        }
    }
}
