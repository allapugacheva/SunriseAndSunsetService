package com.example.sunriseandsunsetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeDTO {
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;
}
