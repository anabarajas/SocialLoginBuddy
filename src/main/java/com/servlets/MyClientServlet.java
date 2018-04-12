package com.servlets;

import com.oauthflow.SessionHandlingManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyClientServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyClientServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().println(SessionHandlingManager.getClientUserInfo());
    }
}
