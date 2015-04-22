/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codenameone.music;

import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.ui.Display;
import java.io.IOException;

/**
 * Ask if the file system is ready before calling any operational method like
 * opening a stream, rename or delete a file.
 *
 * @author Martijn00
 */
public class FileManager {
    private static final FileManager INSTANCE = new FileManager();
    private static final String DIR_ON_SHARED_STORAGES = "MusicPlayer-App";

    public static FileManager getInstance()
    {
        return INSTANCE;
    }

    /**
     * Check if the root exists (maybe you removed the SD card?).
     * @return 
     */
    public Boolean isReady()
    {
        return deviceSupportsHomeStorageOnly() || FileSystemStorage.getInstance().isDirectory(getRoot());
    }

    /**
     * Figures out the best place to store data and define it as global root.
     * This can be a SD card, the main storage or any unknown type. If more
     * roots are available, the system will check them in this order to find the
     * best one and save it.
     */
    private void initRoot()
    {
        String root = getRoot(false);
        if (root != null && root.length() > 0) {
            return;
        }

        root = "";

        FileSystemStorage storage = FileSystemStorage.getInstance();

        String roots[] = storage.getRoots();
        // Detect a root-folder to be used. SD-card is preferred over phone- and unknown storage types.
        int[] rootTypes = { FileSystemStorage.ROOT_TYPE_SDCARD, FileSystemStorage.ROOT_TYPE_MAINSTORAGE, FileSystemStorage.ROOT_TYPE_UNKNOWN };
        for (int rootType : rootTypes) {
            for (String root1 : roots) {
                if (storage.getRootType(root1) == rootType) {
                    root = root1;
                }
            }
        }

        setRootDirectory(root);
    }

    /**
     * Set a path as root-directory (creates a subdirectory within this storage)
     * @param root 
     */
    public void setRootDirectory(String root)
    {
        FileSystemStorage storage = FileSystemStorage.getInstance();
        char separator = storage.getFileSystemSeparator();
        
        if (!root.equals("")) {
            // Append slash if it doesn't end with one.
            if (!root.endsWith(String.valueOf(separator)))
                root += separator;

            // Append folder and create it if it doesn't exist already
            root += DIR_ON_SHARED_STORAGES;
            if (!storage.isDirectory(root))
                storage.mkdir(root);

            // Check if directory could be created
            if (!storage.isDirectory(root))
                // Probably permission denied ...
                root = "";
        }

        // No storage found? Take the appHomePath.
        if (root.equals("")) {
            root = FileSystemStorage.getInstance().getAppHomePath();
        }

        Storage.getInstance().writeObject("offlineStoragePath", root);
    }
    
    public boolean deviceSupportsHomeStorageOnly()
    {
        return Display.getInstance().getPlatformName().equals("ios");
    }

    public void resetRoot()
    {
        Storage.getInstance().deleteStorageFile("offlineStoragePath");
        initRoot();
    }

    private String getRoot(boolean forceResult)
    {
        if (deviceSupportsHomeStorageOnly()) {
            return FileSystemStorage.getInstance().getAppHomePath();
        }

        if(forceResult && Storage.getInstance().readObject("offlineStoragePath") == null) {
            initRoot();
        }

        return (String)Storage.getInstance().readObject("offlineStoragePath");
    }

    public String getRoot()
    {
        String root = getRoot(true);

        // Append slash if it doesn't end with one. We get it without for iOS in the simulator.
        char separator = FileSystemStorage.getInstance().getFileSystemSeparator();
        if (!root.endsWith(String.valueOf(separator))) {
            root += separator;
        }
        
        return root;
    }

    /**
     * Returns the complete path.
     * Please make sure the system is ready by calling isReady().
     *
     * @param localFilename
     * @return String
     */
    public String getFullPath(String localFilename)
    {
        return getRoot() + localFilename;
    }

    /**
     * Queries if a file exists.
     * @param localFilename
     * @return 
     */
    public Boolean exists(String localFilename)
    {
        return isReady() && FileSystemStorage.getInstance().exists(getFullPath(localFilename));
    }

    /**
     * Rename a file. Both strings are just filenames. No paths in here.
     * @param tmpFilename
     * @param localFilename 
     */
    public void rename(String tmpFilename, String localFilename)
    {
        FileSystemStorage.getInstance().rename(getFullPath(tmpFilename), localFilename);
    }

    /**
     * Delete the file from the file system.
     * If it doesn't work, try it again 10 times (this means, retrying to delete
     * this file for the next 5sec). If it doesn't work then, leave it ...
     * @param localFilename 
     */
    public void delete(String localFilename)
    {
        FileSystemStorage.getInstance().deleteRetry(getFullPath(localFilename), 10);
    }

    public String[] listFiles()
    {
        try {
            return FileSystemStorage.getInstance().listFiles(getFullPath(""));
        } catch (IOException ex) {
            return null;
        }
    }
}
