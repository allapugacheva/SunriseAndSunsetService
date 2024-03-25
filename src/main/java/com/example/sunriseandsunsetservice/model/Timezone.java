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
public class Timezone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String timezone;

    @OneToMany(mappedBy = "timezone", fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    public Timezone(String t) {
        this.timezone = t;
    }

    public void addLocation(Location location) { this.locations.add(location); }
}
