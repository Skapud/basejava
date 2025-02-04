package com.urise.webapp.storage;

import com.urise.webapp.model.ListSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.model.TimelineSection;

import java.util.List;

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
        resume.setSection(PERSONAL, new TextSection("Личные качества", "Шизофренический склад ума"));
        resume.setSection(OBJECTIVE, new TextSection("Позиция", "Ведущий бездельник"));
        resume.setSection(ACHIEVEMENT, new ListSection());
        resume.setSection(QUALIFICATIONS, new ListSection());
        resume.setSection(EXPERIENCE, new TimelineSection());
        resume.setSection(EDUCATION, new TimelineSection());
        return resume;
    }
}

("Личные качества"),
("Позиция"),
("Достижения"),
("Квалификация"),
("Опыт работы"),
("Образование");
