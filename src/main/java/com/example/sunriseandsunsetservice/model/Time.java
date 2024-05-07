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
import java.util.Objects;
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

  @ManyToMany(mappedBy = "times", fetch = FetchType.EAGER)
  private Set<Date> dates = new HashSet<>();

  @ManyToMany(mappedBy = "times", fetch = FetchType.EAGER)
  private Set<Location> locations = new HashSet<>();

  /**
   * Constructor.
   */
  public Time(LocalTime sunrise, LocalTime sunset) {

    this.id = 0;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Time time)) {
      return false;
    }
    return Objects.equals(id, time.id) && Objects.equals(sunriseTime, time.sunriseTime)
            && Objects.equals(sunsetTime, time.sunsetTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sunriseTime, sunsetTime);
  }
}
