package com.example.SunriseAndSunsetService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
public class SunriseData {
    @Id
    @GeneratedValue
    private Long id;
    private Double lat;
    private Double lng;
    private LocalDate date;
    private String place;
    private LocalTime sunrise;
    private LocalTime sunset;

    public SunriseData(Double lat, Double lng, LocalDate date, String place, LocalTime sunrise, LocalTime sunset){
        this.lat = lat;
        this.lng = lng;
        this.date = date;
        this.place = place;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
