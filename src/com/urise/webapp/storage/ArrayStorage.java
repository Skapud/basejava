package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int resumeCount;

    public void clear() {
        if (storage[0] == null) return;
        Arrays.fill(storage, 0, resumeCount, null);
        resumeCount = 0;
    }

    public void save(Resume r) {
        if (resumeCount >= storage.length) {
            System.out.println("Нет места, хранилище переполнено");
            return;
        }
        int index = find(r.toString());
        if (index >= 0) {
            System.out.println("Резюме " + r + " уже было добавлено");
            return;
        }
        storage[resumeCount++] = r;
    }

    public Resume get(String uuid) {
        int index = find(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("Резюме " + uuid + " не найдено");
        return null;
    }

    public void delete(String uuid) {
        int index = find(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index, resumeCount - index - 1);
            storage[--resumeCount] = null;
            return;
        }
        System.out.println("Резюме " + uuid + " не найдено");
    }

    public void update(Resume resume) {
        int index = find(resume.toString());
        if (index >= 0) {
            storage[index] = resume;
            return;
        }
        System.out.println("Резюме " + resume + " не найдено");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumeCount);
    }

    public int size() {
        return resumeCount;
    }

    private int find(String uuid) {
        for (int i = 0; i < resumeCount; i++) {
            if (storage[i].toString().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
