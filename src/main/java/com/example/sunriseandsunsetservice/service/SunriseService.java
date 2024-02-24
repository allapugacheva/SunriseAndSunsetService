package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.model.SunriseData;

import java.time.LocalDate;
import java.util.List;

public interface SunriseService {
    List<SunriseData> findAllData();
    SunriseData findTime(Double lat, Double lng, LocalDate date);
    void deleteData(Long id);
}
