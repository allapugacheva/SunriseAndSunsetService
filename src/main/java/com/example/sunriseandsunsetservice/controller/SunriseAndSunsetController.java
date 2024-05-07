package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.SunriseAndSunsetRequest;
import com.example.sunriseandsunsetservice.dto.response.DaytimeResponse;
import com.example.sunriseandsunsetservice.dto.response.SunriseAndSunsetResponse;
import com.example.sunriseandsunsetservice.service.SunriseAndSunsetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for sunrise and sunset.
 */
@Tag(name = "Sunrise and sunset CRUD")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class SunriseAndSunsetController {

  private final SunriseAndSunsetService service;

  @Operation(summary = "Find sunrise and sunset")
  @PostMapping("/sunriseandsunset")
  public ResponseEntity<SunriseAndSunsetResponse> findSunriseAndSunsetTime(
          @RequestParam Double lat,
          @RequestParam Double lng,
          @RequestParam LocalDate date) {
    return ResponseEntity.ok(service.findSunriseAndSunsetTime(lat, lng, date));
  }

  @Operation(summary = "Find many sunrises and sunsets")
  @PostMapping("/sunrisesandsunsets")
  public ResponseEntity<List<SunriseAndSunsetResponse>> findManySunriseAndSunsetTimes(
          @RequestBody List<SunriseAndSunsetRequest> data) {

    return ResponseEntity.ok(service.findManySunriseAndSunsetTimes(data));
  }

  @Operation(summary = "Show all sunrises and sunsets")
  @GetMapping("/sunriseandsunset")
  public ResponseEntity<List<SunriseAndSunsetResponse>> readAllSunrisesAnsSunsets() {

    return ResponseEntity.ok(service.readAllSunrisesAnsSunsets());
  }

  @Operation(summary = "Get sunrise and sunset by id")
  @GetMapping("/sunriseandsunset/one")
  public ResponseEntity<SunriseAndSunsetResponse> getById(@RequestParam Integer locationId,
                                                          @RequestParam Integer dateId) {
    return ResponseEntity.ok(service.getById(locationId, dateId));
  }

  @Operation(summary = "Update sunrise and sunset")
  @PutMapping("/sunriseandsunset")
  public ResponseEntity<SunriseAndSunsetResponse> updateSunriseAndSunset(
          @RequestParam Integer locationId,
          @RequestParam Integer dateId,
          @RequestParam Double lat,
          @RequestParam Double lng,
          @RequestParam LocalDate date) {
    return ResponseEntity.ok(service.updateSunriseAndSunset(locationId, dateId, lat, lng, date));
  }

  @Operation(summary = "Delete sunrise and sunset")
  @DeleteMapping("/sunriseandsunset")
  public ResponseEntity<SunriseAndSunsetResponse> deleteSunriseAndSunsetTime(
          @RequestParam Integer locationId,
          @RequestParam Integer dateId) {
    return ResponseEntity.ok(service.deleteSunriseAndSunsetTime(locationId, dateId));
  }

  @Operation(summary = "Find daytime length")
  @GetMapping("/sunriseandsunset/daytime")
  public ResponseEntity<DaytimeResponse> findDaytimeLength(@RequestParam Integer dateId,
                                                           @RequestParam Integer locationId) {
    return ResponseEntity.ok(service.findDaytimeLength(dateId, locationId));
  }
}
