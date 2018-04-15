package com.model;

public class Client {
    private String redirectUri = "";
    private String session = "";

    public Client(String redirectUri, String session) {
        this.redirectUri = redirectUri;
        this.session = session;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getSession() {
        return session;
    }
}
