package com.example.sunriseandsunsetservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Location request.
 */
@Getter
@Setter
@AllArgsConstructor
public class LocationRequest {
  Double lat;
  Double lng;
}
