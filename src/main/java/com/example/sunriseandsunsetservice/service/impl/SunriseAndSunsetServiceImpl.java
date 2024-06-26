package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.SunriseAndSunsetRequest;
import com.example.sunriseandsunsetservice.dto.response.DaytimeResponse;
import com.example.sunriseandsunsetservice.dto.response.SunriseAndSunsetResponse;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import com.example.sunriseandsunsetservice.service.SunriseAndSunsetService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Implementation of sunrise and sunset service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SunriseAndSunsetServiceImpl implements SunriseAndSunsetService {
  private final DateRepository dateRepository;
  private final LocationRepository locationRepository;
  private final TimeRepository timeRepository;
  private final TimezoneRepository timezoneRepository;

  private final CommonService commonService;
  private final InMemoryCache cache;

  private static final String LOCATION_KEY = "Location";
  private static final String DATE_KEY = "Date";
  private static final String NOT_FOUND_STRING = " not found.";
  private static final String DATE_INFO = "Date with id ";

  @Override
  @SneakyThrows
  @Transactional
  public SunriseAndSunsetResponse findSunriseAndSunsetTime(Double lat,
                                                           Double lng,
                                                           LocalDate newDate) {

    if (commonService.notValidLat(lat) || commonService.notValidLng(lng)) {
      throw new BadRequestException("Invalid latitude or longitude");
    }

    Timezone timezone;
    Location location = locationRepository.findByLatitudeAndLongitude(lat, lng);
    if (location == null) {

      String[] timezoneAndPlace = commonService.getTimezoneAndPlace(lat, lng);
      location = locationRepository.save(new Location(timezoneAndPlace[1], lat, lng));

      if ((timezone = timezoneRepository.findBySunTimezone(timezoneAndPlace[0])) == null) {
        timezone = timezoneRepository.save(new Timezone(timezoneAndPlace[0]));
      }
    } else {
      timezone = location.getTimezone();
    }

    Date date = dateRepository.findBySunDate(newDate);
    if (date == null) {
      date = dateRepository.save(new Date(newDate));
    }

    location.addDate(date);
    date.addLocation(location);

    Time time = commonService.getSunriseAndSunsetTime(lat, lng, newDate, timezone.getSunTimezone());

    date.addTime(time);
    time.addDate(date);

    timezone.addLocation(location);
    location.setTimezone(timezone);

    location.addTime(time);
    time.addLocation(location);

    locationRepository.save(location);

    cache.put(DATE_KEY + date.getId().toString(), date);
    cache.put(LOCATION_KEY + location.getId().toString(), location);

    return new SunriseAndSunsetResponse(location.getId(), date.getId(),
            location.getSunLocation(), location.getLatitude(),
            location.getLongitude(), date.getSunDate(), time.getSunriseTime(),
            time.getSunsetTime());
  }

  @Override
  @Transactional
  public List<SunriseAndSunsetResponse> findManySunriseAndSunsetTimes(
          List<SunriseAndSunsetRequest> data) {

    return data.stream()
            .map(dataRequest -> findSunriseAndSunsetTime(dataRequest.getLat(),
                                                         dataRequest.getLng(),
                                                         dataRequest.getDate()))
            .collect(Collectors.toList());
  }

  @Override
  public List<SunriseAndSunsetResponse> readAllSunrisesAnsSunsets() {

    return locationRepository.findAllData().stream()
            .map(row -> new SunriseAndSunsetResponse((Integer)row[0], (Integer)row[1],
                    (String) row[2], (Double) row[3], (Double) row[4],
                    ((java.sql.Date) row[5]).toLocalDate(),
                    ((java.sql.Time) row[6]).toLocalTime(),
                    ((java.sql.Time) row[7]).toLocalTime()))
            .toList();
  }

  @Override
  public SunriseAndSunsetResponse getById(Integer locationId, Integer dateId) {

    Location tempLocation = (Location) cache.get(LOCATION_KEY + locationId);
    if (tempLocation == null) {
      tempLocation = locationRepository.findById(locationId).orElseThrow(
             () -> new NoSuchElementException("Location with id " + locationId + NOT_FOUND_STRING));
      cache.put(LOCATION_KEY + locationId, tempLocation);
    }

    Date tempDate = (Date) cache.get(DATE_KEY + dateId);
    if (tempDate == null) {
      tempDate = dateRepository.findById(dateId).orElseThrow(
                () -> new NoSuchElementException(DATE_INFO + dateId + NOT_FOUND_STRING));
      cache.put(DATE_KEY + dateId, tempDate);
    }

    Time tempTime = timeRepository.findCommonTime(dateId, locationId);
    if (tempTime == null) {
      throw new IllegalArgumentException(DATE_INFO + dateId + " and location with id "
              + locationId + " have no common time.");
    }

    return new SunriseAndSunsetResponse(locationId, dateId,
            tempLocation.getSunLocation(), tempLocation.getLatitude(),
            tempLocation.getLongitude(), tempDate.getSunDate(),
            tempTime.getSunriseTime(), tempTime.getSunsetTime());
  }

  @Override
  @SneakyThrows
  @Transactional
  public SunriseAndSunsetResponse updateSunriseAndSunset(Integer locationId, Integer dateId,
                                                         Double lat, Double lng, LocalDate date) {
    if (commonService.notValidLat(lat) || commonService.notValidLng(lng)) {
      throw new BadRequestException("Invalid latitude or longitude");
    }

    deleteSunriseAndSunsetTime(locationId, dateId);
    return findSunriseAndSunsetTime(lat, lng, date);
  }

  @Override
  @SneakyThrows
  @Transactional
  public SunriseAndSunsetResponse deleteSunriseAndSunsetTime(Integer locationId, Integer dateId) {

    Date date = (Date) cache.get(DATE_KEY + dateId);
    if (date == null) {
      date = dateRepository.findById(dateId).orElseThrow(
            () -> new NoSuchElementException(DATE_INFO + dateId + NOT_FOUND_STRING));
    }

    Location location = (Location) cache.get(LOCATION_KEY + locationId);
    if (location == null) {
      location = locationRepository.findById(locationId).orElseThrow(
            () -> new NoSuchElementException("Location with id " + locationId + NOT_FOUND_STRING));
    }

    Time time = timeRepository.findCommonTime(dateId, locationId);
    if ((location.getDates().contains(date) && location.getTimes().contains(time))
            && (time.getDates().contains(date) && time.getLocations().contains(location))
            && (date.getTimes().contains(time) && date.getLocations().contains(location))) {

      date.deleteLocation(location);
      time.deleteLocation(location);

      location.deleteDate(date);
      time.deleteDate(date);

      location.deleteTime(time);
      date.deleteTime(time);

      locationRepository.save(location);

      cache.remove(DATE_KEY + dateId);
      cache.remove(LOCATION_KEY + locationId);
      cache.remove("Time" + time.getId());
    } else {
      throw new IllegalArgumentException("Data has connections.");
    }

    return new SunriseAndSunsetResponse(locationId, dateId,
            location.getSunLocation(), location.getLatitude(),
      location.getLongitude(), date.getSunDate(), time.getSunriseTime(),
      time.getSunsetTime());
  }

  @Override
  public DaytimeResponse findDaytimeLength(Integer dateId, Integer locationId) {

    DaytimeResponse response = (DaytimeResponse) cache.get("Daytime" + dateId + locationId);

    if (response == null) {
      response = new DaytimeResponse(Integer.valueOf(locationId.toString()
              + dateId.toString()), LocalTime.MIDNIGHT.plusSeconds(locationRepository
           .findDaytimeLength(dateId, locationId)));

      if (response.getDuration() == LocalTime.of(0, 0)) {
        throw new NoSuchElementException(DATE_INFO + dateId + " and location with id "
                + locationId + " have no common time.");
      }

      cache.put("Daytime" + dateId + locationId, response);
    }

    return response;
  }
}
