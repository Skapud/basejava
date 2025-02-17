package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
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
        map.remove(((Resume) key).getUuid());
    }

    @Override
    public void doUpdate(Resume r, Object key) {
        map.put(r.getUuid(), r);
    }

    @Override
    public Resume doGet(Object key) {
        return (Resume) key;
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Object getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    public boolean isExist(Object searchKey) {
        return searchKey != null;
    }
}
