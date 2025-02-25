package com.urise.webapp.storage.serializers;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.util.PeriodUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size()); //marker
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

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
        } catch (IOException e) {
            throw new StorageException("Write error", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

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
            return resume;
        } catch (IOException e) {
            throw new StorageException("Write error", null, e);
        }
    }

    private void writeTextSection(DataOutputStream dos, TextSection textSection) throws IOException {
        dos.writeUTF(textSection.getText());
    }

    private void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        dos.writeInt(listSection.getLines().size()); //marker
        for (String list : listSection.getLines()) {
            dos.writeUTF(list);
        }
    }

    private void writeTimelineSection(DataOutputStream dos, TimelineSection timelineSection) throws IOException {
        dos.writeInt(timelineSection.getCompanies().size()); //marker
        for (Companies company : timelineSection.getCompanies()) {
            dos.writeUTF(company.getName());
            dos.writeUTF(company.getWebsite());
            PeriodUtil.writePeriods(dos, company.getPeriods());
        }
    }

    private Section readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private Section readListSection(DataInputStream dis) throws IOException {
        List<String> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        return new ListSection(list);
    }

    private Section readTimelineSection(DataInputStream dis) throws IOException {
        List<Companies> companiesList = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            Companies company = new Companies(dis.readUTF(), dis.readUTF(), PeriodUtil.readPeriods(dis));
            companiesList.add(company);
        }
        return new TimelineSection(companiesList);
    }
}
