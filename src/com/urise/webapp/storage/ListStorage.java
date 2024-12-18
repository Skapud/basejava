package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.ArrayList;
import java.util.Iterator;

public class ListStorage extends AbstractStorage {
    protected final ArrayList<Resume> list = new ArrayList<>();
    protected Iterator<Resume> iterator = list.iterator();

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void doSave(Resume r, Object object) {
        Resume newResume = new Resume((String) object);
        list.add(newResume);
    }

    @Override
    public void doDelete(Object object) {
        list.remove((Resume) object);
    }

    @Override
    public void doUpdate(Resume key, Object object) {
        int index = list.indexOf(key);
        list.set(index, (Resume) object);
    }

    @Override
    public Resume doGet(Object object) {
        while (iterator.hasNext()) {
            Resume element = iterator.next();
            if (element.equals(object)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public boolean isExist(Object searchKey) {
        for (Resume element : list) {
            if (element.equals(searchKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Resume[] doGetAll() {
        return list.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return list.size();
    }

    public Object getSearchKey(String uuid) {
        while (iterator.hasNext()) {
            Resume resume = iterator.next();
            if (resume.getUuid().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }
}
