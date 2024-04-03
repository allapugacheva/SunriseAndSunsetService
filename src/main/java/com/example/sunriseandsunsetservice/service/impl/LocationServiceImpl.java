package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.LocationDto;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import com.example.sunriseandsunsetservice.service.LocationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Implementation of location service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final LocationRepository locationRepository;
  private final CommonService commonService;

  private final InMemoryCache cache;

  private static final String LOCATION_KEY = "Location";
  private static final String LOCATION_INFO = "Location with id ";

  @Override
  @SneakyThrows
  @Transactional
  public LocationDto createLocation(Double lat, Double lng) {

    if (commonService.notValidLat(lat) || commonService.notValidLng(lng)) {
      throw new BadRequestException("Invalid latitude or longitude");
    }

    Location location;
    if ((location = locationRepository.findByLatitudeAndLongitude(lat, lng)) == null) {
      location = new Location();

      location.setLatitude(lat);
      location.setLongitude(lng);

      String[] place = commonService.getTimezoneAndPlace(lat, lng);
      location.setSunLocation(place[1]);

      locationRepository.save(location);
    }

    cache.put(LOCATION_KEY + location.getId().toString(), location);

    return new LocationDto(location.getSunLocation(), lat, lng);
  }

  @Override
  public List<LocationDto> readAllLocations() {

    return locationRepository.findAllLocations();
  }

  @Override
  public LocationDto getById(Integer id) {

    Location tempLocation = (Location) cache.get(LOCATION_KEY + id.toString());

    if (tempLocation == null) {
      tempLocation = locationRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(LOCATION_INFO + id + " not found."));

      cache.put(LOCATION_KEY + id, tempLocation);
    }

    return new LocationDto(tempLocation.getSunLocation(), tempLocation.getLatitude(),
        tempLocation.getLongitude());
  }

  @Override
  @Transactional
  public LocationDto updateLocation(Integer id, Double lat, Double lng) {

    return new LocationDto(commonService.updateLocation(id, lat, lng), lat, lng);
  }

  @Override
  @Transactional
  public LocationDto deleteLocation(Integer id) {

    Location location = locationRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException(LOCATION_INFO + id + " not found."));

    if (location.getDates().isEmpty() && location.getTimes().isEmpty()) {
      locationRepository.delete(location);
      cache.remove(LOCATION_KEY + id);

    } else {
      throw new NoSuchElementException(LOCATION_INFO + id + " has connections.");
    }

    return new LocationDto(location.getSunLocation(), location.getLatitude(),
        location.getLongitude());
  }
}