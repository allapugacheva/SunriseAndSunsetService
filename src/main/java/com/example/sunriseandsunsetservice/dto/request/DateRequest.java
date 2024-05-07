package com.example.sunriseandsunsetservice.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRequest {
  LocalDate date;
}
