package com.servlets;

import com.oauth.SocialLoginServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OauthAuthorizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userInformationJSONstring = "";
        if (request.getQueryString() == null) {
            // TODO: handle error properly
            System.out.println("OauthAuthorizationServlet:: null redirection URI from service provider");
        } else if (request.getQueryString() == "") {
            System.out.println("OauthAuthorizationServlet:: no redirection URI from service provider");
        } else {
            // Get authorization code
            String redirectURLqueryString = request.getQueryString();
            System.out.println(new StringBuilder("OauthAuthorizationServlet:: RedirectURI from Google: ").append(redirectURLqueryString));
            String authorizationCode = ParsingUtils.parseRedirectURL(redirectURLqueryString);
            if (authorizationCode != null) {
                // Get access token
                SocialLoginServiceManager socialLoginServiceManager = new SocialLoginServiceManager();
                String accessToken = socialLoginServiceManager.getAccessToken(authorizationCode);
                // Get user info
                if (accessToken != null || !accessToken.equals("")) {
                    userInformationJSONstring = socialLoginServiceManager.getUserInfo(accessToken);
                }
            }
        }
        response.getWriter().println(userInformationJSONstring);
    }

}
