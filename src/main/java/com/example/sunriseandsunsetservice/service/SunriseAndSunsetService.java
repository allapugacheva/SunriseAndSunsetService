package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.ResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface SunriseAndSunsetService {

    ResponseDTO findSunriseAndSunsetTime(Double lat, Double lng, LocalDate date);
    List<ResponseDTO> readAllSunrisesAnsSunsets();
    ResponseDTO updateSunriseAndSunset(Integer locationId, Integer dateId, Double lat, Double lng, LocalDate date);
    ResponseDTO deleteSunriseAndSunsetTime(Integer locationId, Integer dateId);
}
