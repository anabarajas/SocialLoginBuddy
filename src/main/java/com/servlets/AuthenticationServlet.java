package com.servlets;

import com.oauthflow.OauthFlowServiceManager;
import com.oauthflow.SessionHandlingManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionHandlingManager.persistClientProvidedRedirectURI(request);
        OauthFlowServiceManager.createAuthorizationRequestURI(request,response);
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info(new StringBuffer("Servlet ").append(this.getServletName()).append(" has started"));
    }

    @Override
    public void destroy() {
        LOGGER.info(new StringBuffer("Servlet ").append(this.getServletName()).append(" has stopped"));
    }
}
