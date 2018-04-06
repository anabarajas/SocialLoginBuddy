package com.oauth;

public class AuthenticationRequest {
    public static String USER_SESSION = "1234567890abcdefg";
    public static String CLIENT_ID = "282485959172-68df31dotcu7lo705k4up9dkd5tfcen4.apps.googleusercontent.com";
    public static String CLIENT_SECRET = "9vgxMF-GPWvC-bmE0l8ABkz6";

    // TODO: Figure out how to get base URI from Discovery Document - use key "authorization_endpoint"
    //public static String GOOGLE_DISCOVERY_DOCUMENT_URI = "https://accounts.google.com/.well-known/openid-configuration";
    public static String authorizationBaseURI = "https://accounts.google.com/o/oauth2/v2/auth";

    public String createAuthenticationRequest(String userSession) {
        StringBuilder authenticationURI = new StringBuilder();

        //authenticationURI.append();
        return "";
    }
}
