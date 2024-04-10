package com.example.sunriseandsunsetservice.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Sunrise and sunset request.
 */
@Getter
@Setter
@AllArgsConstructor
public class SunriseAndSunsetRequest {
  Double lat;
  Double lng;
  LocalDate date;
}
