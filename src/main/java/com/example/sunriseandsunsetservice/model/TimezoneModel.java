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
@Table(name = "timezone")
@NoArgsConstructor
public class TimezoneModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String timezone;

    @OneToMany(mappedBy = "timezone", fetch = FetchType.LAZY)
    private Set<LocationModel> locations = new HashSet<>();

    public TimezoneModel(String t) {
        this.timezone = t;
    }

    public void addLocation(LocationModel locationModel) { this.locations.add(locationModel); }
}
