package com.example.sunriseandsunsetservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Timezone object.
 */
@Entity
@Getter
@Setter
@Table(name = "timezone")
@NoArgsConstructor
public class Timezone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String sunTimezone;

  @OneToMany(mappedBy = "timezone", fetch = FetchType.LAZY)
  private Set<Location> locations = new HashSet<>();

  public Timezone(String t) {

    this.sunTimezone = t;
  }

  public void addLocation(Location location) {

    this.locations.add(location);
  }
}
