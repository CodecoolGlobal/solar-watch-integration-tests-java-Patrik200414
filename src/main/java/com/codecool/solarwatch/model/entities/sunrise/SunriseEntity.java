package com.codecool.solarwatch.model.entities.sunrise;

import com.codecool.solarwatch.model.entities.city.City;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sunrises")
public class SunriseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String sunrise;
    private LocalDate date;
    @ManyToOne
    private City city;

    public SunriseEntity() {
    }

    public SunriseEntity(long id, String sunrise, LocalDate date, City city) {
        this.id = id;
        this.sunrise = sunrise;
        this.date = date;
        this.city = city;
    }

    public SunriseEntity(String sunrise, LocalDate date, City city) {
        this.sunrise = sunrise;
        this.date = date;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getSunrise() {
        return sunrise;
    }

    public LocalDate getDate() {
        return date;
    }

    public City getCity() {
        return city;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "SunriseEntity{" +
                "id=" + id +
                ", sunrise='" + sunrise + '\'' +
                ", date=" + date +
                ", city=" + city +
                '}';
    }
}
