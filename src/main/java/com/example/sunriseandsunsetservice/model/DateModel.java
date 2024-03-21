package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "date")
public class DateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "location_date_mapping",
            joinColumns =  @JoinColumn(name = "date_id") ,
            inverseJoinColumns =  @JoinColumn(name = "location_id"))
    private Set<LocationModel> locations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "date_time_mapping",
            joinColumns =  @JoinColumn(name = "date_id") ,
            inverseJoinColumns =  @JoinColumn(name = "sunrise_and_sunset_time_id"))
    private Set<TimeModel> times = new HashSet<>();

    public DateModel(LocalDate d) {
        this.date = d;
    }

    public void addTime(TimeModel timeModel) {
        this.times.add(timeModel);
    }

    public void deleteTime(TimeModel timeModel) { this.times.remove(timeModel); }

    public void addLocation(LocationModel locationModel) {
        this.locations.add(locationModel);
    }

    public void deleteLocation(LocationModel locationModel) { this.locations.remove(locationModel); }
}
