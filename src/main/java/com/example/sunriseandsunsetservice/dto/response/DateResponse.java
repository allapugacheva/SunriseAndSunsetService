package com.example.sunriseandsunsetservice.dto.response;

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
public class DateResponse {
  private Integer id;
  private LocalDate date;
}
