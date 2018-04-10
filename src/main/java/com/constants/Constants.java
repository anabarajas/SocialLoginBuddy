package com.constants;

public enum JSONKeys {
    ACCESS_TOKEN("access_token");

    private final String key;

    JSONKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}


