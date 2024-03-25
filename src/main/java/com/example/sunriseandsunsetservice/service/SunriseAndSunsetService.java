package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.DaytimeDto;
import com.example.sunriseandsunsetservice.dto.ResponseDto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for sunrise and sunset service.
 */
public interface SunriseAndSunsetService {

  ResponseDto findSunriseAndSunsetTime(Double lat, Double lng, LocalDate date);

  List<ResponseDto> readAllSunrisesAnsSunsets();

  ResponseDto getById(Integer locationId, Integer dateId);

  ResponseDto updateSunriseAndSunset(Integer locationId, Integer dateId, Double lat,
                                     Double lng, LocalDate date);

  ResponseDto deleteSunriseAndSunsetTime(Integer locationId, Integer dateId);

  DaytimeDto findDaytimeLength(Integer dateId, Integer locationId);
}
