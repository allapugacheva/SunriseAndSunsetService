package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "sunrise_and_sunset_time")
@NoArgsConstructor
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalTime sunriseTime;

    private LocalTime sunsetTime;

    @ManyToMany(mappedBy = "times", fetch = FetchType.LAZY)
    private Set<Date> dates = new HashSet<>();

    @ManyToMany(mappedBy = "times", fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    public Time(LocalTime sunrise, LocalTime sunset) {
        this.sunriseTime = sunrise;
        this.sunsetTime = sunset;
    }

    public void addDate(Date date) {
        this.dates.add(date);
    }

    public void deleteDate(Date date) { this.dates.remove(date); }

    public void addLocation(Location location) {
        this.locations.add(location);
    }

    public void deleteLocation(Location location) { this.locations.remove(location); }
}
