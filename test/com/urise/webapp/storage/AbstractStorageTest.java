package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;

import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();
    protected Storage storage;

    protected static final String UUID_1 = UUID.randomUUID().toString();
    protected static final String UUID_2 = UUID.randomUUID().toString();
    protected static final String UUID_3 = UUID.randomUUID().toString();
    protected static final String UUID_4 = UUID.randomUUID().toString();
    protected static final String UUID_NOT_EXIST = "dummy";

    private static final ResumeTestData testData = new ResumeTestData();

    protected static final Resume R1 = testData.create(UUID_1, "Name_1");
    protected static final Resume R2 = testData.create(UUID_2, "Name_2");
    protected static final Resume R3 = testData.create(UUID_3, "Name_3");
    protected static final Resume R4 = testData.create(UUID_4, "Name_4");

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assertions.assertArrayEquals(new Resume[0], storage.getAllSorted().toArray());
    }

//    @Test
//    public void update() throws Exception {
//        Resume newResume = new Resume(R1.getUuid(), "Name_1");
//        storage.update(newResume);
//        Assertions.assertEquals(newResume, storage.get(R1.getUuid()));
//        assertSize(storage.size());
//        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume(UUID_NOT_EXIST, "Name_dummy")));
//    }

    @Test
    public void update() throws Exception {
        Resume newResume = testData.create(UUID_1, "New_Name");
        storage.update(newResume);
        Assertions.assertEquals(newResume, storage.get(R1.getUuid()));
        assertSize(storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume(UUID_NOT_EXIST, "Name_dummy")));
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> expected = new ArrayList<>(Arrays.asList(R1, R2, R3));
        Assertions.assertEquals(expected, storage.getAllSorted());
    }

    @Test
    public void save() throws Exception {
        storage.save(R4);
        assertSize(4);
        assertGet(R4);
    }

    @Test
    public void saveExist() throws Exception {
        assertThrows(ExistStorageException.class, () -> storage.save(R1));
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void get() {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test
    public void getNotExist() throws Exception {
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    protected void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }

    protected void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }
}