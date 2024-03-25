package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Time object.
 */
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

  public void deleteDate(Date date) {
    this.dates.remove(date);
  }

  public void addLocation(Location location) {

    this.locations.add(location);
  }

  public void deleteLocation(Location location) {
    this.locations.remove(location);
  }
}
