package com.urise.webapp.model;

import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;
import static com.urise.webapp.util.DateUtil.of;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Periods implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final Periods EMPTY = new Periods();

    private String title;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;
    private String description;

    public Periods() {
    }

    public Periods(String title, int startYear, Month startMonth, String description) {
        this(title, of(startYear, startMonth), NOW, description);
    }

    public Periods(String title, int startYear, Month startMonth, int endYear, Month endMonth, String description) {
        this(title, of(startYear, startMonth), of(endYear, endMonth), description);
    }

    public Periods(String title, LocalDate startDate, LocalDate endDate, String description) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description == null ? "" : description;
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
