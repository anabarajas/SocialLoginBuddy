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
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Base64;
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

    private String encodeClientInfoResponseRedirectURI(String encodedUserInformationJSONstring) {

        StringBuilder clientProvidedRedirectURI = new StringBuilder(SessionHandlingManager.getClientRedirectUri());

        clientProvidedRedirectURI.append("&")
                    .append(Constants.USER_INFO.getKey()).append("=")
                    .append(encodedUserInformationJSONstring).append("&")
                    .append(Constants.ACCESS_TOKEN.getKey()).append("=")
                    .append(SessionHandlingManager.getClientAccessToken()).append("&")
                    .append(Constants.ID_TOKEN.getKey()).append("=")
                    .append(SessionHandlingManager.getClientIdToken());
        return clientProvidedRedirectURI.toString();
    }

    private void performOAuthFlow(String authorizationCode, HttpServletResponse response) throws IOException {
        String clientRedirectUri = "";
        String userInformationJSONstring = "";
        String autoForm = "";
        try{
            // Get access token
            SocialLoginServiceManager socialLoginServiceManager = new SocialLoginServiceManager();
            HashMap<Constants, String> clientTokens= socialLoginServiceManager.getClientTokens(authorizationCode);
            SessionHandlingManager.persistClientTokens(clientTokens);

            if (clientTokens.size() < 2){
                throw new IllegalArgumentException("performOAuthFlow:: Missing fields in provider response");
            }
            // Get user info and encoded
            userInformationJSONstring = socialLoginServiceManager.getUserInfo(clientTokens.get(Constants.ACCESS_TOKEN));

            // Encodings of user information
            String URLencodedUserInformationJSONstring = URLEncoder.encode(userInformationJSONstring, Constants.UTF_8.getKey());
            String base64EncodedUserInformationJSONstring = new String(Base64.getEncoder().encode(userInformationJSONstring.getBytes()));
            //SessionHandlingManager.persistUserInfoJson(userInformationJSONstring);

            // Build client redirect url
            clientRedirectUri = encodeClientInfoResponseRedirectURI(userInformationJSONstring);

            // Build post for to client
            autoForm = buildAutoForm(base64EncodedUserInformationJSONstring, clientTokens);
            LOGGER.info(new StringBuilder("performOAuthFlow:: POST form to client: ").append(autoForm));
        } catch (Exception e) {
            LOGGER.warn(new StringBuilder("performOAuthFlow:: ").append(e.getStackTrace()));
        }
        response.getWriter().println(autoForm);
     //   response.sendRedirect(clientRedirectUri);
    }

    private String buildAutoForm(String base64EncodedUserInfoResponse, HashMap<Constants, String> clientTokens) {
        return new StringBuilder(
                "<html>\n" +
                        "<HEAD>\n" +
                        "  <META HTTP-EQUIV='PRAGMA' CONTENT='NO-CACHE'>\n" +
                        "  <META HTTP-EQUIV='CACHE-CONTROL' CONTENT='NO-CACHE'>\n" +
                        "  <TITLE>Social Login Buddy Auto-Form POST</TITLE>\n" +
                        "</HEAD>\n" +
                        "<body onLoad=\"document.forms[0].submit()\">\n" +
                        "<NOSCRIPT>Your browser does not support JavaScript.  Please click the 'Continue' button below to proceed. <br><br></NOSCRIPT>\n" +
                        "<form action=\"").append(SessionHandlingManager.getClientRedirectUri()).append("\" method=\"POST\">\n" +
                "<input type=\"hidden\" name=\"userinforesponse\" value=\"").append(base64EncodedUserInfoResponse).append("\">\n" +
                "<input type=\"hidden\" name=\"id_token\" value=\"").append(clientTokens.get(Constants.ID_TOKEN)).append("\">\n" +
                //          "<input type=\"hidden\" name=\"state \" value=\"${state}\">\n" +
                "<NOSCRIPT>\n" +
                "  <INPUT TYPE=\"SUBMIT\" VALUE=\"Continue\">\n" +
                "</NOSCRIPT>\n" +
                "      </form>\n" +
                "   </body>\n" +
                "</html>").toString();
    }
}
