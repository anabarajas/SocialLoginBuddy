package com.servlets;

import com.constants.Constants;
import com.model.SlbClient;
import com.oauthflow.SessionHandlingManager;
import com.oauthflow.OauthFlowServiceManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class OauthFlowServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OauthFlowServlet.class);
    private SlbClient slbClient;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            slbClient = new SlbClient();
            if (request.getQueryString() != null || !request.getQueryString().equals("")) {
                LOGGER.info(new StringBuilder("doGet - provider response query string: ").append(request.getQueryString()));
                slbClient.setSlbSession(request.getParameter(Constants.SLB_SESSION.getKey()));

                // Get authorization code from provider URL response
                slbClient.setAuthorizationCode(request.getParameter(Constants.CODE.getKey()));
                String responsePostForm = OauthFlowServiceManager.performOAuthFlow(slbClient.getAuthorizationCode());

                // Post user information to client
                response.getWriter().println(responsePostForm);
            } else {
                throw new IllegalArgumentException("doGet - empty response from service provider");
            }
        } catch (IOException e) {
            LOGGER.error(new StringBuilder("doGet - error performing oauth flow: ").append(e.getStackTrace()));
        }
    }

}
