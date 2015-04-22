package com.codenameone.music.api;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.processing.Result;
import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codenameone.music.Api;
import com.codenameone.music.MediaHelper;
import com.codenameone.music.UserProfile;
import com.codenameone.music.view.DialogConfirm;
import com.codenameone.music.view.LoginView;
import com.codenameone.music.view.ToastView;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class Proxy {

    private final Map<String, ArrayList<IResponseHandler>> events;
    private final Map<String, ConnectionRequest> requests;

    private final String baseUri = "https://some-random-api.com";

    public Proxy()
    {
        events = new HashMap<String, ArrayList<IResponseHandler>>();
        requests = new HashMap<String, ConnectionRequest>();
    }

    private String buildQueryStringParameter(String key, String val)
    {
        return Util.encodeUrl(key) + "=" + Util.encodeUrl(val);
    }

    private String buildQueryString(Map<String, Object> parameters)
    {
        String queryString = "";
        for (String key : parameters.keySet()) {
            if (parameters.get(key) instanceof String) {
                String value = (String)parameters.get(key);
                queryString += "&" + buildQueryStringParameter(key, value);
            } else {
                String[] values = (String[])parameters.get(key);
                for (String value : values) {
                    queryString += "&" + buildQueryStringParameter(key, value);
                }
            }
        }

        if (!queryString.equals("")) {
            queryString = queryString.substring(1);
        }

        return queryString;
    }

    public ConnectionRequest get(String uri, final Map<String, Object> parameters, IResponseHandler responseHandler)
    {
        if (parameters != null) {
            String queryString = buildQueryString(parameters);
            
            if (!queryString.equals("")) {
                uri += "?" + queryString;
            }
        }

        return connect(baseUri + uri, null, new IRequestModifier() {

            @Override
            public ConnectionRequest modify(ConnectionRequest request) {
                request.setPost(false);

                return request;
            }
        }, responseHandler);
    }

    public ConnectionRequest post(String uri, Map<String, String> parameters, IResponseHandler responseHandler)
    {
        return connect(baseUri + uri, parameters, new IRequestModifier() {

            @Override
            public ConnectionRequest modify(ConnectionRequest request) {
                request.setPost(true);

                return request;
            }
        }, responseHandler);
    }


    public ConnectionRequest put(String uri, Map data, IStringResponseHandler responseHandler) {
        return connect(baseUri + uri, data, new IRequestModifier() {

            @Override
            public ConnectionRequest modify(ConnectionRequest request) {
                request.setPost(true);
                request.setHttpMethod("PUT");

                return request;
            }
        }, responseHandler);
    }

    public ConnectionRequest delete(String uri, IStringResponseHandler responseHandler) {
        return connect(baseUri + uri, null, new IRequestModifier() {

            @Override
            public ConnectionRequest modify(ConnectionRequest request) {
                request.setPost(false);
                request.setHttpMethod("DELETE");

                return request;
            }
        }, responseHandler);
    }

    public ConnectionRequest link(String uri, final List<Map> tracks, IStringResponseHandler responseHandler) {
        return connect(baseUri + uri, null, new IRequestModifier() {

            @Override
            public ConnectionRequest modify(ConnectionRequest request) {
                request.setPost(true);

                List<String> trackLinks = new ArrayList<String>();
                for (Map track : tracks)
                {
                    trackLinks.add("</track/" + MediaHelper.getId(track) + ">");
                }

                StringBuilder builder = new StringBuilder();
                builder.append(trackLinks.remove(0));

                for (String s : trackLinks) {
                    builder.append( ", ");
                    builder.append( s);
                }

                request.addRequestHeader("Link", builder.toString());
                request.addRequestHeader("Accept-Language", Api.getInstance().getLanguage());
                request.addRequestHeader("X-HTTP-METHOD-OVERRIDE", "LINK");

                return request;
            }
        }, responseHandler);
    }

    private ConnectionRequest connect(final String uri, final Map<String, String> parameters, final IRequestModifier modifier, final IResponseHandler responseHandler)
    {
        Boolean singleCall = !events.containsKey(uri);
        
        ArrayList<IResponseHandler> eventsArrayList = events.get(uri);
        if (eventsArrayList == null)
        {
            eventsArrayList = new ArrayList();
        }

        eventsArrayList.add(responseHandler);
        events.put(uri, eventsArrayList);

        // If this is the first call, just execute it. If another call comes in
        // and the first one isn't handled, serve both.
        if (singleCall)
        {
            Log.p("API loading " + uri);

            ConnectionRequest request = new ConnectionRequest() {
                
                private Map<String, String> headers = new HashMap<String, String>();

                @Override
                protected void buildRequestBody(OutputStream os) throws IOException {
                    if (parameters != null) {
                        Result val = Result.fromContent(parameters);
                        if(shouldWriteUTFAsGetBytes()) {
                            os.write(val.toString().getBytes("UTF-8"));
                        } else {
                            OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
                            w.write(val.toString());
                        }
                    }
                }

                @Override
                protected void readHeaders(Object connection) throws IOException {
                    String[] headerNames = getHeaderFieldNames(connection);

                    for(String headerName : headerNames) {
                        headers.put(headerName, getHeader(connection, headerName));
                    }
                }

                @Override
                protected void readErrorCodeHeaders(Object connection) throws IOException {
                    String[] headerNames = getHeaderFieldNames(connection);

                    for(String headerName : headerNames) {
                        headers.put(headerName, getHeader(connection, headerName));
                    }
                }

                @Override
                protected void readResponse(InputStream input) throws IOException {
                    ArrayList<IResponseHandler> eventsArrayList = events.get(uri);
                    events.remove(uri);

                    final String sResponse = Util.readToString(input);

                    for (final IResponseHandler responseHandler : eventsArrayList)
                    {
                        if (responseHandler instanceof IJsonResponseHandler) {
                            JSONParser parser = new JSONParser();
                            CharArrayReader charArrayReader = new CharArrayReader(sResponse.toCharArray());
                            final Map data = parser.parseJSON(charArrayReader);
                            Display.getInstance().callSerially(new Runnable() {
                                @Override
                                public void run() {
                                    IJsonResponseHandler tmpResponseHandler = (IJsonResponseHandler) responseHandler;
                                    tmpResponseHandler.onSuccess(data, headers);
                                }
                            });
                        } else if (responseHandler instanceof IStringResponseHandler) {
                            Display.getInstance().callSerially(new Runnable() {
                                @Override
                                public void run() {
                                    IStringResponseHandler tmpResponseHandler = (IStringResponseHandler) responseHandler;
                                    tmpResponseHandler.onSuccess(headers);
                                }
                            });
                        } else {
                            throw new IOException("Response-Interface is not supported");
                        }
                    }
                }

                @Override
                protected void handleException(Exception err) {
                    //TODO: think if we need to do something here. It seems that if i use a toastview here on iOS the app crashes
                }

                @Override
                protected void handleRuntimeException(RuntimeException err) {
                    //TODO: think if we need to do something here. It seems that if i use a toastview here on iOS the app crashes
                }

                @Override
                protected void handleErrorResponseCode(final int code, final String message) {
                    Display.getInstance().callSerially(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<IResponseHandler> eventsArrayList = events.get(uri);
                            events.remove(uri);

                            if (!LoginView.FORM_NAME.equals(Display.getInstance().getCurrent().getName())
                                    && (code == 401 || code == 302)) {
                                StateMachine.getInstance().loginView.show();
                                return;
                            }

                            if(eventsArrayList != null && !eventsArrayList.isEmpty())
                            {
                                for (IResponseHandler responseHandler : eventsArrayList) {
                                    responseHandler.onFailure(code, message, headers);
                                }
                            }
                        }
                    });
                }

                /**
                 * Try to get around an Android specific behavior ...
                 * @see: http://code.google.com/p/codenameone/issues/detail?id=646
                 * @see: http://stackoverflow.com/questions/10431202/java-io-ioexception-received-authentication-challenge-is-null?lq=1
                 * @see: http://stackoverflow.com/questions/11810447/httpurlconnection-worked-fine-in-android-2-x-but-not-in-4-1-no-authentication-c
                 * @TODO: Find another solution - not based on an exception-message
                 */
                @Override
                protected void handleIOException(final IOException err) {
                    Display.getInstance().callSerially(new Runnable() {
                        @Override
                        public void run() {
                            events.remove(uri);

                            if (err.getMessage() != null && err.getMessage().indexOf("authentication challenge") > -1) {
                                handleErrorResponseCode(401, "Unauthorized");
                            } else {
                                String dialogText;
                                String buttonText;
                                if(Api.getInstance().isOnline())
                                {
                                    dialogText = StateMachine._translate("dialog_bad_connection", "[default] The internet connection is bad. Do you want to go offline?");
                                    buttonText = StateMachine._translate("button_yes", "[default] Yes");
                                }
                                else
                                {
                                    dialogText = StateMachine._translate("offline_error", "[default] There is no internet connection available, please try again later");
                                    buttonText = StateMachine._translate("button_ok", "[default] OK");
                                }
                                
                                // If you want to go offline, do so. Otherwise, try again.
                                DialogConfirm.show(dialogText, new Command(buttonText) {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        //StateMachine.goOffline();
                                    }
                                }, new Command(StateMachine._translate("command_dialog_retry", "[default] Retry")) {

                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        //connect(uri, parameters, modifier, responseHandler);
                                    }
                                });
                            }
                        }
                    });
                }

                public void kill() {
                    super.kill();

                    requests.remove(uri);
                    events.remove(uri);
                }
            };

            if (parameters != null) {
                request.addRequestHeader("Content-Type", "application/json");
            }

            UserProfile profile = Api.getInstance().getProfile();
            if (profile != null && profile.getToken() != null) {
                String credentials = Api.getInstance().getProfile().getUsername() + ":" + Api.getInstance().getProfile().getToken();

                request.addRequestHeader("Authorization", "Basic " + Base64.encodeNoNewline((credentials).getBytes()));
            }

            request.setUrl(uri);
            request.addRequestHeader("Accept", "application/json");
            request.addRequestHeader("Accept-Language", Api.getInstance().getLanguage());
            request.setCookiesEnabled(false);

            if (modifier != null) {
                request = modifier.modify(request);
            }

            NetworkManager.getInstance().addToQueue(request);

            requests.put(uri, request);

            return request;
        } else {
            Log.p("API Queue " + uri);

            return requests.get(uri);
        }
    }
    
    private interface IRequestModifier {
        public ConnectionRequest modify(ConnectionRequest request);
    }
}