package com.utils;

import com.constants.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParsingUtils {

    private static final Logger LOGGER = Logger.getLogger(ParsingUtils.class);

    public static String parseRedirectURL(String queryString) {
        String[] array = queryString.split("&");
        for (String str : array) {
            String[] pair = str.split("=");
            if (2 != pair.length) {
                throw new IllegalArgumentException("parseRedirectURL:: URI Parse failed");
            } else {
                if (pair[0].equals(Constants.CODE.getKey())) {
                    String authorizationCode = pair[1];
                    LOGGER.info(new StringBuilder("parseRedirectURL:: code is: ").append(authorizationCode));
                    return authorizationCode;
                }
            }
        }
        LOGGER.info("OauthAuthorizationServlet:: no code recieved");
        return "";
    }


    public static String parsePOSTproviderResponse(String POSTrequest){
        JSONParser parser = new JSONParser();
        String access_token = "";
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(POSTrequest);
            if (jsonObject != null) {
                access_token = (String) jsonObject.get(Constants.ACCESS_TOKEN.getKey());
                if (!access_token.equals("")) {
                    LOGGER.info(new StringBuilder("parsePOSTproviderResponse:: access_token is: ").append(access_token));

                } else {
                    LOGGER.info("parsePOSTproviderResponse:: no access_token in response");
                }
            } else {
                LOGGER.info("parsePOSTproviderResponse:: parsing failed");
            }
        } catch (ParseException e) {
            LOGGER.error(new StringBuilder("parsePOSTproviderResponse:: ").append(e.getStackTrace()));
        }
        finally {
            return access_token;
        }
    }
}
