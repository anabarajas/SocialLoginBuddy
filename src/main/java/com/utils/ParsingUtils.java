package com.utils;

import com.constants.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class ParsingUtils {

    private static final Logger LOGGER = Logger.getLogger(ParsingUtils.class);

    public static HashMap<Constants, String> parsePOSTproviderResponse(String POSTrequest){
        JSONParser parser = new JSONParser();
        HashMap<Constants, String> clientTokens = new HashMap<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(POSTrequest);
            if (jsonObject != null) {
                // Get access_token
                String accessTokenValue = (String) jsonObject.get(Constants.ACCESS_TOKEN.getKey());
                if (accessTokenValue != null || !accessTokenValue.equals("")) {
                    clientTokens.put(Constants.ACCESS_TOKEN, accessTokenValue);
                    LOGGER.info(new StringBuilder("parsePOSTproviderResponse:: access_token in response is: ").append(accessTokenValue));
                } else {
                    LOGGER.info("parsePOSTproviderResponse:: no access_token recieved");
                }
                // Get id_token
                String idTokenValue = (String) jsonObject.get(Constants.ID_TOKEN.getKey());
                if (idTokenValue != null || !idTokenValue.equals("")) {
                    clientTokens.put(Constants.ID_TOKEN, idTokenValue);
                    LOGGER.info(new StringBuilder("parsePOSTproviderResponse:: id_token in response is: ").append(idTokenValue));
                } else {
                    LOGGER.info("parsePOSTproviderResponse:: no id_token recieved");
                }
            } else {
                LOGGER.info("parsePOSTproviderResponse:: JSON object parsing failed");
            }
        } catch (ParseException e) {
            LOGGER.error(new StringBuilder("parsePOSTproviderResponse:: ").append(e.getStackTrace()));
        }
        return clientTokens;
    }
}
