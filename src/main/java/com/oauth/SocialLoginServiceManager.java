package com.oauth;

import com.servlet.ParsingUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

public class AuthenticationRequest {
    private static String USER_HARDCODED_SESSION = "1234567890abcdefg";
    private static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    private static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";
    private static String REDIRECTION_URI_HARDCODED = "http://localhost:8080/SocialLoginBuddy/redirect?action=login";
    private static String SCOPE_HARDCODED = "openid%20profile%20email&";
    private static String GOOGLE_AUTHORIZATION_URI_HARDCODED = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String GOOGLE_TOKEN_URI_HARDCODED = "https://www.googleapis.com/oauth2/v4/token";

    private static String CODE="code";
    private static String CLIENT_ID="client_id";
    private static String CLIENT_SECRET="client_secret";
    private static String REDIRECT_URI="redirect_uri";
    private static String GRANT_TYPE="grant_type";

    private static String AUTHORIZATION_CODE = "authorization_code";


    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint". Maybe?
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";


    // TODO: handle errors
    public static void createAuthorizationURI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuilder authenticationURL = new StringBuilder();
            authenticationURL.append(GOOGLE_AUTHORIZATION_URI_HARDCODED).append("?")
                    .append("client_id=").append(CLIENT_ID_HARDCODED).append("&")
                    .append("response_type=code&")
                    .append("scope=").append(SCOPE_HARDCODED).append("&")
                    .append("redirect_uri=").append(REDIRECTION_URI_HARDCODED).append("&")
                    .append("state=").append(USER_HARDCODED_SESSION);

            System.out.println(new StringBuffer("AuthenticationRequest:createAuthorizationURI:: AuthenticationURL: ").append(authenticationURL));
            response.sendRedirect(authenticationURL.toString());
        } catch (IOException e) {
            System.out.println("YO! There was an error");
        }
    }


    public String POSTrequest_accessToken_IDtoken(String authorizationCode) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(GOOGLE_TOKEN_URI_HARDCODED);
        try {
            // build POST request
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(CLIENT_ID, CLIENT_ID_HARDCODED));
            urlParameters.add(new BasicNameValuePair(CLIENT_SECRET, CLIENT_SECRET_HARDCODED));
            urlParameters.add(new BasicNameValuePair(REDIRECT_URI, REDIRECTION_URI_HARDCODED));
            urlParameters.add(new BasicNameValuePair(GRANT_TYPE, AUTHORIZATION_CODE));
            urlParameters.add(new BasicNameValuePair(CODE, authorizationCode));

            HttpEntity httpEntity = new UrlEncodedFormEntity(urlParameters);

            ByteArrayOutputStream httpEntityOutputStream = new ByteArrayOutputStream();
            httpEntity.writeTo(httpEntityOutputStream);
            httpPost.setEntity(httpEntity);
            System.out.println("AuthenticationRequest:POSTrequest_accessToken_IDtoken:: POST request http parameters: " + httpEntityOutputStream.toString());

            // POST request
            HttpResponse response = httpClient.execute(httpPost);
            String responseEntity = EntityUtils.toString(response.getEntity());

            System.out.println("AuthenticationRequest:POSTrequest_accessToken_IDtoken:: Response from provider = "  + responseEntity);
            System.out.println("AuthenticationRequest:POSTrequest_accessToken_IDtoken:: HTTP POST request response Code: " + response.getStatusLine().getStatusCode());
            return responseEntity;

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: add logger classes
            return "AuthenticationRequest:POSTrequest_accessToken_IDtoken:: creation of POST request failed";
        }
    }



    // TODO: Add try-catches
    public String getAccessToken(String authorizationCode) {
        String providerResponse = POSTrequest_accessToken_IDtoken(authorizationCode);
        return ParsingUtils.parsePOSTproviderResponse(providerResponse);
    }
}
