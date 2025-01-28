package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Companies {
    private final String name;
    private final String website;
    private final List<Periods> periods;

    public Companies(String name, String website, List<Periods> periods) {
        this.name = name;
        this.website = website;
        this.periods = new ArrayList<>(periods);
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public List<Periods> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Companies companies = (Companies) object;
        return Objects.equals(name, companies.name) &&
                Objects.equals(website, companies.website) &&
                Objects.equals(periods, companies.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, website, periods);
    }

    @Override
    public String toString() {
        return "Companies{" +
                "name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", periods=" + periods +
                '}';
    }
}
