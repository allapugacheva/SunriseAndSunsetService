package com.example.sunriseandsunsetservice.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for sunrise and sunset response.
 */
@Getter
@Setter
@AllArgsConstructor
public class SunriseAndSunsetResponse {
  private String location;
  private Double latitude;
  private Double longitude;
  private LocalDate date;
  private LocalTime sunriseTime;
  private LocalTime sunsetTime;
}
