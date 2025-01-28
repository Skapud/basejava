package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
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
    public void doSave(Resume r, Object object) {
        if (size >= storage.length) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        insertResume(r);
        size++;
    }

    @Override
    public void doDelete(Object object) {
        int index = (Integer) object;
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public Resume doGet(Object object) {
        return storage[(Integer) object];
    }

    @Override
    public void doUpdate(Resume r, Object object) {
        int index = (Integer) getSearchKey(r.getUuid());
        storage[index] = r;
    }

    @Override
    public Resume[] doGetAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    public boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertResume(Resume r);

    protected abstract Object getSearchKey(String uuid);
}
