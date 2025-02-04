package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Periods {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;

    public Periods(String title, LocalDate startDate, LocalDate endDate, String description) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Periods periods = (Periods) object;
        return Objects.equals(title, periods.title) &&
                Objects.equals(startDate, periods.startDate) &&
                Objects.equals(endDate, periods.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Periods{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                '}';
    }
}
