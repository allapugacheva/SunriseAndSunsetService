package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.TimezoneDto;
import java.util.List;

/**
 * Interface for timezone service.
 */
public interface TimezoneService {

  TimezoneDto createTimezone(String timezone);

  List<TimezoneDto> readAllTimezones();

  TimezoneDto getById(Integer id);

  TimezoneDto updateTimezone(Integer id, String timezone);

  TimezoneDto deleteTimezone(Integer id);
}
