package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.LocationRequest;
import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import com.example.sunriseandsunsetservice.service.LocationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
  public LocationResponse createLocation(Double lat, Double lng) {

    if (commonService.notValidLat(lat) || commonService.notValidLng(lng)) {
      throw new RuntimeException("Invalid latitude or longitude");
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

    return new LocationResponse(location.getSunLocation(), lat, lng);
  }

  @Override
  @Transactional
  public List<LocationResponse> createManyLocations(List<LocationRequest> locations) {

    return locations.stream()
            .map(locationRequest -> createLocation(locationRequest.getLat(),
                                                   locationRequest.getLng()))
            .collect(Collectors.toList());
  }

  @Override
  public List<LocationResponse> readAllLocations() {

    return locationRepository.findAllLocations();
  }

  @Override
  public LocationResponse getById(Integer id) {

    Location tempLocation = (Location) cache.get(LOCATION_KEY + id.toString());

    if (tempLocation == null) {
      tempLocation = locationRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(LOCATION_INFO + id + " not found."));

      cache.put(LOCATION_KEY + id, tempLocation);
    }

    return new LocationResponse(tempLocation.getSunLocation(), tempLocation.getLatitude(),
        tempLocation.getLongitude());
  }

  @Override
  @Transactional
  public LocationResponse updateLocation(Integer id, Double lat, Double lng) {

    return new LocationResponse(commonService.updateLocation(id, lat, lng), lat, lng);
  }

  @Override
  @Transactional
  public LocationResponse deleteLocation(Integer id) {

    Location location = locationRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException(LOCATION_INFO + id + " not found."));

    if (location.getDates().isEmpty() && location.getTimes().isEmpty()) {
      locationRepository.delete(location);
      cache.remove(LOCATION_KEY + id);

    } else {
      throw new RuntimeException(LOCATION_INFO + id + " has connections.");
    }

    return new LocationResponse(location.getSunLocation(), location.getLatitude(),
        location.getLongitude());
  }
}