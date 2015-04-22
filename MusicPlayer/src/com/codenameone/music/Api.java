package com.codenameone.music;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Display;
import com.codename1.util.StringUtil;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codenameone.music.api.IJsonResponseHandler;
import com.codenameone.music.api.IProfileResponseHandler;
import com.codenameone.music.api.IStringResponseHandler;
import com.codenameone.music.api.Proxy;

/**
 *
 * @author Martijn00
 */
public class Api {
    private static final Api INSTANCE = new Api();

    private String language;
    private final Proxy apiProxy;

    // Adding cache on per-instance basis
    private Map<Integer, Map> categoryCache;
    private Map<String, Map> trackCache; // Per language ...
    private Map<String, Map> categoriesByContentCache; // Per language ...
    private Map<String, Map> tracksByTagsCache; // Per language ...
    private Map newsCache;
    private Map searchCache;
    private String searchCacheTerm;
    private String searchCacheFilters;

    
    private Api()
    {
        apiProxy = new Proxy();

        if (Storage.getInstance().readObject("API Base - supported languages") == null)
            Storage.getInstance().writeObject("API Base - supported languages", new ArrayList<String>());

        clearCache();
    }
    
    public static Api getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void goOnline(final IJsonResponseHandler handler)
    {
        apiProxy.get("/", null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {

                Storage.getInstance().writeObject("API Base - supported languages", data.get("languages"));

                if (handler != null) {
                    handler.onSuccess(null, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    private void clearCache()
    {
        categoryCache = new HashMap<Integer, Map>();
        trackCache = new HashMap<String, Map>();
        categoriesByContentCache = new HashMap<String, Map>();
        tracksByTagsCache = new HashMap<String, Map>();
        newsCache = null;
    }

    public void setLanguage(String lang)
    {
        language = lang;
        Storage.getInstance().writeObject("API Base - language", lang);
        clearCache();
    }

    public String getLanguage()
    {
        if (language != null)
            return language;

        language = (String) Storage.getInstance().readObject("API Base - language");
        if (language != null)
            return language;
        
        ArrayList<String> supportedLanguages = getSupportedLanguagesStorage();
        if (supportedLanguages != null && supportedLanguages.contains(Display.getInstance().getLocalizationManager().getLanguage()))
            language = Display.getInstance().getLocalizationManager().getLanguage();
        
        if (language != null)
            return language;
        else
            return "en";
    }

    public ArrayList<String> getSupportedLanguages()
    {
        return getSupportedLanguagesStorage();
    }
    
    public ConnectionRequest authenticate(final IProfileResponseHandler handler, String username, String password)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        
        return apiProxy.post("/login/authentication", params, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                final UserProfile userProfile = new UserProfile(data);
                Log.p("API: UserProfile retrieved successfully. " + userProfile);
                Storage.getInstance().writeObject("API Base - Profile", userProfile);

                if (handler != null) {
                    handler.onSuccess(userProfile, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public void logout()
    {
        Storage.getInstance().deleteStorageFile("API Base - Profile");
        Storage.getInstance().deleteStorageFile("API Base - Library");
        clearCache();
    }

    public boolean hasProfile()
    {
        return Storage.getInstance().readObject("API Base - Profile") != null;
    }

    public UserProfile getProfile()
    {
        return (UserProfile)Storage.getInstance().readObject("API Base - Profile");
    }

    public Map getNewsCache()
    {
        return newsCache;
    }

    public ConnectionRequest getNews(final IJsonResponseHandler handler, boolean allowCache, int from, int size)
    {
        if (newsCache != null && allowCache) {
            handler.onSuccess(newsCache, new HashMap<String, String>());
            return null;
        }

        ArrayList<String> contentTypes = new ArrayList<String>();
        contentTypes.add("song");
        contentTypes.add("speech");
        contentTypes.add("audiobook");

        Map<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("from", String.valueOf(from));
        urlParams.put("size", String.valueOf(size));
        urlParams.put("content-type[]", contentTypes.toArray(new String[3]));
        
        return apiProxy.get("/track/", urlParams, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded news.");

                if(newsCache == null)
                    newsCache = data;
                else
                    newsCache.putAll(data);
                
                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public Map getCategoriesByContentTypesCache(List<String> contentTypesList)
    {
        final String[] contentTypes = contentTypesList.toArray(new String[contentTypesList.size()]);

        String tmp = "";
        for (String contentType : contentTypes) {
            tmp += contentType + "/";
        }
        final String key = tmp;

        return categoriesByContentCache.get(key);
    }

    public ConnectionRequest getCategoriesByContentTypes(final IJsonResponseHandler handler, final List<String> contentTypesList, boolean allowCache)
    {
        final String[] contentTypes = contentTypesList.toArray(new String[contentTypesList.size()]);

        String tmp = "";
        for (String contentType : contentTypes) {
            tmp += contentType + "/";
        }
        final String key = tmp;

        if (categoriesByContentCache.get(key) != null && allowCache) {
            handler.onSuccess(categoriesByContentCache.get(key), new HashMap<String, String>());
            return null;
        }

        Map<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("from", "0");
        urlParams.put("size", "300");
        if (contentTypes.length > 0) {
            urlParams.put("content-type[]", contentTypes);
        }

        return apiProxy.get("/album/", urlParams, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {

                categoriesByContentCache.put(key, data);
                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public Map getCategoryCache(Integer id)
    {
        return categoryCache.get(id);
    }

    public ConnectionRequest getCategory(final IJsonResponseHandler handler, Integer id, boolean allowCache)
    {
        if (categoryCache.get(id) != null && allowCache) {
            handler.onSuccess(categoryCache.get(id), new HashMap<String, String>());
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.get(prepareUri("/album/{id}", params), null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded the category '" + data.get("title") + "' - " + data.get("id"));

                categoryCache.put(MediaHelper.getId(data), data);

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public Map getTracksByTagsCache(final List<String> tagsList) {
        final String[] tags = tagsList.toArray(new String[tagsList.size()]);

        String tmp = "";
        for (String tag : tags) {
            tmp += tag + "/";
        }
        final String key = tmp;

        return tracksByTagsCache.get(key);
    }

    public ConnectionRequest getTracksByTags(final IJsonResponseHandler handler, final List<String> tagsList, boolean allowCache)
    {
        final String[] tags = tagsList.toArray(new String[tagsList.size()]);

        String tmp = "";
        for (String tag : tags) {
            tmp += tag + "/";
        }
        final String key = tmp;

        if (tracksByTagsCache.get(key) != null && allowCache) {
            handler.onSuccess(tracksByTagsCache.get(key), new HashMap<String, String>());
            return null;
        }

        Map<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("from", "0");
        urlParams.put("size", "300");
        if (tags.length > 0) {
            urlParams.put("tags[]", tags);
        }

        return apiProxy.get("/track/", urlParams, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {

                tracksByTagsCache.put(key, data);
                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public Map getTrackFromCache(Integer id, String language)
    {
        return trackCache.get(id + "_" + language);
    }

    public ConnectionRequest getTrack(final IJsonResponseHandler handler, final Integer id, boolean allowCache)
    {
        if (getTrackFromCache(id, language) != null && allowCache) {
            handler.onSuccess(getTrackFromCache(id, language), new HashMap<String, String>());
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        return apiProxy.get(prepareUri("/track/{id}", params), null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                data.put("id", id);

                Log.p("API: Loaded the track '"+data.get("title")+"' - '"+data.get("id")+"'");

                trackCache.put(id + "_" + data.get("language"), data);

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest getTrack(final IJsonResponseHandler handler, final Integer id, String tmpLanguage, boolean allowCache)
    {
        String backupLanguage = language;

        language = tmpLanguage;
        ConnectionRequest request = getTrack(handler, id, allowCache);
        language = backupLanguage;

        return request;
    }

    public ConnectionRequest getPerformer(final IJsonResponseHandler handler, int id)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        return apiProxy.get(prepareUri("/contributor/{id}", params), null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest getPerformersTracks(final IJsonResponseHandler handler, int id)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        return apiProxy.get(prepareUri("/contributor/{id}/track/", params), null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest getPlaylists(final IJsonResponseHandler handler)
    {
        return apiProxy.get("/track_collection/", null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded the playlists. Size: " + ((ArrayList)data.get("root")).size());

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest getPlaylist(final IJsonResponseHandler handler, Integer id)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.get(prepareUri("/track_collection/{id}", params), null, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded the playlist '" + data.get("name") + "' - " + data.get("id"));

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest linkTracksToPlaylist(final IStringResponseHandler handler, final Integer id, final List<Map> tracks)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.link(prepareUri("/track_collection/{id}", params), tracks, new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                Log.p("API: Linked tracks to the playlist '" + id);

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest addPlaylist(final IStringResponseHandler handler, final Map data)
    {
        return apiProxy.post(prepareUri("/track_collection/", new HashMap<String, Object>()), data, new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                Log.p("API: Saved the playlist '" + data.get("title") + "' - " + data.get("id"));

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest setPlaylist(final IStringResponseHandler handler, Integer id, final Map data)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.put(prepareUri("/track_collection/{id}", params), data, new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                Log.p("API: Saved the playlist '" + data.get("title") + "' - " + data.get("id"));

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest deletePlaylist(final IStringResponseHandler handler, final Integer id)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.delete(prepareUri("/track_collection/{id}", params), new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                Log.p("API: Deleted the playlist '" + id);

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public ConnectionRequest linkTrackToPlaylist(final IStringResponseHandler handler, final Integer id, final List tracks)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        return apiProxy.link(prepareUri("/track_collection/{id}", params), tracks, new IStringResponseHandler() {
            @Override
            public void onSuccess(Map<String, String> headers) {
                Log.p("API: Linked tracks to the playlist '" + id);

                if (handler != null) {
                    handler.onSuccess(headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }

    public Map getSearchCache(String search, ArrayList<String> filters) {
        if (search.equals(searchCacheTerm) && filters.toString().equals(searchCacheFilters)) {
            return searchCache;
        }

        return null;
    }

    public ConnectionRequest search(final IJsonResponseHandler handler, final String search, final ArrayList<String> filters)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("term", search);

        Map<String, Object> urlParams = null;
        if (filters.size() > 0) {
            urlParams = new HashMap<String, Object>();
            urlParams.put("content-type[]", filters.toArray(new String[filters.size()]));
        }

        return apiProxy.get(prepareUri("/search/{term}", params), urlParams, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded search for '" + search + "' - Results: " + data.size());

                searchCache = data;
                searchCacheFilters = filters.toString();
                searchCacheTerm =  search;

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }
    
    public ConnectionRequest suggest(final IJsonResponseHandler handler, final String search, ArrayList<String> filters)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("term", search);

        Map<String, Object> urlParams = null;
        if (filters.size() > 0) {
            urlParams = new HashMap<String, Object>();
            urlParams.put("content-type[]", filters.toArray(new String[filters.size()]));
        }

        return apiProxy.get(prepareUri("/suggest/{term}", params), urlParams, new IJsonResponseHandler() {
            @Override
            public void onSuccess(Map data, Map<String, String> headers) {
                Log.p("API: Loaded suggestions for '" + search + "' - Results: " + data.size());

                if (handler != null) {
                    handler.onSuccess(data, headers);
                }
            }

            @Override
            public void onFailure(int code, String message, Map<String, String> headers) {
                if (handler != null) {
                    handler.onFailure(code, message, headers);
                }
            }
        });
    }
    
    /**
     * Loop through the Map and use replace() to update the URL
     * according to the given parameters
     * @param uri
     * @param params
     * @return 
     */
    private String prepareUri(String uri, Map<String, Object> params)
    {
        if (params == null)
            params = new HashMap<String, Object>();

        for (Map.Entry entry : params.entrySet()) {
            String key = entry.getKey().toString();
            Object value = params.get(key);
           
            if (value instanceof Integer)
                value = value.toString();
            
            if (value instanceof String) {
                uri = StringUtil.replaceAll(uri, "{" + key + "}", Util.encodeUrl((String) value));
            }
        }

        return uri;
    }

    public void goOffline() {
        // Reset list of supported languages, the indicator for the app being online.
        Storage.getInstance().writeObject("API Base - supported languages", new ArrayList<String>());

        // Kill all running network-requests
        Enumeration e = NetworkManager.getInstance().enumurateQueue();
        while (e.hasMoreElements()) {
            ConnectionRequest r = (ConnectionRequest) e.nextElement();
            r.kill();
        }
    }

    /**
     * Being online indicates, that the API is ready to be used. It does not
     * indicate, that the user is authenticated and has a profile!
     * @return 
     */
    public Boolean isOnline() {
        return getSupportedLanguagesStorage() != null && getSupportedLanguagesStorage().size() > 0;
    }
    
    public boolean canDownload()
    {
        if (!isOnline())
            return false;

        if(!Storage.getInstance().exists("NetworkDownloadMobileData"))
            Storage.getInstance().writeObject("NetworkDownloadMobileData", false);
        
        int apType = NetworkManager.getInstance().getCurrentAccessPoint() != null ? NetworkManager.getInstance().getAPType(NetworkManager.getInstance().getCurrentAccessPoint()) : NetworkManager.ACCESS_POINT_TYPE_CABLE;
        
        return ((Boolean) Storage.getInstance().readObject("NetworkDownloadMobileData")) || apType == NetworkManager.ACCESS_POINT_TYPE_CABLE || apType == NetworkManager.ACCESS_POINT_TYPE_WLAN;
    }

    private ArrayList getSupportedLanguagesStorage()
    {
        return (ArrayList<String>)Storage.getInstance().readObject("API Base - supported languages");
    }
}
