package com.codecool.solarwatch.model.entity.sunset;

import com.codecool.solarwatch.model.entity.city.City;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "sunsets")
public class SunsetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String sunset;
    private LocalDate date;
    @ManyToOne
    private City city;

    public SunsetEntity() {
    }

    public SunsetEntity(long id, String sunset, LocalDate date, City city) {
        this.id = id;
        this.sunset = sunset;
        this.date = date;
        this.city = city;
    }

    public SunsetEntity(String sunset, LocalDate date, City city) {
        this.sunset = sunset;
        this.date = date;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getSunset() {
        return sunset;
    }

    public LocalDate getDate() {
        return date;
    }

    public City getCity() {
        return city;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "SunsetEntity{" +
                "id=" + id +
                ", sunset='" + sunset + '\'' +
                ", date=" + date +
                ", city=" + city +
                '}';
    }
}
