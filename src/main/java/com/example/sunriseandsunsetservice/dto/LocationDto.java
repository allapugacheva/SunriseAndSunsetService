package com.example.sunriseandsunsetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for location.
 */
@Getter
@Setter
@AllArgsConstructor
public class LocationDto {
  private String location;
  private Double latitude;
  private Double longitude;
}
