package com.model;

public class Client {
    private String redirectUri = "";
    private String state = "";

    public Client(String redirectUri, String state) {
        this.redirectUri = redirectUri;
        this.state = state;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getState() {
        return state;
    }
}
