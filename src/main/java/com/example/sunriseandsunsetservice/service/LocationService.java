package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.LocationDto;
import java.util.List;

/**
 * Interface for location service.
 */
public interface LocationService {

  LocationDto createLocation(Double lat, Double lng);

  List<LocationDto> readAllLocations();

  LocationDto getById(Integer id);

  LocationDto updateLocation(Integer id, Double lat, Double lng);

  LocationDto deleteLocation(Integer id);
}
