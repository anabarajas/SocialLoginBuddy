package com.servlet;

import com.oauth.AuthenticationRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String output = "";
        if (request.getQueryString() == null) {
            // TODO: handle error properly
            System.out.println("RedirectionServlet:: null redirection URI from service provider");
        } else if (request.getQueryString() == "") {
            System.out.println("RedirectionServlet:: no redirection URI from service provider");
        } else {
            String redirectURLqueryString = request.getQueryString();
            System.out.println(new StringBuilder("RedirectionServlet:: RedirectURI from Google: ").append(redirectURLqueryString));
            String authorizationCode = ParsingUtils.parseRedirectURL(redirectURLqueryString);

            if (authorizationCode != null) {
                AuthenticationRequest ar = new AuthenticationRequest();
                output = ar.getAccessToken(authorizationCode);
            }

        }
        response.getWriter().println(output);
    }

}
