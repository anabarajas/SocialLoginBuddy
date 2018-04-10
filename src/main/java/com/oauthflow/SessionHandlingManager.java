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
        String clientQueryString = request.getQueryString();
        LOGGER.info(new StringBuilder("persistSession:: client query string is: ").append(clientQueryString));
        HashMap<Constants, String> clientURIparameters = ParsingUtils.extractClientURIparameters(clientQueryString);
        String clientRedirectURI = clientURIparameters.get(Constants.REDIRECT_URI);
        String clientState = clientURIparameters.get(Constants.STATE);
        // TODO: handle errors

        return "";
    }
}
