package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.request.TimeRequest;
import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import java.time.LocalTime;
import java.util.List;

/**
 * Interface for time service.
 */
public interface TimeService {

  TimeResponse createTime(LocalTime sunriseTime, LocalTime sunsetTime);

  List<TimeResponse> createManyTimes(List<TimeRequest> times);

  List<TimeResponse> readAllTimes();

  TimeResponse getById(Integer id);

  TimeResponse updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime);

  TimeResponse deleteTime(Integer id);
}
