package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.LocationDTO;
import java.util.List;

public interface LocationService {

    LocationDTO createLocation(Double lat, Double lng);
    List<LocationDTO> readAllLocations();
    LocationDTO updateLocation(int id, Double lat, Double lng);
    LocationDTO deleteLocation(int id);
}