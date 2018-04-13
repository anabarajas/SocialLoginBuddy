package com.servlets;

import com.constants.Constants;
import com.oauthflow.SessionHandlingManager;
import com.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MyClientServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyClientServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringBuilder responseToPrint = new StringBuilder("");

        // get parameters from request
        List<String> parameterNames = Collections.list(request.getParameterNames());
        List<String> parameters = parameterNames.stream()
                .map(p -> request.getParameter(p))
                .map(r -> r.toString())
                .collect(Collectors.toList());

        // decode userInfo from base64
        parameters.set(2, Utils.decodeUserinfo(parameters.get(2)));

        for (int i = 0; i < parameterNames.size(); i++) {
            responseToPrint.append(parameterNames.get(i)).append(" = ").append(parameters.get(i)).append("\n");
        }

        // print content from request
        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseToPrint.toString());
    }
}
