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
    ID_TOKEN("id_token"),
    REDIRECT_URI("redirect_uri"),
    RESPONSE_TYPE("response_type"),
    SCOPE("scope"),
    STATE("state"),
    OPENID_SCOPE("openid%20profile%20email"),
    UTF_8("UTF-8"),
    USER_INFO("userinforesponse");

    private final String key;

    Constants(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
