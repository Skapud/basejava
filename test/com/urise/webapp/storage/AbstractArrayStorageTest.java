package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1);
        storage.update(newResume);
        Assertions.assertSame(newResume, storage.get("UUID_1"));
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void getAll() {
        Resume[] resumes = storage.getAll();
        storage.save(new Resume(UUID_4));
        Assertions.assertNotEquals(resumes, storage.getAll());
    }

    @Test
    void save() {
        storage.save(new Resume(UUID_4));
        Resume[] resumes = storage.getAll();
        int size = storage.size();
        Assertions.assertEquals(UUID_4, resumes[size - 1].getUuid());
        Assertions.assertEquals(4, storage.size());
    }

    @Test
    void delete() {
        storage.delete("uuid1");
        Assertions.assertEquals(2, storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.get("UUID_1"));
    }

    @Test
    void get() {
        Resume resume = storage.get("UUID_1");
        Assertions.assertEquals(UUID_1, resume.getUuid());
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }
}