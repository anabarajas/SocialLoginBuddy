package com.utils;

import com.constants.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static ConcurrentHashMap<Constants, String> extractResponseClientTokens(String providerResponse){
        JSONParser parser = new JSONParser();
        ConcurrentHashMap<Constants, String> clientTokens = new ConcurrentHashMap<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(providerResponse);

            // Get access_token
            String accessTokenValue = (String) jsonObject.get(Constants.ACCESS_TOKEN.getKey());
            clientTokens.put(Constants.ACCESS_TOKEN, accessTokenValue);
            LOGGER.info(new StringBuilder("extractResponseClientTokens:: access_token in response is: ").append(accessTokenValue));

            // Get id_token
            String idTokenValue = (String) jsonObject.get(Constants.ID_TOKEN.getKey());
            clientTokens.put(Constants.ID_TOKEN, idTokenValue);
            LOGGER.info(new StringBuilder("extractResponseClientTokens:: id_token in response is: ").append(idTokenValue));

        } catch (Exception e) {
            LOGGER.error(new StringBuilder("extractResponseClientTokens:: JSON object parsing failed").append(e.getStackTrace()));
        }
        return clientTokens;
    }
}