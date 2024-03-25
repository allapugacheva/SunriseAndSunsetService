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
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date object.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "date")
public class Date {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private LocalDate sunDate;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
        name = "location_date_mapping",
          joinColumns =  @JoinColumn(name = "date_id"),
        inverseJoinColumns =  @JoinColumn(name = "location_id"))
  private Set<Location> locations = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
        name = "date_time_mapping",
        joinColumns =  @JoinColumn(name = "date_id"),
        inverseJoinColumns =  @JoinColumn(name = "sunrise_and_sunset_time_id"))
  private Set<Time> times = new HashSet<>();

  public Date(LocalDate d) {

    this.sunDate = d;
  }

  public void addTime(Time time) {

    this.times.add(time);
  }

  public void deleteTime(Time time) {

    this.times.remove(time);
  }

  public void addLocation(Location location) {

    this.locations.add(location);
  }

  public void deleteLocation(Location location) {
    this.locations.remove(location);
  }
}
