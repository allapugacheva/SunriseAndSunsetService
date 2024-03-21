package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.dto.TimeDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.TimeModel;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.service.TimeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;

    @Override
    @Transactional
    public TimeDTO createTime(LocalTime sunriseTime, LocalTime sunsetTime) {

        if (timeRepository.findBySunriseTimeAndSunsetTime(sunriseTime, sunsetTime) == null)
            timeRepository.save(new TimeModel(sunriseTime, sunsetTime));

        return new TimeDTO(sunriseTime, sunsetTime);
    }

    @Override
    public List<TimeDTO> readAllTimes() {

        List<TimeDTO> times = new ArrayList<>();

        for(TimeModel tm : timeRepository.findAll())
            times.add(new TimeDTO(tm.getSunriseTime(), tm.getSunsetTime()));

        return times;
    }

    @Override
    @Transactional
    public TimeDTO updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime) {

        TimeModel timeModel = timeRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        timeModel.setSunriseTime(sunriseTime);
        timeModel.setSunsetTime(sunsetTime);
        timeRepository.save(timeModel);

        return new TimeDTO(sunriseTime, sunsetTime);
    }

    @Override
    @Transactional
    public TimeDTO deleteTime(Integer id) {

        TimeModel timeModel = timeRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (timeModel.getDates().isEmpty() && timeModel.getLocations().isEmpty())
            timeRepository.delete(timeModel);
        else throw new MyRuntimeException("Time has connections.");

        return new TimeDTO(timeModel.getSunriseTime(), timeModel.getSunsetTime());
    }
}
