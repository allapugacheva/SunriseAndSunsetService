package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.TimezoneRequest;
import com.example.sunriseandsunsetservice.dto.response.TimezoneResponse;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.example.sunriseandsunsetservice.service.TimezoneService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of timezone service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TimezoneServiceImpl implements TimezoneService {

  private final TimezoneRepository timezoneRepository;

  private final InMemoryCache cache;

  private static final String TIMEZONE_KEY = "Timezone";
  private static final String TIMEZONE_INFO = "Time with id ";
  private static final String NOT_FOUND_STRING = " not found.";

  @Override
  @Transactional
  public TimezoneResponse createTimezone(String newTimezone) {

    Timezone timezone;
    if ((timezone = timezoneRepository.findBySunTimezone(newTimezone)) == null) {
      timezone = timezoneRepository.save(new Timezone(newTimezone));
    }

    cache.put(TIMEZONE_KEY + timezone.getId().toString(), timezone);

    return new TimezoneResponse(newTimezone);
  }

  @Override
  @Transactional
  public List<TimezoneResponse> createManyTimezones(List<TimezoneRequest> timezones) {

    return timezones.stream()
            .map(timezoneRequest -> createTimezone(timezoneRequest.getTimezone()))
            .collect(Collectors.toList());
  }

  @Override
  public List<TimezoneResponse> readAllTimezones() {

    return timezoneRepository.findAllTimezones();
  }

  @Override
  public TimezoneResponse getById(Integer id) {

    Timezone tempTimezone = (Timezone) cache.get(TIMEZONE_KEY + id.toString());

    if (tempTimezone == null) {
      tempTimezone = timezoneRepository.findById(id).orElseThrow(
               () -> new NoSuchElementException(TIMEZONE_INFO + id + NOT_FOUND_STRING));

      cache.put(TIMEZONE_KEY + id, tempTimezone);
    }

    return new TimezoneResponse(tempTimezone.getSunTimezone());
  }

  @Override
  @Transactional
  public TimezoneResponse updateTimezone(Integer id, String newTimezone) {

    Timezone timezone = (Timezone) cache.get(TIMEZONE_KEY + id);
    if (timezone == null) {
      timezone = timezoneRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(TIMEZONE_INFO + id + NOT_FOUND_STRING));
    }

    cache.remove(TIMEZONE_KEY + id);

    timezone.setSunTimezone(newTimezone);
    timezoneRepository.save(timezone);

    cache.put(TIMEZONE_KEY + id, timezone);

    return new TimezoneResponse(newTimezone);
  }

  @Override
  @SneakyThrows
  @Transactional
  public TimezoneResponse deleteTimezone(Integer id) {

    Timezone timezone = (Timezone) cache.get(TIMEZONE_KEY + id);
    if (timezone == null) {
      timezone = timezoneRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(TIMEZONE_INFO + id + NOT_FOUND_STRING));
    }

    if (timezone.getLocations().isEmpty()) {
      timezoneRepository.deleteById(id);
      cache.remove(TIMEZONE_KEY + id);
    } else {
      throw new IllegalArgumentException(TIMEZONE_INFO + id + " has connections.");
    }

    return new TimezoneResponse(timezone.getSunTimezone());
  }
}