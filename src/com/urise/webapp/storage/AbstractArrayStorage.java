package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = getIndex(r.toString());
        if (index >= 0) {
            storage[index] = r;
            return;
        }
        System.out.println("Резюме " + r + " не найдено");
    }

    public void save(Resume r) {
        if (size >= storage.length) {
            System.out.println("Нет места, хранилище переполнено");
            return;
        }
        if (getIndex(r.toString()) >= 0) {
            System.out.println("Резюме " + r + " уже было добавлено");
            return;
        }
        add(r);
        size++;
    }

    protected abstract void add(Resume r);

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("Резюме " + uuid + " не найдено");
        return null;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            moveArray(index);
            storage[size - 1] = null;
            size--;
            return;
        }
        System.out.println("Резюме " + uuid + " не найдено");
    }

    protected abstract void moveArray(int index);

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int getIndex(String uuid);

    public int size() {
        return size;
    }
}
