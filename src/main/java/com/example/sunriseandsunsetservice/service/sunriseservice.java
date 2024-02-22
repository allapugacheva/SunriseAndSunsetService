package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.model.sunrisedata;

import java.time.LocalDate;
import java.util.List;

public interface sunriseservice {
    List<sunrisedata> findAllData();
    sunrisedata findTime(Double lat, Double lng, LocalDate date);
}
