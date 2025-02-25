package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Companies implements Serializable {
    private String name;
    private String website;
    private List<Periods> periods;

    public Companies() {
    }

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

    public void addName(String name) {
        this.name = name;
    }

    public void addWebsite(String website) {
        this.website = website;
    }

    public void addPeriods(List<Periods> periods) {
        this.periods = periods;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Companies companies = (Companies) object;
        return Objects.equals(name, companies.name) &&
                Objects.equals(website, companies.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, website);
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
