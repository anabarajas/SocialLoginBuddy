package com.oauthflow;

import com.constants.Constants;
import com.model.Client;
import com.model.SlbClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class SessionHandlingManager {

    private static final Logger LOGGER = Logger.getLogger(SessionHandlingManager.class);
    private static SlbClient slbClient;

    public static void persistClientQueryParameters(HttpServletRequest request) throws UnsupportedEncodingException,ServletException, IOException {

        // Create slb and httpSession for client

        HttpSession httpSession = request.getSession();
        slbClient = new SlbClient();
        slbClient.setSlbSession(httpSession.getId());

        // Decode client provided redirect_uri
        String queryString= URLDecoder.decode(request.getQueryString(), Constants.UTF_8.getKey());

        if (queryString != null && !queryString.equals("")){
            try {
                // persist new client in http session
                String redirectUri = request.getParameter(Constants.REDIRECT_URI.getKey());
                Client client = new Client(redirectUri, request.getParameter(Constants.STATE.getKey()));
                httpSession.setAttribute(slbClient.getSlbSession(), client);

                LOGGER.info(new StringBuilder("persistClientQueryParameters - \nDecoded client redirect uri: ").append(redirectUri)
                        .append("\nNew clientSLB state:").append(slbClient.getSlbSession()));

            } catch (Exception e) {
                throw new IllegalArgumentException("persistClientQueryParameters - No redirect_uri or state provided by client");
            }
        } else {
            LOGGER.fatal(new StringBuilder("persistClientQueryParameters - No query string from client").append(request.getQueryString()));
        }
    }
    // TODO: ask sasha --> / how to get rid of SocialLoginBuddy --> webannotation @webserlet?
    public static SlbClient getSlbClient() {
        return slbClient;
    }
}
