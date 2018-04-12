package com.servlets;

import com.constants.Constants;
import com.oauthflow.SessionHandlingManager;
import com.oauthflow.SocialLoginServiceManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;

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

    public String encodeResponseInClientRedirectURI(String userInformationJSONstring) {

        StringBuilder clientProvidedRedirectURI = new StringBuilder(SessionHandlingManager.getClientRedirectUri());

        try{
            clientProvidedRedirectURI.append("&")
                    .append(Constants.USER_INFO.getKey()).append("=")
                    .append(URLEncoder.encode(userInformationJSONstring, "UTF-8")).append("&")
                    .append(Constants.ACCESS_TOKEN.getKey()).append("=")
                    .append(SessionHandlingManager.getClientAccessToken()).append("&")
                    .append(Constants.ID_TOKEN.getKey()).append("=")
                    .append(SessionHandlingManager.getClientIdToken());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(new StringBuilder("encodeResponseInClientRedirectURI:: ").append(e.getStackTrace()));
        }
        return clientProvidedRedirectURI.toString();
    }

    private void performOAuthFlow(String authorizationCode, HttpServletResponse response) throws IOException {
        String clientURI = "";
        String userInformationJSONstring = "";
        try{
            // Get access token
            SocialLoginServiceManager socialLoginServiceManager = new SocialLoginServiceManager();
            HashMap<Constants, String> clientTokens= socialLoginServiceManager.getClientTokens(authorizationCode);
            SessionHandlingManager.persistClientTokens(clientTokens);

            if (clientTokens.size() < 2){
                throw new IllegalArgumentException("performOAuthFlow:: Missing fields in provider response");
            }
            // Get user info
            userInformationJSONstring = socialLoginServiceManager.getUserInfo(clientTokens.get(Constants.ACCESS_TOKEN));
            SessionHandlingManager.persistUserInfoJson(userInformationJSONstring);
            clientURI = encodeResponseInClientRedirectURI(userInformationJSONstring);

        } catch (Exception e) {
            LOGGER.warn(new StringBuilder("performOAuthFlow:: ").append(e.getStackTrace()));
        }
        response.sendRedirect(clientURI);
    }
}
