package com.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationRequest {
    public static String USER_HARDCODED_SESSION = "1234567890abcdefg";
    public static String CLIENT_ID_HARDCODED = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    public static String CLIENT_SECRET_HARDCODED = "9vgxMF-GPWvC-bmE0l8ABkz6";
    public static String REDIRECTION_URI_HARDCODED = "https://localhost:8080/login";

    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint"
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";
    public static String AUTHORIZATION_BASE_URI_HARDCODED = "https://accounts.google.com/o/oauth2/v2/auth";


    private AuthorizationCodeFlow flow;

    public void createAuthorizationURI(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String authenticationURL = flow.newAuthorizationUrl().setState(USER_HARDCODED_SESSION).setRedirectUri(REDIRECTION_URI_HARDCODED).build();
            System.out.println(new StringBuffer("This is the authenticationURL: ").append(authenticationURL));
            response.sendRedirect(authenticationURL);
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
