package com.constants;

public enum Constants {
    ACCESS_TOKEN("access_token"),
    AUTHORIZATION("Authorization"),
    AUTHORIZATION_CODE("authorization_code"),
    BEARER("Bearer "),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    CODE("code"),
    GRANT_TYPE("grant_type"),
    REDIRECT_URI("redirect_uri"),
    OPENID_SCOPE("openid%20profile%20email");

    private final String key;

    Constants(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
