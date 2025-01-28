package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractStorageTest {
    protected Storage storage;
    protected static final String UUID_1 = "UUID1";
    protected static final String UUID_2 = "UUID2";
    protected static final String UUID_3 = "UUID3";
    protected static final String UUID_4 = "UUID4";
    protected static final String UUID_NOT_EXIST = "dummy";

    protected static final Resume RESUME_1 = new Resume(UUID_1, "Name_1");
    protected static final Resume RESUME_2 = new Resume(UUID_2, "Name_2");
    protected static final Resume RESUME_3 = new Resume(UUID_3, "Name_3");
    protected static final Resume RESUME_4 = new Resume(UUID_4, "Name_4");

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assertions.assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    public void update() {
        Resume newResume = new Resume(RESUME_1.getUuid(), "Name_1");
        storage.update(newResume);
        Assertions.assertSame(newResume, storage.get(RESUME_1.getUuid()));
        assertSize(storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume(UUID_NOT_EXIST, "Name_dummy")));
    }

    @Test
    public void getAll() {
        if (storage instanceof MapUuidStorage || storage instanceof MapResumeStorage) {
            return;
        }
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        Assertions.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void getAllSorted() {
        List<Resume> expected = new ArrayList<>(Arrays.asList(RESUME_1, RESUME_2, RESUME_3));
        Assertions.assertEquals(expected, storage.getAllSorted());
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    protected void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }

    protected void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }
}