package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.TimeDto;
import java.time.LocalTime;
import java.util.List;

/**
 * Interface for time service.
 */
public interface TimeService {

  TimeDto createTime(LocalTime sunriseTime, LocalTime sunsetTime);

  List<TimeDto> readAllTimes();

  TimeDto getById(Integer id);

  TimeDto updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime);

  TimeDto deleteTime(Integer id);
}
