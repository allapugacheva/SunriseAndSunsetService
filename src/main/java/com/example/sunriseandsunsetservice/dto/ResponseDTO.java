package com.example.sunriseandsunsetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO {
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDate date;
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;
}
