package com.codecool.solarwatch.model.entities.city;

import com.codecool.solarwatch.model.entities.sunrise.SunriseEntity;
import com.codecool.solarwatch.model.entities.sunset.SunsetEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cities")
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String state;
    private String country;
    private double longitude;
    private double latitude;
    @OneToMany(mappedBy = "city")
    private List<SunriseEntity> sunrises;
    @OneToMany(mappedBy = "city")
    private List<SunsetEntity> sunset;

    public City() {
    }

    public City(long id, String name, String state, String country, double longitude, double latitude, List<SunriseEntity> sunrises, List<SunsetEntity> sunset) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sunrises = sunrises;
        this.sunset = sunset;
    }

    public City(String name, String state, String country, double longitude, double latitude, List<SunriseEntity> sunrises, List<SunsetEntity> sunset) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sunrises = sunrises;
        this.sunset = sunset;
    }

    public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public List<SunriseEntity> getSunrise() {
        return new ArrayList<>(sunrises);
    }

    public List<SunsetEntity> getSunset() {
        return new ArrayList<>(sunset);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSunrises(List<SunriseEntity> sunrises) {
        this.sunrises = sunrises;
    }

    public void setSunset(List<SunsetEntity> sunset) {
        this.sunset = sunset;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof City city)) return false;
        return Double.compare(getLongitude(), city.getLongitude()) == 0 && Double.compare(getLatitude(), city.getLatitude()) == 0 && Objects.equals(getName(), city.getName()) && Objects.equals(getState(), city.getState()) && Objects.equals(getCountry(), city.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getState(), getCountry(), getLongitude(), getLatitude());
    }
}
