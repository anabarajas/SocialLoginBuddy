package com.constants;

public enum Constants {
    ACCESS_TOKEN("access_token"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    GRANT_TYPE("grant_type"),
    CODE("code"),
    OPENID_SCOPE("openid%20profile%20email"),
    AUTHORIZATION_CODE("authorization_code");

    private final String key;

    Constants(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
