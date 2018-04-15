package com.oauthflow;

import com.constants.Constants;
import com.utils.Utils;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OauthFlowServiceManager {
    private static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    private static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";

    private static final String APP_USERINFO_REDIRECTION_URI = "http://ana.socialloginbuddy.ca.com:8080/SocialLoginBuddy/userinfo?provider=Google&action=login";

    private static String GOOGLE_AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String GOOGLE_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
    private static String GOOGLE_USER_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    private static final Logger LOGGER = Logger.getLogger(OauthFlowServiceManager.class);

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
                    .append("=").append(SessionHandlingManager.getClientSlbState());
            LOGGER.info(new StringBuffer("performAuthorizationRequest - Authentication url: ").append(authenticationURL));
            response.sendRedirect(authenticationURL.toString());
        } catch (IOException e) {
            LOGGER.error(new StringBuffer("performAuthorizationRequest - failed URI redirection: ").append(e.getStackTrace()));
        }
    }


    public String postRequest_accessToken_idToken(String authorizationCode) {
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
            LOGGER.info(new StringBuffer("postRequest_accessToken_idToken - POST request http parameters: ").append(httpEntityOutputStream.toString()));

            // POST request
            HttpResponse response = httpClient.execute(httpPost);
            String responseEntity = EntityUtils.toString(response.getEntity());

            LOGGER.info(new StringBuffer("postRequest_accessToken_idToken - Response from provider = ").append(responseEntity));
            LOGGER.info(new StringBuffer("postRequest_accessToken_idToken - HTTP POST request response Code: ").append(response.getStatusLine().getStatusCode()));
            return responseEntity;

        } catch (IOException e) {
            LOGGER.error(new StringBuffer("postRequest_accessToken_idToken -").append(e.getStackTrace()));
            return "OauthFlowServiceManager:postRequest_accessToken_idToken - creation of POST request failed";
        }
    }

    public HashMap<Constants, String> getClientTokens(String authorizationCode) {
        String providerResponse = postRequest_accessToken_idToken(authorizationCode);
        return Utils.parsePostProviderResponse(providerResponse);
    }

    public String getUserInfo(String accessToken) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(GOOGLE_USER_INFO_ENDPOINT);
        httpGet.setHeader(Constants.AUTHORIZATION.getKey(), Constants.BEARER.getKey() + accessToken);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String userInfoJSONResponseString = EntityUtils.toString(httpResponse.getEntity());
            LOGGER.info(new StringBuilder("getUserInfo - provider response:").append(userInfoJSONResponseString));
            return userInfoJSONResponseString;
        } catch (IOException e) {
            LOGGER.error(new StringBuffer("getUserInfo - GET request failed: ").append(e.getStackTrace()));
            return "Error getting user info!";
        }
    }

}
