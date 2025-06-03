package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeServletFreemarker extends HttpServlet {
    private Storage storage;
    private Configuration configuration;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
        //        storage = new SqlStorage("jdbc:postgresql://localhost:5432/resumes",
//                "postgres",
//                "postgres");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/templates");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Resume> resumes = storage.getAllSorted();
        Map<String, Object> root = new HashMap<>();
        root.put("resumes", resumes);

        var template = configuration.getTemplate("resumes.ftl");

        try (var out = response.getWriter()) {
            template.process(root, out);
        } catch (Exception e) {
            throw new ServletException("Template error", e);
        }
    }
}
