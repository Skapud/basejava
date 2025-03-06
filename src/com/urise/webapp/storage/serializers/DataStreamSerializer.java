package com.urise.webapp.storage.serializers;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeContacts(resume, dos);
            writeSections(resume, dos);
        } catch (IOException e) {
            throw new StorageException("Write error", null, e);
        }
    }


    private static void writeContacts(Resume resume, DataOutputStream dos) throws IOException {
        Map<ContactType, String> contacts = resume.getContacts();
        dos.writeInt(contacts.size()); //marker
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private static void writeSections(Resume resume, DataOutputStream dos) throws IOException {
        Map<SectionType, Section> sections = resume.getSections();
        dos.writeInt(sections.size()); //marker
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            SectionType sectionType = entry.getKey();
            Section section = entry.getValue();
            dos.writeUTF(sectionType.name());
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> writeTextSection(dos, (TextSection) section);
                case ACHIEVEMENT, QUALIFICATIONS -> writeListSection(dos, (ListSection) section);
                case EXPERIENCE, EDUCATION -> writeTimelineSection(dos, (TimelineSection) section);
                default -> throw new IllegalStateException("Unexpected value: " + section);
            }
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();

            Resume resume = new Resume(uuid, fullName);
            readContacts(dis, resume);
            readSections(dis, resume);
            return resume;
        } catch (IOException e) {
            throw new StorageException("Write error", null, e);
        }
    }

    private static void readContacts(DataInputStream dis, Resume resume) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
        }
    }

    private static void readSections(DataInputStream dis, Resume resume) throws IOException {
        int sectionSize = dis.readInt();
        for (int i = 0; i < sectionSize; i++) {
            SectionType sectionType = SectionType.valueOf(dis.readUTF());
            Section section;
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> section = readTextSection(dis);
                case ACHIEVEMENT, QUALIFICATIONS -> section = readListSection(dis);
                case EXPERIENCE, EDUCATION -> section = readTimelineSection(dis);
                default -> throw new IllegalStateException("Unexpected value: " + sectionType);
            }
            resume.addSection(sectionType, section);
        }
    }

    private static void writeTextSection(DataOutputStream dos, TextSection textSection) throws IOException {
        dos.writeUTF(textSection.getText());
    }

    private static void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        dos.writeInt(listSection.getLines().size()); //marker
        for (String list : listSection.getLines()) {
            dos.writeUTF(list);
        }
    }

    private static void writeTimelineSection(DataOutputStream dos, TimelineSection timelineSection) throws IOException {
        dos.writeInt(timelineSection.getCompanies().size()); //marker
        for (Companies company : timelineSection.getCompanies()) {
            dos.writeUTF(company.getName());
            dos.writeUTF(company.getWebsite());
            writePeriods(dos, company.getPeriods());
        }
    }

    private static Section readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private static Section readListSection(DataInputStream dis) throws IOException {
        List<String> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        return new ListSection(list);
    }

    private static Section readTimelineSection(DataInputStream dis) throws IOException {
        List<Companies> companiesList = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String website = dis.readUTF();
            List<Periods> periods = readPeriods(dis);
            Companies company = new Companies(name, website, periods);
            companiesList.add(company);
        }
        return new TimelineSection(companiesList);
    }

    private static void writePeriods(DataOutputStream dos, List<Periods> periods) throws IOException {
        dos.writeInt(periods.size());
        for (Periods period : periods) {
            dos.writeUTF(period.getTitle());
            dos.writeUTF(period.getStartDate().toString());
            dos.writeUTF(period.getEndDate().toString());
            dos.writeUTF(period.getDescription());
        }
    }

    private static List<Periods> readPeriods(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<Periods> periods = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            periods.add(new Periods(
                    dis.readUTF(),
                    LocalDate.parse(dis.readUTF()),
                    LocalDate.parse(dis.readUTF()),
                    dis.readUTF()));
        }
        return periods;
    }
}
