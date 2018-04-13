package com.utils;

import com.constants.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Base64;
import java.util.HashMap;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static HashMap<Constants, String> parsePostProviderResponse(String POSTrequest){
        JSONParser parser = new JSONParser();
        HashMap<Constants, String> clientTokens = new HashMap<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(POSTrequest);
            if (jsonObject != null) {
                // Get access_token
                String accessTokenValue = (String) jsonObject.get(Constants.ACCESS_TOKEN.getKey());
                if (accessTokenValue != null || !accessTokenValue.equals("")) {
                    clientTokens.put(Constants.ACCESS_TOKEN, accessTokenValue);
                    LOGGER.info(new StringBuilder("parsePostProviderResponse:: access_token in response is: ").append(accessTokenValue));
                } else {
                    LOGGER.info("parsePostProviderResponse:: no access_token recieved");
                }
                // Get id_token
                String idTokenValue = (String) jsonObject.get(Constants.ID_TOKEN.getKey());
                if (idTokenValue != null || !idTokenValue.equals("")) {
                    clientTokens.put(Constants.ID_TOKEN, idTokenValue);
                    LOGGER.info(new StringBuilder("parsePostProviderResponse:: id_token in response is: ").append(idTokenValue));
                } else {
                    LOGGER.info("parsePostProviderResponse:: no id_token recieved");
                }
            } else {
                LOGGER.info("parsePostProviderResponse:: JSON object parsing failed");
            }
        } catch (ParseException e) {
            LOGGER.error(new StringBuilder("parsePostProviderResponse:: ").append(e.getStackTrace()));
        }
        return clientTokens;
    }

    public static HashMap<Constants, String> extractQueryParameters(String queryString) {
        HashMap<String, String> queryParametersMap = new HashMap<>();
        HashMap<Constants, String> result = new HashMap<>();

        // remove redirect_uri key from query string
      //  String substringURI = queryString.substring(Constants.REDIRECT_URI.getKey().length() + 1);

        String[] queryParameters = queryString.split("&");

        for (int i = 0; i < queryParameters.length; i++) {
            String[] keyValuePair = queryParameters[i].split("=");
            queryParametersMap.put(keyValuePair[0], keyValuePair[1]);
        }

        if (queryParametersMap.containsKey(Constants.REDIRECT_URI.getKey())) {
            result.put(Constants.REDIRECT_URI, queryParametersMap.get(Constants.REDIRECT_URI.getKey()));
            queryParametersMap.remove(Constants.REDIRECT_URI.getKey());
        }
        if (queryParametersMap.containsKey(Constants.STATE.getKey())) {
            result.put(Constants.STATE, queryParametersMap.get(Constants.STATE.getKey()));
            queryParametersMap.remove(Constants.STATE.getKey());
        }
        if (queryParametersMap.size() > 0) {
            result.put(Constants.OTHER_PARAMETERS, queryParametersMap.values().toString());
        }
        return result;
// replace first & for ?
//        clientResponseURI.append(queryParameters[0]).append("?");
//        for (int s = 1; s < queryParameters.length; s++) {
//            clientResponseURI.append(queryParameters[s]);
//            if (s < queryParameters.length - 1) {
//                clientResponseURI.append("&");
//            }
//        }
//        return clientResponseURI.toString();
    }

    public static String decodeUserinfo(String encodedUserInfo) {

        return new String(Base64.getDecoder().decode(encodedUserInfo));
    }
}