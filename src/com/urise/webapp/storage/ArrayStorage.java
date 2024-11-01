package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

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
        storage[size++] = r;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            return;
        }
        System.out.println("Резюме " + uuid + " не найдено");
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
