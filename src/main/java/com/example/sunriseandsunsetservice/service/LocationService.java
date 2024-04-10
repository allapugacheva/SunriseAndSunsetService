package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.request.LocationRequest;
import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import java.util.List;

/**
 * Interface for location service.
 */
public interface LocationService {

  LocationResponse createLocation(Double lat, Double lng);

  List<LocationResponse> createManyLocations(List<LocationRequest> locations);

  List<LocationResponse> readAllLocations();

  LocationResponse getById(Integer id);

  LocationResponse updateLocation(Integer id, Double lat, Double lng);

  LocationResponse deleteLocation(Integer id);
}
