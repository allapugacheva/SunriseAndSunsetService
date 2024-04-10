package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.TimeRequest;
import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.service.TimeService;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of time service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TimeServiceImpl implements TimeService {

  private final TimeRepository timeRepository;

  private final InMemoryCache cache;

  private static final String TIME_KEY = "Time";
  private static final String TIME_INFO = "Time with id ";
  private static final String NOT_FOUND_STRING = " not found.";

  @Override
  @Transactional
  public TimeResponse createTime(LocalTime sunriseTime, LocalTime sunsetTime) {

    Time time;
    if ((time = timeRepository.findBySunriseTimeAndSunsetTime(sunriseTime, sunsetTime)) == null) {
      time = timeRepository.save(new Time(sunriseTime, sunsetTime));
    }

    cache.put(TIME_KEY + time.getId().toString(), time);

    return new TimeResponse(sunriseTime, sunsetTime);
  }

  @Override
  @Transactional
  public List<TimeResponse> createManyTimes(List<TimeRequest> times) {

    return times.stream()
            .map(timeRequest -> createTime(timeRequest.getSunriseTime(),
                                           timeRequest.getSunsetTime()))
            .collect(Collectors.toList());
  }

  @Override
  public List<TimeResponse> readAllTimes() {

    return timeRepository.findAllTimes();
  }

  @Override
  public TimeResponse getById(Integer id) {

    Time tempTime = (Time) cache.get(TIME_KEY + id.toString());

    if (tempTime == null) {
      tempTime = timeRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException(TIME_INFO + id + NOT_FOUND_STRING));

      cache.put(TIME_KEY + id, tempTime);
    }

    return new TimeResponse(tempTime.getSunriseTime(), tempTime.getSunsetTime());
  }

  @Override
  @Transactional
  public TimeResponse updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime) {

    Time time = (Time) cache.get(TIME_KEY + id);
    if (time == null) {
      time = timeRepository.findById(id).orElseThrow(
             () -> new NoSuchElementException(TIME_INFO + id + NOT_FOUND_STRING));

      cache.remove(TIME_KEY + id);
    }

    time.setSunriseTime(sunriseTime);
    time.setSunsetTime(sunsetTime);
    timeRepository.save(time);

    cache.put(TIME_KEY + id, time);

    return new TimeResponse(sunriseTime, sunsetTime);
  }

  @Override
  @SneakyThrows
  @Transactional
  public TimeResponse deleteTime(Integer id) {

    Time time = (Time) cache.get(TIME_KEY + id);
    if (time == null) {
      time = timeRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(TIME_INFO + id + NOT_FOUND_STRING));
    }

    if (time.getDates().isEmpty() && time.getLocations().isEmpty()) {
      timeRepository.delete(time);
      cache.remove(TIME_KEY + id);
    } else {
      throw new IllegalArgumentException(TIME_INFO + id + " has connections.");
    }

    return new TimeResponse(time.getSunriseTime(), time.getSunsetTime());
  }
}
