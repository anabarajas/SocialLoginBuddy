package com.servlets;

import com.constants.Constants;
import com.model.SlbClient;
import com.oauthflow.SessionHandlingManager;
import com.oauthflow.OauthFlowServiceManager;
import com.sun.tools.internal.jxc.ap.Const;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

public class OauthFlowServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OauthFlowServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getQueryString() != null || !request.getQueryString().equals("")) {
                HttpSession httpSession = request.getSession(false);
                LOGGER.info(new StringBuilder("doGet - Provider response query string: ").append(request.getQueryString()));

                // confirm antiforgery state token
                String httpSessionId = httpSession.getId();
                if (!httpSessionId.equals(request.getParameter(Constants.STATE.getKey()))) {
                    LOGGER.fatal(new StringBuilder("doGet - User state tokens don't match!"));
                    throw new IllegalArgumentException(new StringBuilder("doGet - mismatch with httpSession id ").append(httpSessionId).toString());
                }

                String responsePostForm = OauthFlowServiceManager.performOAuthFlow(request);

                // Post user information to client
                response.getWriter().println(responsePostForm);
            } else {
                throw new IllegalArgumentException("doGet - Empty response from service provider");
            }
        } catch (IOException e) {
            LOGGER.error(new StringBuilder("doGet - error performing oauth flow: ").append(e.getStackTrace()));
        }
    }

}
