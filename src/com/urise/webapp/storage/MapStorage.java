package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.HashMap;

public class MapStorage extends AbstractStorage {
    protected final HashMap<String, Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void doSave(Resume r, Object object) {
        map.put(object.toString(), r);
    }

    @Override
    public void doDelete(Object object) {
        map.remove((String) object);
    }

    @Override
    public void doUpdate(Resume r, Object object) {
        map.replace((String) object, r);
    }

    @Override
    public Resume doGet(Object object) {
        return map.get((String) object);
    }

    @Override
    public Resume[] doGetAll() {
        return map.values().toArray(new Resume[0]);
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
