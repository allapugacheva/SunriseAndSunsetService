package com.example.sunriseandsunsetservice.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for time.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimeDto {
  private LocalTime sunriseTime;
  private LocalTime sunsetTime;
}
