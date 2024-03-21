package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "location")
@NoArgsConstructor
public class LocationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String location;

    private Double latitude;

    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "timezone_id")
    private TimezoneModel timezone;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<DateModel> dates = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "location_time_mapping",
            joinColumns =  @JoinColumn(name = "location_id") ,
            inverseJoinColumns =  @JoinColumn(name = "sunrise_and_sunset_time_id"))
    private Set<TimeModel> times = new HashSet<>();

    public LocationModel(String l, Double lat, Double lng) {
        this.location = l;
        this.latitude = lat;
        this.longitude = lng;
    }

    public void addDate(DateModel dateModel) {
        this.dates.add(dateModel);
    }

    public void deleteDate(DateModel dateModel) { this.dates.remove(dateModel); }

    public void addTime(TimeModel timeModel) {
        this.times.add(timeModel);
    }

    public void deleteTime(TimeModel timeModel) { this.times.remove(timeModel); }
}
