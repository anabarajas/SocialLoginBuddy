package com.servlets;

import com.constants.Constants;
import com.oauthflow.SocialLoginServiceManager;
import com.utils.ParsingUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;

public class OauthAuthorizationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OauthAuthorizationServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOGGER.info(new StringBuilder("doGet:: Response URL from provider: ").append(request.getQueryString()));
        try {
            if (request.getQueryString() == null) {
                throw new MalformedURLException("doGet:: null response URL from service provider");
            } else if (request.getQueryString().equals("")) {
                throw new IllegalArgumentException("doGet:: empty response from service provider");
            } else {
                // Get authorization code from provider URL response
                String authorizationCode = request.getParameter(Constants.CODE.getKey());
                LOGGER.info(new StringBuilder("doGet:: code is: ").append(authorizationCode));
                performOAuthFlow(authorizationCode, response);
            }
        } catch (Exception e) {
         LOGGER.warn(new StringBuilder("doGet:: failed to obtain authorization code from provider").append(e.getStackTrace()));
        }
    }

    private void performOAuthFlow(String authorizationCode, HttpServletResponse response) throws IOException {
        String userInformationJSONstring = "";
        try{
            // Get access token
            SocialLoginServiceManager socialLoginServiceManager = new SocialLoginServiceManager();
            String accessToken = socialLoginServiceManager.getAccessToken(authorizationCode);
            // Get user info
            if (accessToken != null || !accessToken.equals("")) {
                userInformationJSONstring = socialLoginServiceManager.getUserInfo(accessToken);
            }
        } catch (Exception e) {
            LOGGER.warn(new StringBuilder("performOAuthFlow:: ").append(e.getStackTrace()));
        }
        response.getWriter().println(userInformationJSONstring);
    }
}
