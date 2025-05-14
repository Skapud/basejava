package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

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
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeSQL("" +
                "SELECT * FROM resume AS r " +
                "  LEFT JOIN contact AS c " +
                "    ON r.UUID = c.resume_uuid " +
                " WHERE r.uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                String value = rs.getString("value");
                ContactType type = ContactType.valueOf(rs.getString("type"));
                resume.addContact(type, value);
            } while (rs.next());
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
