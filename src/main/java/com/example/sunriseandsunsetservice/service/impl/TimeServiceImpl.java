package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.TimeDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.service.TimeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;

    private final InMemoryCache cache;

    private final static String key = "Time";

    @Override
    @Transactional
    public TimeDTO createTime(LocalTime sunriseTime, LocalTime sunsetTime) {

        Time time;
        if ((time = timeRepository.findBySunriseTimeAndSunsetTime(sunriseTime, sunsetTime)) == null)
            time = timeRepository.save(new Time(sunriseTime, sunsetTime));

        cache.put(key + time.getId().toString(), time);

        return new TimeDTO(sunriseTime, sunsetTime);
    }

    @Override
    public List<TimeDTO> readAllTimes() { return timeRepository.findAllTimes(); }

    @Override
    public TimeDTO getById(Integer id) {

        Time tempTime = (Time) cache.get(key + id.toString());

        if(tempTime == null) {
            tempTime = timeRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Time not found."));

            cache.put(key + id, tempTime);
        }

        return new TimeDTO(tempTime.getSunriseTime(), tempTime.getSunsetTime());
    }

    @Override
    @Transactional
    public TimeDTO updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime) {

        Time time = (Time) cache.get(key + id);
        if(time == null) {
            time = timeRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Wrong id."));
            cache.remove(key + id);
        }

        time.setSunriseTime(sunriseTime);
        time.setSunsetTime(sunsetTime);
        timeRepository.save(time);

        cache.put(key + id, time);

        return new TimeDTO(sunriseTime, sunsetTime);
    }

    @Override
    @Transactional
    public TimeDTO deleteTime(Integer id) {

        Time time = (Time) cache.get(key + id);
        if(time == null)
            time = timeRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (time.getDates().isEmpty() && time.getLocations().isEmpty()) {
            timeRepository.delete(time);
            cache.remove(key + id);
        }
        else throw new MyRuntimeException("Time has connections.");

        return new TimeDTO(time.getSunriseTime(), time.getSunsetTime());
    }
}
