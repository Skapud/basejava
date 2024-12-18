package com.urise.webapp.storage;

import com.urise.webapp.exception.*;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume r) {
        Object object = getNotExistingSearchKey(r.getUuid());
        doSave(r, object);
    }

    public void delete(String uuid) {
        Object object = getExistingSearchKey(uuid);
        doDelete(object);
    }

    public Resume get(String uuid) {
        Object object = getExistingSearchKey(uuid);
        return doGet(object);
    }

    public void update(Resume r) {
        Object object = getExistingSearchKey(r.getUuid());
        doUpdate(r, object);
    }

    private Object getExistingSearchKey(String uuid) {
        Object object = getSearchKey(uuid);
        if (!isExist(object)) {
            throw new NotExistStorageException(uuid);
        }
        return object;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object object = getSearchKey(uuid);
        if (isExist(object)) {
            throw new ExistStorageException(uuid);
        }
        return object;
    }

    public Resume[] getAll() {
        return doGetAll();
    }

    protected abstract void doUpdate(Resume r, Object object);

    protected abstract void doSave(Resume r, Object object);

    protected abstract void doDelete(Object object);

    protected abstract Resume doGet(Object object);

    protected abstract Resume[] doGetAll();

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);
}
