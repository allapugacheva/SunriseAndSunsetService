package com.example.sunriseandsunsetservice.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for error handlers.
 */
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
  private LocalDateTime dateTime;
  private Integer status;
  private String errorText;
}
