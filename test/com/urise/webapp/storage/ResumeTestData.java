package com.urise.webapp.storage;

import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.model.TimelineSection;

import java.util.ArrayList;

import static com.urise.webapp.model.ContactType.*;
import static com.urise.webapp.model.SectionType.*;

public class ResumeTestData {
    public Resume create(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        resume.setContact(PHONE, "+74996993941");
        resume.setContact(MOBILE, "+7995591117336");
        resume.setContact(HOME_PHONE, "+79057641579");
        resume.setContact(SKYPE, "skapud");
        resume.setContact(MAIL, "tinyxxl@bk.ru");
        resume.setContact(LINKEDIN, "linkedin.com/in/igor-podolian-61006b2b7");
        resume.setContact(GITHUB, "github.com/Skapud/basejava");
        resume.setContact(STACKOVERFLOW, "stackoverflow.com/users/4443260/kris-rice");
        resume.setSection(PERSONAL, new TextSection("Шизофренический склад ума"));
        resume.setSection(OBJECTIVE, new TextSection("Ведущий бездельник"));
        resume.setSection(ACHIEVEMENT, new ListSection(new ArrayList<>()));
        resume.setSection(QUALIFICATIONS, new ListSection(new ArrayList<>()));
        resume.setSection(EXPERIENCE, new TimelineSection(new ArrayList<>()));
        resume.setSection(EDUCATION, new TimelineSection(new ArrayList<>()));
        return resume;
    }
}