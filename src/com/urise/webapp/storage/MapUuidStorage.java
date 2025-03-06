package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    protected final Map<String, Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void doSave(Resume resume, String key) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public void doDelete(String key) {
        map.remove(key);
    }

    @Override
    public void doUpdate(Resume resume, String key) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public Resume doGet(String key) {
        return map.get(key);
    }

    @Override
    public List<Resume> doGetAllSorted() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }

    public String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    public boolean isExist(String key) {
        return map.containsKey(key);
    }
}
