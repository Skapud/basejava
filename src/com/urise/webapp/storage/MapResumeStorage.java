package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    protected final Map<String, Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void doSave(Resume resume, Resume key) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public void doDelete(Resume key) {
        map.remove(key.getUuid());
    }

    @Override
    public void doUpdate(Resume resume, Resume key) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public Resume doGet(Resume key) {
        return key;
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
    public Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    public boolean isExist(Resume key) {
        return key != null;
    }
}
