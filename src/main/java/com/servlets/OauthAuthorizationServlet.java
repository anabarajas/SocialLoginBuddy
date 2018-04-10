package com.servlets;

import com.oauthflow.SocialLoginServiceManager;
import com.utils.ParsingUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OauthAuthorizationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OauthAuthorizationServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userInformationJSONstring = "";
        if (request.getQueryString() == null) {
            // TODO: handle error properly
            LOGGER.info("doGet:: null redirection URI from service provider");
        } else if (request.getQueryString() == "") {
            LOGGER.info("doGet:: no redirection URI from service provider");
        } else {
            // Get authorization code
            String redirectURLqueryString = request.getQueryString();
            LOGGER.info(new StringBuilder("doGet:: RedirectURI sent from Google: ").append(redirectURLqueryString));
            String authorizationCode = ParsingUtils.parseRedirectURL(redirectURLqueryString);
            if (authorizationCode != null) {
                // Get access token
                SocialLoginServiceManager socialLoginServiceManager = new SocialLoginServiceManager();
                String accessToken = socialLoginServiceManager.getAccessToken(authorizationCode);
                // Get user info
                if (accessToken != null || !accessToken.equals("")) {
                    userInformationJSONstring = socialLoginServiceManager.getUserInfo(accessToken);
                }
            }
        }
        response.getWriter().println(userInformationJSONstring);
    }

}
