package com.servlets;

import com.oauthflow.SessionHandlingManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MyClientServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyClientServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // get parameters from request
        List<String> parameterNames = Collections.list(request.getParameterNames());
        List<String> parameters = parameterNames.stream()
                .map(p -> request.getParameter(p))
                .map(q -> new StringBuilder(q).append("\n\n"))
                .map(r -> r.toString())
                .collect(Collectors.toList());

        // print content from request
        PrintWriter printWriter = response.getWriter();
        printWriter.println(parameters.stream().collect(Collectors.joining()));
    }
}
