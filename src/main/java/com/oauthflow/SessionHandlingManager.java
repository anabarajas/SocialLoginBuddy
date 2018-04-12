package com.oauthflow;

import com.constants.Constants;
import com.utils.ParsingUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class SessionHandlingManager {

    private static final Logger LOGGER = Logger.getLogger(SessionHandlingManager.class);
    protected static String CLIENT_REDIRECT_URI = "";
    protected static String CLIENT_ACCESS_TOKEN = "";
    protected static String CLIENT_ID_TOKEN = "";
    protected static String CLIENT_USER_INFO = "";

    public static void persistClientProvidedRedirectURI(HttpServletRequest request) throws ServletException, IOException {
        String queryString= request.getQueryString();
        if (queryString != null && !queryString.equals("")){
            CLIENT_REDIRECT_URI = ParsingUtils.convertQueryStringToResponseURI(queryString);
            LOGGER.info(new StringBuilder("persistClientProvidedRedirectURI:: client redirect uri: ").append(CLIENT_REDIRECT_URI));
            // TODO: ask sasha --> / how to get rid of SocialLoginBuddy
        } else {
            LOGGER.fatal(new StringBuilder("persistClientProvidedRedirectURI:: client query string is: ").append(request.getQueryString()));
            throw new IllegalArgumentException("persistClientProvidedRedirectURI:: no query string from client!");

        }
    }

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
}
