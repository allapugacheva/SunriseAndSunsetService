package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.LocationDto;
import com.example.sunriseandsunsetservice.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for location.
 */
@Tag(name = "Location CRUD")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class LocationController {

  private final LocationService locationService;

  @Operation(summary = "Create location")
  @PostMapping("/location")
  public ResponseEntity<LocationDto> createLocation(@RequestParam Double lat,
                                                    @RequestParam Double lng) {
    return ResponseEntity.ok(locationService.createLocation(lat, lng));
  }

  @Operation(summary = "Show all locations")
  @GetMapping("/location")
  public ResponseEntity<List<LocationDto>> readAllLocations() {

    return ResponseEntity.ok(locationService.readAllLocations());
  }

  @Operation(summary = "Get location by id")
  @GetMapping("/location/one")
  public ResponseEntity<LocationDto> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(locationService.getById(id));
  }

  @Operation(summary = "Update location")
  @PutMapping("/location")
  public ResponseEntity<LocationDto> updateLocation(@RequestParam Integer id,
                                                    @RequestParam Double lat,
                                                    @RequestParam Double lng) {
    return ResponseEntity.ok(locationService.updateLocation(id, lat, lng));
  }

  @Operation(summary = "Delete location")
  @DeleteMapping("/location")
  public ResponseEntity<LocationDto> deleteLocation(@RequestParam Integer id) {

    return ResponseEntity.ok(locationService.deleteLocation(id));
  }
}