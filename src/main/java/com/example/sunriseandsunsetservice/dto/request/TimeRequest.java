package com.example.sunriseandsunsetservice.dto.request;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Time request.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimeRequest {
  LocalTime sunriseTime;
  LocalTime sunsetTime;
}
