package com.example.sunriseandsunsetservice.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for date.
 */
@Getter
@Setter
@AllArgsConstructor
public class DateDto {
  LocalDate date;
}
