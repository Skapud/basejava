package com.urise.webapp.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimelineSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Companies> companies;

    public TimelineSection() {
    }

    public TimelineSection(List<Companies> companies) {
        this.companies = companies;
    }

    public List<Companies> getCompanies() {
        return new ArrayList<>(companies);
    }

    @Override
    public String toString() {
        return "TimelineSection{" +
                "companies=" + companies +
                "} " + super.toString();
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
