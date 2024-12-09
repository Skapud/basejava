package com.urise.webapp.storage;

import com.urise.webapp.exception.*;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.*;

import static com.urise.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    protected static final Resume RESUME_1 = new Resume("uuid1");
    protected static final Resume RESUME_2 = new Resume("uuid2");
    protected static final Resume RESUME_3 = new Resume("uuid3");
    protected static final Resume RESUME_4 = new Resume("uuid4");

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void size() {
        assertSize(3);
    }

    void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
        Assertions.assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    void update() {
        Resume newResume = new Resume(RESUME_1.getUuid());
        storage.update(newResume);
        Assertions.assertSame(newResume, storage.get(RESUME_1.getUuid()));
        assertSize(storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy")));
    }

    @Test
    void getAll() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        Assertions.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    void save() {
        storage.save(RESUME_4);
        assertSize(storage.size());
        assertGet(RESUME_4);
    }

    @Test
    void delete() {
        storage.delete("uuid1");
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get("UUID_1"));
    }

    @Test
    void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    void overflow() {
        storage.clear();
        for(int i = 0; i < STORAGE_LIMIT; i++) {
            try {
                storage.save(new Resume("uuid" + i));
            } catch (Exception e) {
                Assertions.fail("Error during fill");
            }
        }
        assertThrows(ExistStorageException.class, () -> storage.save(new Resume(String.valueOf(STORAGE_LIMIT))));
    }
}