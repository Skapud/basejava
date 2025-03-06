package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializers.Serializer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final Serializer streamSerializer;

    protected PathStorage(String path, Serializer streamSerializer) {
        Objects.requireNonNull(path, "directory must not be null");
        this.streamSerializer = streamSerializer;
        this.directory = Paths.get(path);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(path + " is not directory or is not witable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null, e);
        }
    }

    @Override
    public int size() {
        List<Path> paths = getPaths();
        return paths.size();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            streamSerializer.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            streamSerializer.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Couldn't create path " + path, getFileName(path), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Read error", getFileName(path), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path hasn't been deleted", getFileName(path), e);
        }
    }

    @Override
    protected List<Resume> doGetAllSorted() {
        List<Resume> list = new ArrayList<>();
        List<Path> paths = getPaths();
        for (Path path : paths) {
            if (Files.isRegularFile(path)) {
                list.add(doGet(path));
            }
        }
        return list;
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private List<Path> getPaths() {
        try {
            return Files.list(directory).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Read error", null, e);
        }
    }
}