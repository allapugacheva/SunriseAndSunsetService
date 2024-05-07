package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.LocationRequest;
import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import com.example.sunriseandsunsetservice.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for location.
 */
@Tag(name = "Location CRUD")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class LocationController {

  private final LocationService locationService;

  @Operation(summary = "Create location")
  @PostMapping("/location")
  public ResponseEntity<LocationResponse> createLocation(@RequestParam Double lat,
                                                         @RequestParam Double lng) {
    return ResponseEntity.ok(locationService.createLocation(lat, lng));
  }

  @Operation(summary = "Create many locations")
  @PostMapping("/locations")
  public ResponseEntity<List<LocationResponse>> createManyLocations(@RequestBody
                                                             List<LocationRequest> locations) {

    return ResponseEntity.ok(locationService.createManyLocations(locations));
  }

  @Operation(summary = "Show all locations")
  @GetMapping("/location")
  public ResponseEntity<List<LocationResponse>> readAllLocations() {

    return ResponseEntity.ok(locationService.readAllLocations());
  }

  @Operation(summary = "Get location by id")
  @GetMapping("/location/one")
  public ResponseEntity<LocationResponse> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(locationService.getById(id));
  }

  @Operation(summary = "Update location")
  @PutMapping("/location")
  public ResponseEntity<LocationResponse> updateLocation(@RequestParam Integer id,
                                                         @RequestParam Double lat,
                                                         @RequestParam Double lng) {
    return ResponseEntity.ok(locationService.updateLocation(id, lat, lng));
  }

  @Operation(summary = "Delete location")
  @DeleteMapping("/location")
  public ResponseEntity<LocationResponse> deleteLocation(@RequestParam Integer id) {

    return ResponseEntity.ok(locationService.deleteLocation(id));
  }
}