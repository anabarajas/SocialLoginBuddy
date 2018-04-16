package com.model;

public class SlbClient {
    private String slbSession = "";
    private String authorizationCode = "";
    private String accessToken = "";
    private String idToken = "";

    public String getSlbSession() {
        return slbSession;
    }

    public void setSlbSession(String slbSession) {
        this.slbSession = slbSession;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
