package com.oauthflow;

import com.constants.Constants;
import com.model.Client;
import com.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandlingManager {

    private static final Logger LOGGER = Logger.getLogger(SessionHandlingManager.class);
    private static String CLIENT_REDIRECT_URI = "";
    private static String CLIENT_ACCESS_TOKEN = "";
    private static String CLIENT_ID_TOKEN = "";
    private static String CLIENT_USER_INFO = "";
    private static String CLIENT_STATE = "";
    private static String CLIENT_OTHER_PARAMS = "";
    private static String CLIENT_SLB_STATE = "";

    public static void persistClientQueryParameters(HttpServletRequest request) throws UnsupportedEncodingException,ServletException, IOException {

        // Create session for client
        HttpSession httpSession = request.getSession();
        CLIENT_SLB_STATE = httpSession.getId();

        // Decode client provided redirect_uri
        String queryString= URLDecoder.decode(request.getQueryString(), Constants.UTF_8.getKey());

        if (queryString != null && !queryString.equals("")){
            try {
                CLIENT_REDIRECT_URI = request.getParameter(Constants.REDIRECT_URI.getKey());
                CLIENT_STATE = request.getParameter(Constants.STATE.getKey());

                // persist new client in http session
                Client client = new Client(CLIENT_REDIRECT_URI, CLIENT_STATE);
                httpSession.setAttribute(CLIENT_SLB_STATE, client);

                LOGGER.info(new StringBuilder("persistClientQueryParameters - decoded client redirect uri: ").append(CLIENT_REDIRECT_URI)
                        .append("\n clientSLB state:").append(CLIENT_SLB_STATE));

            } catch (Exception e) {
                throw new IllegalArgumentException("persistClientQueryParameters:: no redirect_uri or state provided by client");
            }
        } else {
            LOGGER.fatal(new StringBuilder("persistClientQueryParameters - no query string from client").append(request.getQueryString()));
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

    public static String getClientSlbState() {
        return CLIENT_SLB_STATE;
    }

    public static String getClientOtherParams() {
        return CLIENT_OTHER_PARAMS;
    }
}
