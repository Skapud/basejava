package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected final ArrayList<Resume> list = new ArrayList<>();

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void doSave(Resume r, Object object) {
        list.add(r);
    }

    @Override
    public void doDelete(Object object) {
        list.remove((int) object);
    }

    @Override
    public void doUpdate(Resume r, Object object) {
        int index = (Integer) object;
        list.set(index, r);
    }

    @Override
    public Resume doGet(Object object) {
        return list.get((Integer) object);
    }

    @Override
    public Resume[] doGetAll() {
        return list.toArray(new Resume[0]);
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return new ArrayList<>(list);
    }

    @Override
    public int size() {
        return list.size();
    }

    public Object getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }
}
