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
        return map.get(((Resume) key).getUuid());
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

    @Override
    public Object getSearchKey(String uuid) {
        return new Resume(uuid, "");
    }

    @Override
    public boolean isExist(Object searchKey) {
        return map.containsKey(((Resume) searchKey).getUuid());
    }
}
