package com.urise.webapp.storage;

import com.urise.webapp.model.*;

import java.time.LocalDate;
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
        resume.setSection(PERSONAL, new TextSection("Personal data"));
        resume.setSection(OBJECTIVE, new TextSection("Lead non-worker"));
        resume.setSection(ACHIEVEMENT, new ListSection(List.of("Achivment11", "Achivment12", "Achivment13")));
        resume.setSection(QUALIFICATIONS, new ListSection(List.of("Java", "SQL", "Cycling", "Gym")));
        resume.setSection(EXPERIENCE, new TimelineSection(List.of(
                new Companies("Adzhakhuri and Khachapuri", "https://adzhapuri.ge", List.of(new Periods(
                        "Povar",
                        LocalDate.of(2016, 5, 1),
                        LocalDate.of(2018, 12, 31),
                        "Разработка внутренних сервисов и API"))),
                new Companies("Trinx bikes", "https://trinx.ge", List.of(new Periods(
                        "Engineer",
                        LocalDate.of(2019, 1, 1),
                        LocalDate.of(2022, 12, 05),
                        "Разработка веб приложения")))
        )));
        resume.setSection(EDUCATION, new TimelineSection(List.of(
                new Companies("School 1479", "https://sch1479.ru", List.of(new Periods(
                        "Schoolboy",
                        LocalDate.of(2000, 9, 1),
                        LocalDate.of(2009, 6, 30),
                        "With advanced English language study"))),
                new Companies("MIIT", "https://miit.ru", List.of(new Periods(
                        "Student",
                        LocalDate.of(2010, 9, 1),
                        LocalDate.of(2015, 6, 30),
                        "Economics and finance"), new Periods(
                        "Student",
                        LocalDate.of(2019, 9, 1),
                        LocalDate.of(2022, 9, 26),
                        "Bridges and transport tunnels")))
        )));
        return resume;
    }
}