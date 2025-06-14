package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.HtmlUtil;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResumeServletHtml extends HttpServlet {

    private enum THEME {
        dark, light, purple
    }

    private Storage storage;
    private final Set<String> themes = new HashSet<>();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
        for (THEME t : THEME.values()) {
            themes.add(t.name());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        final boolean isCreate = (uuid == null || uuid.isEmpty());
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            Config.get().checkImmutable(uuid);
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.setSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.setSection(type, new ListSection(value.split("\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Companies> companies = new ArrayList<>();
//                        String[] names = request.getParameterValues(type.name());
                        String[] websites = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                String website = websites[i];
                                List<Periods> periods = new ArrayList<>();
                                String prefix = type.name() + i;

                                String[] startDates = request.getParameterValues(prefix + "startDate");
                                String[] endDates = request.getParameterValues(prefix + "endDate");
                                String[] titles = request.getParameterValues(prefix + "titles");
                                String[] descriptions = request.getParameterValues(prefix + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        periods.add(new Periods(titles[j],
                                                DateUtil.parse(startDates[j]),
                                                DateUtil.parse(endDates[j]),
                                                descriptions[j]));
                                    }
                                }
                                companies.add(new Companies(name, website, periods));
                            }
                        }
                        r.setSection(type, new TimelineSection(companies));
                        break;
                }
            }
        }
        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume?theme=" + getTheme(request));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
//        response.setContentType("text/html; charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        String name = request.getParameter("name");
//            response.getWriter().write(name == null ? "Hello resumes!" : "Hello " + name + " !");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    TimelineSection section = (TimelineSection) r.getSection(type);
                    List<Companies> emptyFirstCompany = new ArrayList<>();
                    emptyFirstCompany.add(Companies.EMPTY);
                    if (section != null) {
                        for (Companies company : section.getCompanies()) {
                            List<Periods> emptyFirstPeriod = new ArrayList<>();
                            emptyFirstPeriod.add(Periods.EMPTY);
                            emptyFirstPeriod.addAll(company.getPeriods());
                            emptyFirstCompany.add(new Companies(company.getName(), company.getWebsite(), emptyFirstPeriod));
                        }
                    }
                    r.setSection(type, new TimelineSection(emptyFirstCompany));
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private String getTheme(HttpServletRequest request) {
        String theme = request.getParameter("theme");
        return themes.contains(theme) ? theme : THEME.light.name();
    }
}
