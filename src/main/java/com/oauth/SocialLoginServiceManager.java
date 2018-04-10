package com.oauth;

import com.constants.Constants;
import com.servlet.ParsingUtils;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocialLoginServiceManager {
    private static String USER_HARDCODED_SESSION = "1234567890abcdefg";
    private static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    private static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";
    private static String REDIRECTION_URI_HARDCODED = "http://localhost:8080/SocialLoginBuddy/redirect?action=login";
    private static String GOOGLE_AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String GOOGLE_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
    private static String GOOGLE_USER_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";



    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint". Maybe?
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";


    // TODO: handle errors
    public static void createAuthorizationRequestURI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuilder authenticationURL = new StringBuilder();
            authenticationURL.append(GOOGLE_AUTHORIZATION_ENDPOINT).append("?")
                    .append("client_id=").append(CLIENT_ID_HARDCODED).append("&")
                    .append("response_type=code&")
                    .append("scope=").append(Constants.OPENID_SCOPE.getKey()).append("&")
                    .append("redirect_uri=").append(REDIRECTION_URI_HARDCODED).append("&")
                    .append("state=").append(USER_HARDCODED_SESSION);

            System.out.println(new StringBuffer("SocialLoginServiceManager:createAuthorizationRequestURI:: AuthenticationURL: ").append(authenticationURL));
            response.sendRedirect(authenticationURL.toString());
        } catch (IOException e) {
            System.out.println("YO! There was an error");
        }
    }


    public String POSTrequest_accessToken_IDtoken(String authorizationCode) {
        // TODO: DefaultHttpClient is deprecated :(
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(GOOGLE_TOKEN_ENDPOINT);
        try {
            // build POST request
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(Constants.CLIENT_ID.getKey(), CLIENT_ID_HARDCODED));
            urlParameters.add(new BasicNameValuePair(Constants.CLIENT_SECRET.getKey(), CLIENT_SECRET_HARDCODED));
            urlParameters.add(new BasicNameValuePair(Constants.REDIRECT_URI.getKey(), REDIRECTION_URI_HARDCODED));
            urlParameters.add(new BasicNameValuePair(Constants.GRANT_TYPE.getKey(), Constants.AUTHORIZATION_CODE.getKey()));
            urlParameters.add(new BasicNameValuePair(Constants.CODE.getKey(), authorizationCode));

            HttpEntity httpEntity = new UrlEncodedFormEntity(urlParameters);

            ByteArrayOutputStream httpEntityOutputStream = new ByteArrayOutputStream();
            httpEntity.writeTo(httpEntityOutputStream);
            httpPost.setEntity(httpEntity);
            System.out.println("SocialLoginServiceManager:POSTrequest_accessToken_IDtoken:: POST request http parameters: " + httpEntityOutputStream.toString());

            // POST request
            HttpResponse response = httpClient.execute(httpPost);
            String responseEntity = EntityUtils.toString(response.getEntity());

            System.out.println("SocialLoginServiceManager:POSTrequest_accessToken_IDtoken:: Response from provider = "  + responseEntity);
            System.out.println("SocialLoginServiceManager:POSTrequest_accessToken_IDtoken:: HTTP POST request response Code: " + response.getStatusLine().getStatusCode());
            return responseEntity;

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: add logger classes
            return "SocialLoginServiceManager:POSTrequest_accessToken_IDtoken:: creation of POST request failed";
        }
    }

    public String getAccessToken(String authorizationCode) {
        String providerResponse = POSTrequest_accessToken_IDtoken(authorizationCode);
        return ParsingUtils.parsePOSTproviderResponse(providerResponse);
    }

    public String getUserInfo(String accessToken) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(GOOGLE_USER_INFO_ENDPOINT);
        httpGet.setHeader("Authorization", "Bearer " + accessToken);
        System.out.println();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            System.out.println(new StringBuilder("SocialLoginServiceManager:getUserInfo:: GET request failed: ").append(e.getStackTrace()));
            return "Error getting user info!";
        }
    }
}
