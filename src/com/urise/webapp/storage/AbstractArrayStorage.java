package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void doSave(Resume resume, Integer key) {
        if (size >= storage.length) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, key);
        size++;
    }

    @Override
    public void doDelete(Integer key) {
        fillDeletedElement(key);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public Resume doGet(Integer key) {
        return storage[key];
    }

    @Override
    public void doUpdate(Resume resume, Integer key) {
        storage[key] = resume;
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    public boolean isExist(Integer key) {
        return key >= 0;
    }

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract Integer getSearchKey(String uuid);
}
