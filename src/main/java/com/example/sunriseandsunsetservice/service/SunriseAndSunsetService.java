package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.request.SunriseAndSunsetRequest;
import com.example.sunriseandsunsetservice.dto.response.DaytimeResponse;
import com.example.sunriseandsunsetservice.dto.response.SunriseAndSunsetResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for sunrise and sunset service.
 */
public interface SunriseAndSunsetService {

  SunriseAndSunsetResponse findSunriseAndSunsetTime(Double lat, Double lng, LocalDate date);

  List<SunriseAndSunsetResponse> findManySunriseAndSunsetTimes(List<SunriseAndSunsetRequest> data);

  List<SunriseAndSunsetResponse> readAllSunrisesAnsSunsets();

  SunriseAndSunsetResponse getById(Integer locationId, Integer dateId);

  SunriseAndSunsetResponse updateSunriseAndSunset(Integer locationId, Integer dateId, Double lat,
                                                  Double lng, LocalDate date);

  SunriseAndSunsetResponse deleteSunriseAndSunsetTime(Integer locationId, Integer dateId);

  DaytimeResponse findDaytimeLength(Integer dateId, Integer locationId);
}
