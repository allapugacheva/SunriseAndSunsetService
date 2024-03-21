package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.TimeDTO;
import java.time.LocalTime;
import java.util.List;

public interface TimeService {

    TimeDTO createTime(LocalTime sunriseTime, LocalTime sunsetTime);
    List<TimeDTO> readAllTimes();
    TimeDTO updateTime(int id, LocalTime sunriseTime, LocalTime sunsetTime);
    TimeDTO deleteTime(int id);
}