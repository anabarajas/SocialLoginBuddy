package com.oauthflow;

import com.constants.Constants;
import com.model.Client;
import com.model.SlbClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class OauthFlowServiceManager {
    private static SlbClient slbClient;
    private static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    private static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";

    // TODO: encode this URI maybe to accept multiple providers?
    private static final String APP_USERINFO_REDIRECTION_URI = "http://ana.socialloginbuddy.ca.com:8080/SocialLoginBuddy/userinfo?action=login";
    private static final Logger LOGGER = Logger.getLogger(OauthFlowServiceManager.class);

    private static String GOOGLE_AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String GOOGLE_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
    private static String GOOGLE_USER_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint". Maybe?
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";

    public static void performAuthorizationRequest(HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuilder authenticationURL = new StringBuilder();
            authenticationURL.append(GOOGLE_AUTHORIZATION_ENDPOINT)
                    .append("?").append(Constants.CLIENT_ID.getKey())
                    .append("=").append(CLIENT_ID_HARDCODED)
                    .append("&").append(Constants.RESPONSE_TYPE.getKey())
                    .append("=").append(Constants.CODE.getKey())
                    .append("&").append(Constants.SCOPE.getKey())
                    .append("=").append(Constants.OPENID_SCOPE.getKey())
                    .append("&").append(Constants.REDIRECT_URI.getKey())
                    .append("=").append(APP_USERINFO_REDIRECTION_URI)
                    .append("&").append(Constants.STATE.getKey())
                    .append("=").append(SessionHandlingManager.getSlbClient().getSlbSession());
            LOGGER.info(new StringBuffer("performAuthorizationRequest - Authentication url: ").append(authenticationURL));
            response.sendRedirect(authenticationURL.toString());
        } catch (IOException e) {
            LOGGER.error(new StringBuffer("performAuthorizationRequest - failed URI redirection: ").append(e.getStackTrace()));
        }
    }

    public static String postRequest_clientTokens(String authorizationCode) {
        // TODO: DefaultHttpClient is deprecated :(
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(GOOGLE_TOKEN_ENDPOINT);
        try {
            // build POST request
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(Constants.CLIENT_ID.getKey(),
                    CLIENT_ID_HARDCODED));
            urlParameters.add(new BasicNameValuePair(Constants.CLIENT_SECRET.getKey(),
                    CLIENT_SECRET_HARDCODED));
            urlParameters.add(new BasicNameValuePair(Constants.REDIRECT_URI.getKey(),
                    APP_USERINFO_REDIRECTION_URI));
            urlParameters.add(new BasicNameValuePair(Constants.GRANT_TYPE.getKey(),
                    Constants.AUTHORIZATION_CODE.getKey()));
            urlParameters.add(new BasicNameValuePair(Constants.CODE.getKey(),
                    authorizationCode));

            HttpEntity httpEntity = new UrlEncodedFormEntity(urlParameters);

            ByteArrayOutputStream httpEntityOutputStream = new ByteArrayOutputStream();
            httpEntity.writeTo(httpEntityOutputStream);
            httpPost.setEntity(httpEntity);
            LOGGER.info(new StringBuffer("postRequest_clientTokens - POST request http parameters: ").append(httpEntityOutputStream.toString()));

            // POST request
            HttpResponse response = httpClient.execute(httpPost);
            String responseEntity = EntityUtils.toString(response.getEntity());

            LOGGER.info(new StringBuffer("postRequest_clientTokens - Response from provider: ").append(responseEntity));
            return responseEntity;

        } catch (IOException e) {
            LOGGER.error(new StringBuffer("postRequest_clientTokens -").append(e.getStackTrace()));
            return "";
        }
    }

    public static String performOAuthFlow(HttpServletRequest request) throws IOException {
        String userInfoString = "";
        String autoForm = "";
        slbClient = SessionHandlingManager.getSlbClient();
        slbClient.setAuthorizationCode(request.getParameter(Constants.CODE.getKey()));

        try{
            // Get client tokens
            String providerResponse = postRequest_clientTokens(slbClient.getAuthorizationCode());
            extractResponseClientTokens(providerResponse);

            // Get user info and encode it
            userInfoString = getUserInfo(slbClient.getAccessToken());
            String base64EncodedUserInformationJSONstring = new String(Base64.getEncoder().encode(userInfoString.getBytes()));

            // Build post for to client
            HttpSession session = request.getSession(false);
            LOGGER.info(new StringBuilder("performOAuthFlow - client slb session: ").append(session.getId()));
            Client client = (Client) session.getAttribute(slbClient.getSlbSession());
            autoForm = buildAutoForm(base64EncodedUserInformationJSONstring, client);
            LOGGER.info(new StringBuilder("performOAuthFlow - POST form to client: ").append(autoForm));
        } catch (Exception e) {
            LOGGER.warn(new StringBuilder("performOAuthFlow - ").append(e.getLocalizedMessage()));
        }
        finally {
            return autoForm;
        }
    }

    public static void extractResponseClientTokens(String providerResponse){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(providerResponse);

            // Get access_token
            String accessTokenValue = (String) jsonObject.get(Constants.ACCESS_TOKEN.getKey());
            slbClient.setAccessToken(accessTokenValue);

            // Get id_token
            String idTokenValue = (String) jsonObject.get(Constants.ID_TOKEN.getKey());
            slbClient.setIdToken(idTokenValue);
            LOGGER.info(new StringBuilder("extractResponseClientTokens - \bid_token in response is: ").append(idTokenValue)
                    .append("\baccess_token is: ").append(accessTokenValue));

        } catch (Exception e) {
            LOGGER.error(new StringBuilder("extractResponseClientTokens - JSON object parsing failed").append(e.getStackTrace()));
        }
    }

    public static String getUserInfo(String accessToken) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(GOOGLE_USER_INFO_ENDPOINT);
        httpGet.setHeader(Constants.AUTHORIZATION.getKey(), Constants.BEARER.getKey() + accessToken);
        try {
            // Execute get request
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String userInfoString = EntityUtils.toString(httpResponse.getEntity());

            LOGGER.info(new StringBuilder("getUserInfo - provider response:").append(userInfoString));
            return userInfoString;
        } catch (IOException e) {
            LOGGER.error(new StringBuffer("getUserInfo - GET request failed: ").append(e.getStackTrace()));
            return "Error getting user info!";
        }
    }

    public static String buildAutoForm(String base64EncodedUserInfoResponse, Client client) {
        return new StringBuilder(
                "<html>\n" +
                        "<HEAD>\n" +
                        "  <META HTTP-EQUIV='PRAGMA' CONTENT='NO-CACHE'>\n" +
                        "  <META HTTP-EQUIV='CACHE-CONTROL' CONTENT='NO-CACHE'>\n" +
                        "  <TITLE>Social Login Buddy Auto-Form POST</TITLE>\n" +
                        "</HEAD>\n" +
                        "<body onLoad=\"document.forms[0].submit()\">\n" +
                        "<NOSCRIPT>Your browser does not support JavaScript.  Please click the 'Continue' button below to proceed. <br><br></NOSCRIPT>\n" +
                        "<form action=\"").append(client.getRedirectUri()).append("\" method=\"POST\">\n" +
                "<input type=\"hidden\" name=\"userinforesponse\" value=\"").append(base64EncodedUserInfoResponse).append("\">\n" +
                "<input type=\"hidden\" name=\"id_token\" value=\"").append(SessionHandlingManager.getSlbClient().getIdToken()).append("\">\n" +
                "<input type=\"hidden\" name=\"state\" value=\"").append(client.getState()).append("\">\n" +
                "<NOSCRIPT>\n" +
                "  <INPUT TYPE=\"SUBMIT\" VALUE=\"Continue\">\n" +
                "</NOSCRIPT>\n" +
                "      </form>\n" +
                "   </body>\n" +
                "</html>").toString();
    }
}
