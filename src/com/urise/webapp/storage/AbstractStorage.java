package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public void save(Resume r) {
        LOG.info("Save " + r);
        SK object = getNotExistingSearchKey(r.getUuid());
        doSave(r, object);
    }

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK object = getExistingSearchKey(uuid);
        doDelete(object);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK object = getExistingSearchKey(uuid);
        return doGet(object);
    }

    public void update(Resume r) {
        LOG.info("Update " + r);
        SK object = getExistingSearchKey(r.getUuid());
        doUpdate(r, object);
    }

    private SK getExistingSearchKey(String uuid) {
        SK object = getSearchKey(uuid);
        if (!isExist(object)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return object;
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK object = getSearchKey(uuid);
        if (isExist(object)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return object;
    }

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> storageAsList = doGetAllSorted();
        storageAsList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return storageAsList;
    }

    protected abstract void doUpdate(Resume r, SK object);

    protected abstract void doSave(Resume r, SK object);

    protected abstract void doDelete(SK object);

    protected abstract Resume doGet(SK object);

    protected abstract List<Resume> doGetAllSorted();

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);
}
