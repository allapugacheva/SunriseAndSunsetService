package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Location object.
 */
@Entity
@Getter
@Setter
@Table(name = "location")
@NoArgsConstructor
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String sunLocation;

  private Double latitude;

  private Double longitude;

  @ManyToOne
  @JoinColumn(name = "timezone_id")
  private Timezone timezone;

  @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
  private Set<Date> dates = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
        name = "location_time_mapping",
        joinColumns =  @JoinColumn(name = "location_id"),
        inverseJoinColumns =  @JoinColumn(name = "sunrise_and_sunset_time_id"))
  private Set<Time> times = new HashSet<>();

  /**
   * Constructor for location.
   */
  public Location(String l, Double lat, Double lng) {
    this.sunLocation = l;
    this.latitude = lat;
    this.longitude = lng;
  }

  public void addDate(Date date) {

    this.dates.add(date);
  }

  public void deleteDate(Date date) {

    this.dates.remove(date);
  }

  public void addTime(Time time) {

    this.times.add(time);
  }

  public void deleteTime(Time time) {

    this.times.remove(time);
  }
}
