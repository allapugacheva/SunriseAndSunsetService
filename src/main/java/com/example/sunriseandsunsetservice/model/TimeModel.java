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
public class TimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalTime sunriseTime;

    private LocalTime sunsetTime;

    @ManyToMany(mappedBy = "times", fetch = FetchType.LAZY)
    private Set<DateModel> dates = new HashSet<>();

    @ManyToMany(mappedBy = "times", fetch = FetchType.LAZY)
    private Set<LocationModel> locations = new HashSet<>();

    public TimeModel(LocalTime sunrise, LocalTime sunset) {
        this.sunriseTime = sunrise;
        this.sunsetTime = sunset;
    }

    public void addDate(DateModel dateModel) {
        this.dates.add(dateModel);
    }

    public void deleteDate(DateModel dateModel) { this.dates.remove(dateModel); }

    public void addLocation(LocationModel locationModel) {
        this.locations.add(locationModel);
    }

    public void deleteLocation(LocationModel locationModel) { this.locations.remove(locationModel); }
}
