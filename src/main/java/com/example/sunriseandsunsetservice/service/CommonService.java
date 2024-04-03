package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Common for some services functions.
 */
@Slf4j
@Getter
@Setter
@Component
@AllArgsConstructor
public class CommonService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final TimeRepository timeRepository;
  private final DateRepository dateRepository;
  private final LocationRepository locationRepository;
  private final TimezoneRepository timezoneRepository;

  private final InMemoryCache cache;

  /**
   * Get timezone and place for coordinates.
   */
  @SneakyThrows
  public String[] getTimezoneAndPlace(Double lat, Double lng) {

    String url = "https://htmlweb.ru/json/geo/timezone/" + lat.toString() + "," + lng.toString();

    JsonNode root = objectMapper.readTree(restTemplate.getForEntity(url, String.class).getBody());

    return new String[]{root.path("name").asText(), root.path("city").path("name").asText()};
  }

  public boolean notValidLat(Double lat) {

    return lat < -90 || lat > 90;
  }

  public boolean notValidLng(Double lng) {

    return lng < -180 || lng > 180;
  }

  /**
   * Get sunrise and sunset time for coordinates.
   */
  @SneakyThrows
  public Time getSunriseAndSunsetTime(Double lat, Double lng, LocalDate date, String timezone) {

    String url = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng
            + "&date=" + date + "&formatted=0&tzid=" + timezone;

    JsonNode root = objectMapper.readTree(restTemplate.getForEntity(url, String.class).getBody());

    String[] sunriseAndSunsetTime = new String[]{root.path("results")
            .path("sunrise").asText().substring(11, 19),
            root.path("results").path("sunset").asText().substring(11, 19)};

    Time time = timeRepository.findBySunriseTimeAndSunsetTime(LocalTime
            .parse(sunriseAndSunsetTime[0]), LocalTime.parse(sunriseAndSunsetTime[1]));
    if (time == null) {
      time = timeRepository.save(new Time(LocalTime.parse(sunriseAndSunsetTime[0]),
              LocalTime.parse(sunriseAndSunsetTime[1])));
    }

    return time;
  }

  /**
   * Remove time from date and location.
   */
  public void clearTime(Date date, Location location, Time time) {
    if (time == null) {
      throw new NoSuchElementException("Date with id " + date.getId()
              + " and location with id " + location.getId() + " have no common time.");
    }

    date.deleteTime(time);
    location.deleteTime(time);
    time.deleteDate(date);
    time.deleteLocation(location);

    if (time.getLocations().isEmpty() && time.getDates().isEmpty()) {
      timeRepository.delete(time);
      cache.remove("Time" + time.getId().toString());
    }
  }

  /**
   * Updating date for many objects.
   */
  public void updateDate(Integer id, LocalDate newDate) {

    Date date = (Date) cache.get("Date" + id);
    if (date == null) {
      date = dateRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException("Date with id " + id + " not found."));
    }

    boolean flagDeleteDate = true;

    Date tempDate;
    if ((tempDate = dateRepository.findBySunDate(newDate)) == null) {
      date.setSunDate(newDate);

      flagDeleteDate = false;
    }

    for (Location tempLocation : date.getLocations()) {

      clearTime(date, tempLocation, timeRepository.findCommonTime(id, tempLocation.getId()));

      Time time = getSunriseAndSunsetTime(tempLocation.getLatitude(), tempLocation.getLongitude(),
              date.getSunDate(), tempLocation.getTimezone().getSunTimezone());

      if (flagDeleteDate) {
        tempLocation.addDate(tempDate);
        tempDate.addLocation(tempLocation);
      }

      tempLocation.addTime(time);
      time.addLocation(tempLocation);

      if (flagDeleteDate) {
        tempDate.addTime(time);
        time.addDate(tempDate);
      } else {
        date.addTime(time);
        time.addDate(date);
      }
    }

    cache.remove("Date" + id);

    if (flagDeleteDate) {
      dateRepository.delete(date);
      dateRepository.save(tempDate);
      cache.put("Date" + tempDate.getId().toString(), tempDate);
    } else {
      dateRepository.save(date);
      cache.put("Date" + date.getId().toString(), date);
    }
  }

  /**
   * Updating location for many objects.
   */
  public String updateLocation(Integer id, Double lat, Double lng) {
    String key = "Location";

    Location location = (Location) cache.get(key + id);
    if (location == null) {
      location = locationRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException("Location with id " + id + " not found."));
    }

    boolean flagDeleteLocation = true;

    Location tempLocation;
    Timezone tempTimezone;
    if ((tempLocation = locationRepository.findByLatitudeAndLongitude(lat, lng)) == null) {
      String[] timezoneAndPlace = getTimezoneAndPlace(lat, lng);

      if ((tempTimezone = timezoneRepository.findBySunTimezone(timezoneAndPlace[0])) == null) {
        tempTimezone = timezoneRepository.save(new Timezone(timezoneAndPlace[0]));
      }

      tempTimezone.addLocation(location);
      location.setTimezone(tempTimezone);
      location.setSunLocation(timezoneAndPlace[1]);
      location.setLatitude(lat);
      location.setLongitude(lng);

      flagDeleteLocation = false;
    } else {
      tempTimezone = tempLocation.getTimezone();
    }

    for (Date tempDate : location.getDates()) {

      clearTime(tempDate, location, timeRepository.findCommonTime(tempDate.getId(), id));

      Time time = getSunriseAndSunsetTime(location.getLatitude(),
           location.getLongitude(), tempDate.getSunDate(), tempTimezone.getSunTimezone());

      if (flagDeleteLocation) {
        tempLocation.addDate(tempDate);
        tempDate.addLocation(tempLocation);
      }

      tempDate.addTime(time);
      time.addDate(tempDate);

      if (flagDeleteLocation) {
        tempDate.addLocation(tempLocation);
        tempLocation.addTime(time);
      } else {
        tempDate.addLocation(location);
        location.addTime(time);
      }
    }

    cache.remove(key + id);

    if (flagDeleteLocation) {
      locationRepository.delete(location);
      locationRepository.save(tempLocation);

      cache.put(key + tempLocation.getId().toString(), tempLocation);

      return tempLocation.getSunLocation();
    } else {
      locationRepository.save(location);

      cache.put(key + location.getId().toString(), location);

      return location.getSunLocation();
    }
  }
}