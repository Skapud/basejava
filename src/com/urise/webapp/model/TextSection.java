package com.urise.webapp.model;

import java.util.Objects;
import java.util.Optional;

public class TextSection extends Section {
    private final String text = "";

    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TextSection that = (TextSection) object;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
