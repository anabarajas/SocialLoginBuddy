package com.utils;

import com.constants.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

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

    public static String convertQueryStringToResponseURI(String queryString) {
        // remove redirect_uri key from query string
        String substringURI = queryString.substring(Constants.REDIRECT_URI.getKey().length() + 1);
        StringBuilder clientResponseURI = new StringBuilder();

        // replace first & for ?
        String[] queryParameters = substringURI.split("&");

        clientResponseURI.append(queryParameters[0]).append("?");
        for (int s = 1; s < queryParameters.length; s++) {
            clientResponseURI.append(queryParameters[s]);
            if (s < queryParameters.length - 1) {
                clientResponseURI.append("&");
            }
        }
        return clientResponseURI.toString();
    }

    public static String decodeUserinfo(String encodedUserInfo) {
        return new String(Base64.getDecoder().decode(encodedUserInfo));
    }
}