package com.oauthflow;

import com.constants.Constants;
import com.utils.ParsingUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SessionHandlingManager {

    private static final Logger LOGGER = Logger.getLogger(SessionHandlingManager.class);

    public static String persistSession(HttpServletRequest request) throws ServletException, IOException {
        String clientURLQueryString = request.getQueryString();

        String client_redirect_uri = request.getParameter(Constants.REDIRECT_URI.getKey());
        String client_session = request.getParameter(Constants.STATE.getKey());
        LOGGER.info(new StringBuilder("persistSession:: client redirect uri: ").append(client_redirect_uri).append(", \n client session: ").append(client_session));

        // TODO: ask sasha --> / how to get rid of SocialLoginBuddy

        return "";
    }
}
