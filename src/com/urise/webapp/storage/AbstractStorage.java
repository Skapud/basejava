package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    public List<Resume> getAllSorted() {
        List<Resume> storageAsList = doGetAllSorted();
        storageAsList.removeIf(Objects::isNull);
        storageAsList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return storageAsList;
    }

    protected abstract void doUpdate(Resume r, Object object);

    protected abstract void doSave(Resume r, Object object);

    protected abstract void doDelete(Object object);

    protected abstract Resume doGet(Object object);

    protected abstract Resume[] doGetAll();

    protected abstract List<Resume> doGetAllSorted();

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);
}
