package com.example.sunriseandsunsetservice.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for daytime.
 */
@Getter
@Setter
@AllArgsConstructor
public class DaytimeDto {
  LocalTime duration;
}
