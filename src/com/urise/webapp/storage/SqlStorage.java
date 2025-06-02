package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeSQL("DELETE FROM resume", preparedStatement -> {
            preparedStatement.execute();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "UPDATE resume SET full_name = ?" +
                    " WHERE uuid = ?")) {
                preparedStatement.setString(1, r.getFullName());
                preparedStatement.setString(2, r.getUuid());
                if (preparedStatement.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "UPDATE contact SET value = ? " +
                    " WHERE resume_uuid = ? AND type = ?")) {
                processContacts(r, preparedStatement);
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "UPDATE section SET value = ? " +
                    " WHERE resume_uuid = ? AND type = ?")) {
                processSections(r, preparedStatement);
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "INSERT INTO resume(uuid, full_name) " +
                    "VALUES (?, ?)")) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, r.getFullName());
                preparedStatement.execute();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "INSERT INTO contact(value, resume_uuid, type) " +
                    "VALUES (?, ?, ?)")) {
                processContacts(r, preparedStatement);
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "INSERT INTO section(value, resume_uuid, type) " +
                    "VALUES (?,?,?)")) {
                processSections(r, preparedStatement);
            }
            return null;
        });
    }

    private static void processSections(Resume r, PreparedStatement preparedStatement) throws SQLException {
        for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
            Section section = e.getValue();
            String value = JsonParser.write(section, Section.class);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, r.getUuid());
            preparedStatement.setString(3, e.getKey().name());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "SELECT * FROM resume AS r " +
                    "  LEFT JOIN contact AS c " +
                    "    ON r.UUID = c.resume_uuid " +
                    " WHERE r.uuid = ?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
                do {
                    String value = rs.getString("value");
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    resume.addContact(type, value);
                } while(rs.next());
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "SELECT * FROM resume AS r " +
                    "  LEFT JOIN section AS s " +
                    "    ON r.UUID = s.resume_uuid " +
                    " WHERE r.uuid = ?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    if (!(rs.getString("type") == null)) {
                        SectionType type = SectionType.valueOf(rs.getString("type"));
                        Section value = JsonParser.read(rs.getString("value"), Section.class);
                        resume.addSection(type, value);
                    }
                }
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeSQL("DELETE FROM resume AS r WHERE r.uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, uuid);
            preparedStatement.executeUpdate();
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LinkedHashMap<String, Resume> resumes = new LinkedHashMap<>();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM resume")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    String name = rs.getString("full_name");
                    Resume newResume = new Resume(uuid, name);
                    resumes.put(uuid, newResume);
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String resumeUuid = rs.getString("resume_uuid");
                    String contactType = rs.getString("type");
                    String value = rs.getString("value");
                    Resume resume = resumes.get(resumeUuid);
                    resume.addContact(ContactType.valueOf(contactType), value);
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String resumeUuid = rs.getString("resume_uuid");
                    SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                    Section section = JsonParser.read(rs.getString("value"), Section.class);
                    Resume resume = resumes.get(resumeUuid);
                    resume.addSection(sectionType, section);
                }
            }
            return null;
        });
        return new ArrayList<>(resumes.values());
    }

    @Override
    public int size() {
        return sqlHelper.executeSQL("SELECT COUNT(*) FROM resume", preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    private static void processContacts(Resume r, PreparedStatement preparedStatement) throws SQLException {
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            preparedStatement.setString(1, e.getValue());
            preparedStatement.setString(2, r.getUuid());
            preparedStatement.setString(3, e.getKey().name());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }
}
