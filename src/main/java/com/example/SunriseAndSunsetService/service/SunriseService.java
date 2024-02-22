package com.example.SunriseAndSunsetService.service;

import com.example.SunriseAndSunsetService.model.SunriseData;

import java.time.LocalDate;
import java.util.List;

public interface SunriseService {
    List<SunriseData> findAllData();
    SunriseData findTime(Double lat, Double lng, LocalDate date);
}
