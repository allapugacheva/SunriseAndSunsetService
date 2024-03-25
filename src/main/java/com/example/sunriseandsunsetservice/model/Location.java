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
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String location;

    private Double latitude;

    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "timezone_id")
    private Timezone timezone;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<Date> dates = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "location_time_mapping",
            joinColumns =  @JoinColumn(name = "location_id") ,
            inverseJoinColumns =  @JoinColumn(name = "sunrise_and_sunset_time_id"))
    private Set<Time> times = new HashSet<>();

    public Location(String l, Double lat, Double lng) {
        this.location = l;
        this.latitude = lat;
        this.longitude = lng;
    }

    public void addDate(Date date) {
        this.dates.add(date);
    }

    public void deleteDate(Date date) { this.dates.remove(date); }

    public void addTime(Time time) {
        this.times.add(time);
    }

    public void deleteTime(Time time) { this.times.remove(time); }
}
