package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import java.util.Arrays;

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
            throw new ExistStorageException(r.getUuid());
        } else if ((Integer) getSearchKey(r.toString()) >= 0) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            insertResume(r);
            size++;
        }
    }

    @Override
    public void doDelete(Object object) {
        int index = (Integer) object;
        if (index < 0) {
            throw new NotExistStorageException(storage[(int) object].getUuid());
        }
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
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        }
        storage[index] = r;
    }

    @Override
    public Resume[] doGetAll() {
        return Arrays.copyOf(storage, size);
    }

    public boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertResume(Resume r);

    protected abstract Object getSearchKey(String uuid);
}
