package com.utils;

import com.constants.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParsingUtils {

    public static String parseRedirectURL(String queryString) {
        String[] array = queryString.split("&");
        for (String str : array) {
            String[] pair = str.split("=");
            if (2 != pair.length) {
                throw new IllegalArgumentException("ParsingUtils:parseRedirectURL:: URI Parse failed");
            } else {
                if (pair[0].equals("code")) {
                    String authorizationCode = pair[1];
                    System.out.println(new StringBuilder("ParsingUtils:parseRedirectURL:: code is: ").append(authorizationCode));
                    return authorizationCode;
                }
            }
        }
        System.out.println("OauthAuthorizationServlet:: no code recieved");
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
                    System.out.println(new StringBuilder("ParsingUtils:parsePOSTproviderResponse:: access_token is: ").append(access_token));

                } else {
                    System.out.println("ParsingUtils:parsePOSTproviderResponse:: no access_token in response");
                }

            } else {
                System.out.println("ParsingUtils:parsePOSTproviderResponse:: parsing failed");
            }
        } catch (ParseException e) {
            System.out.println(new StringBuilder("ParsingUtils:parsePOSTproviderResponse::").append(e.getStackTrace()));
        }
        finally {
            return access_token;
        }
    }
}
