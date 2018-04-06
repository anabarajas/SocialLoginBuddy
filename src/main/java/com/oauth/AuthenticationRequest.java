package com.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationRequest {
    public static String USER_HARDCODED_SESSION = "security_token%1234567890abcdefg";
    public static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    public static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";
    public static String REDIRECTION_URI_HARDCODED = "https://www.facebook.com";
    public static String SCOPE_HARDCODED = "openid%20profile%20email&";

    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint"
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";
    public static String AUTHORIZATION_BASE_URI_HARDCODED = "https://accounts.google.com/o/oauth2/v2/auth";


    public void createAuthorizationURI(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            StringBuilder authenticationURL = new StringBuilder();
            authenticationURL.append(AUTHORIZATION_BASE_URI_HARDCODED).append("?")
                    .append("client_id=").append(CLIENT_ID_HARDCODED).append("&")
                    .append("response_type=code&")
                    .append("scope=").append(SCOPE_HARDCODED).append("&")
                    .append("redirect_uri=").append(REDIRECTION_URI_HARDCODED).append("&")
                    .append("state=").append(USER_HARDCODED_SESSION);
//            AuthorizationCodeFlow flow = new AuthorizationCodeFlow(BearerToken.authorizationHeaderAccessMethod(), getTransport() , JacksonFactory.getDefaultInstance(), new GenericUrl());
//            String authenticationURL = flow.newAuthorizationUrl().setState(USER_HARDCODED_SESSION).setRedirectUri(REDIRECTION_URI_HARDCODED).build();
            System.out.println(new StringBuffer("This is the authenticationURL: ").append(authenticationURL));
            response.sendRedirect(authenticationURL.toString());
        } catch (IOException e) {
            System.out.println("YOO. There was an error");
        }
    }

    public static String createAuthenticationRequest(String userSession) {
        StringBuilder authenticationURL = new StringBuilder();

        //authenticationURI.append();
        return "";
    }
}
