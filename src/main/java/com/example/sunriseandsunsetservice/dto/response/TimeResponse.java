package com.example.sunriseandsunsetservice.dto.response;

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
public class TimeResponse {
  private Integer id;
  private LocalTime sunriseTime;
  private LocalTime sunsetTime;
}
