package com.urise.webapp.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class ResumeServletHtml extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
            response.getWriter().write(name == null ? "Hello resumes!" : "Hello " + name + " !");
    }
}
