package com.urise.webapp.model;

import java.util.Objects;
import java.util.Optional;

public class TextSection extends Section {
    private final String title = "";
    private final String text = "";

    public TextSection(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Optional<String> getText() {
        return Optional.of(text);
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "text='" + text + '\'' +
                "} " + super.toString();
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
