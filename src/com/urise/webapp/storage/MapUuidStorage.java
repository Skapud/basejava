package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {
    protected final Map<String, Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void doSave(Resume r, Object key) {
        map.put(r.getUuid(), r);
    }

    @Override
    public void doDelete(Object key) {
        map.remove((String) key);
    }

    @Override
    public void doUpdate(Resume r, Object key) {
        map.put(r.getUuid(), r);
    }

    @Override
    public Resume doGet(Object key) {
        return map.get((String) key);
    }

    @Override
    public Resume[] doGetAll() {
        return map.values().toArray(new Resume[0]);
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }

    public Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    public boolean isExist(Object searchKey) {
        return map.containsKey(searchKey);
    }
}
