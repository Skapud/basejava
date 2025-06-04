package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.storage.Storage;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;

public class ResumeServletHtml extends HttpServlet {
    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html; charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        String name = request.getParameter("name");
//            response.getWriter().write(name == null ? "Hello resumes!" : "Hello " + name + " !");
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }
}
