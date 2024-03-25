package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.TimeDto;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.service.TimeService;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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

  @Override
  @Transactional
  public TimeDto createTime(LocalTime sunriseTime, LocalTime sunsetTime) {

    Time time;
    if ((time = timeRepository.findBySunriseTimeAndSunsetTime(sunriseTime, sunsetTime)) == null) {
      time = timeRepository.save(new Time(sunriseTime, sunsetTime));
    }

    cache.put(TIME_KEY + time.getId().toString(), time);

    log.info("Added new time: " + sunriseTime.toString() + " - " + sunsetTime.toString() + ".");

    return new TimeDto(sunriseTime, sunsetTime);
  }

  @Override
  public List<TimeDto> readAllTimes() {

    log.info("All times are shown.");

    return timeRepository.findAllTimes();
  }

  @Override
  public TimeDto getById(Integer id) {

    Time tempTime = (Time) cache.get(TIME_KEY + id.toString());

    if (tempTime == null) {
      tempTime = timeRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Time with id " + id + " not found."));

      cache.put(TIME_KEY + id, tempTime);
    }

    log.info("Time with id " + id + " is shown.");

    return new TimeDto(tempTime.getSunriseTime(), tempTime.getSunsetTime());
  }

  @Override
  @Transactional
  public TimeDto updateTime(Integer id, LocalTime sunriseTime, LocalTime sunsetTime) {

    Time time = (Time) cache.get(TIME_KEY + id);
    if (time == null) {
      time = timeRepository.findById(id).orElseThrow(
             () -> new NoSuchElementException("Time with id " + id + " not found."));

      cache.remove(TIME_KEY + id);
    }

    time.setSunriseTime(sunriseTime);
    time.setSunsetTime(sunsetTime);
    timeRepository.save(time);

    cache.put(TIME_KEY + id, time);

    log.info("Time with id " + id + " updated.");

    return new TimeDto(sunriseTime, sunsetTime);
  }

  @Override
  @SneakyThrows
  @Transactional
  public TimeDto deleteTime(Integer id) {

    Time time = (Time) cache.get(TIME_KEY + id);
    if (time == null) {
      time = timeRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException("Time with id " + id + " not found."));
    }

    if (time.getDates().isEmpty() && time.getLocations().isEmpty()) {
      timeRepository.delete(time);
      cache.remove(TIME_KEY + id);
    } else {
      throw new BadRequestException("Time with id " + id + " has connections.");
    }

    log.info("Time with id " + id + " deleted.");

    return new TimeDto(time.getSunriseTime(), time.getSunsetTime());
  }
}
