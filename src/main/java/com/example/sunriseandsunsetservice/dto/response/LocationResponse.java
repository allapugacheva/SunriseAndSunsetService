package com.example.sunriseandsunsetservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for location.
 */
@Getter
@Setter
@AllArgsConstructor
public class LocationResponse {
  private Integer id;
  private String location;
  private Double latitude;
  private Double longitude;
}
