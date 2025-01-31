package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimelineSection extends Section {
    private final List<Companies> companies = new ArrayList<>();

    public List<Companies> getCompanies() {
        return new ArrayList<>(companies);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TimelineSection that = (TimelineSection) object;
        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(companies);
    }
}
