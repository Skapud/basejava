package com.urise.webapp.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> list;

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> getLines() {
        return new ArrayList<>(list);
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "list=" + list +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ListSection that = (ListSection) object;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
