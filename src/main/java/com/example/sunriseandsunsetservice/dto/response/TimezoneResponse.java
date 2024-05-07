package com.example.sunriseandsunsetservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for timezone.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimezoneResponse {
  private Integer id;
  private String timezone;
}
