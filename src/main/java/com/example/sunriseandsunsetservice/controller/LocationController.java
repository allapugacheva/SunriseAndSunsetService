package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.LocationDTO;
import com.example.sunriseandsunsetservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/location")
    public LocationDTO createLocation(@RequestParam("lat") Double lat,
                                      @RequestParam("lng") Double lng) {
        return locationService.createLocation(lat, lng);
    }

    @GetMapping("/location")
    public List<LocationDTO> readAllLocations() {
        return locationService.readAllLocations();
    }

    @PutMapping("/location")
    public LocationDTO updateLocation(@RequestParam("id") Integer id,
                                      @RequestParam("lat") Double lat,
                                      @RequestParam("lng") Double lng) {
        return locationService.updateLocation(id, lat, lng);
    }

    @DeleteMapping("/location")
    public LocationDTO deleteLocation(@RequestParam("id") Integer id) {
        return locationService.deleteLocation(id);
    }

}