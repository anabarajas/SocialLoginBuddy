package com.oauthflow;

import com.constants.Constants;
import com.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class SessionHandlingManager {

    private static final Logger LOGGER = Logger.getLogger(SessionHandlingManager.class);
    protected static String CLIENT_REDIRECT_URI = "";
    protected static String CLIENT_ACCESS_TOKEN = "";
    protected static String CLIENT_ID_TOKEN = "";
    protected static String CLIENT_USER_INFO = "";
    protected static String CLIENT_STATE = "";
    protected static String CLIENT_OTHER_PARAMS = "";


    public static void persistClientRedirectUriQueryParameters(HttpServletRequest request) throws UnsupportedEncodingException,ServletException, IOException {
        String queryString= URLDecoder.decode(request.getQueryString(), Constants.UTF_8.getKey());
            if (queryString != null && !queryString.equals("")){
                HashMap<Constants, String> queryParameters = Utils.extractQueryParameters(queryString);
                CLIENT_REDIRECT_URI = queryParameters.get(Constants.REDIRECT_URI);
                CLIENT_STATE = queryParameters.get(Constants.STATE);

                // check for extra parameters in client redirect URI
                if (queryParameters.containsKey(Constants.OTHER_PARAMETERS)) {
                    CLIENT_OTHER_PARAMS = queryParameters.get(Constants.OTHER_PARAMETERS);
                }
                LOGGER.info(new StringBuilder("persistClientRedirectUriQueryParameters:: decoded client redirect uri: ").append(CLIENT_REDIRECT_URI));
            } else {
                LOGGER.fatal(new StringBuilder("persistClientRedirectUriQueryParameters:: no query string from client").append(request.getQueryString()));
            }
    }
    // TODO: ask sasha --> / how to get rid of SocialLoginBuddy
    public static void persistClientTokens(HashMap<Constants, String> clientTokens) {
        CLIENT_ACCESS_TOKEN = clientTokens.get(Constants.ACCESS_TOKEN);
        CLIENT_ID_TOKEN = clientTokens.get(Constants.ID_TOKEN);
    }

    public static void persistUserInfoJson(String userInformationJSONstring) {
        CLIENT_USER_INFO = userInformationJSONstring;
    }

    public static String getClientRedirectUri() {
        return CLIENT_REDIRECT_URI;
    }

    public static String getClientAccessToken() {
        return CLIENT_ACCESS_TOKEN;
    }

    public static String getClientIdToken() {
        return CLIENT_ID_TOKEN;
    }

    public static String getClientUserInfo() {
        return CLIENT_USER_INFO;
    }

    public static String getClientState() {
        return CLIENT_STATE;
    }

    public static String getClientOtherParams() {
        return CLIENT_OTHER_PARAMS;
    }
}
