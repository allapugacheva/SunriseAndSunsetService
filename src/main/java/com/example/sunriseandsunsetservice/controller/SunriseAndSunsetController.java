package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.DaytimeDto;
import com.example.sunriseandsunsetservice.dto.ResponseDto;
import com.example.sunriseandsunsetservice.service.SunriseAndSunsetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
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
 * Controller for sunrise and sunset.
 */
@Tag(name = "Sunrise and sunset CRUD")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class SunriseAndSunsetController {

  private final SunriseAndSunsetService service;

  @Operation(summary = "Find sunrise and sunset")
  @PostMapping("/sunriseandsunset")
  public ResponseEntity<ResponseDto> findSunriseAndSunsetTime(@RequestParam Double lat,
                                                              @RequestParam Double lng,
                                                              @RequestParam LocalDate date) {
    return ResponseEntity.ok(service.findSunriseAndSunsetTime(lat, lng, date));
  }

  @Operation(summary = "Show all sunrises and sunsets")
  @GetMapping("/sunriseandsunset")
  public ResponseEntity<List<ResponseDto>> readAllSunrisesAnsSunsets() {

    return ResponseEntity.ok(service.readAllSunrisesAnsSunsets());
  }

  @Operation(summary = "Get sunrise and sunset by id")
  @GetMapping("/sunriseandsunset/one")
  public ResponseEntity<ResponseDto> getById(@RequestParam Integer locationId,
                                             @RequestParam Integer dateId) {
    return ResponseEntity.ok(service.getById(locationId, dateId));
  }

  @Operation(summary = "Update sunrise and sunset")
  @PutMapping("/sunriseandsunset")
  public ResponseEntity<ResponseDto> updateSunriseAndSunset(@RequestParam Integer locationId,
                                                            @RequestParam Integer dateId,
                                                            @RequestParam Double lat,
                                                            @RequestParam Double lng,
                                                            @RequestParam LocalDate date) {
    return ResponseEntity.ok(service.updateSunriseAndSunset(locationId, dateId, lat, lng, date));
  }

  @Operation(summary = "Delete sunrise and sunset")
  @DeleteMapping("/sunriseandsunset")
  public ResponseEntity<ResponseDto> deleteSunriseAndSunsetTime(@RequestParam Integer locationId,
                                                                @RequestParam Integer dateId) {
    return ResponseEntity.ok(service.deleteSunriseAndSunsetTime(locationId, dateId));
  }

  @Operation(summary = "Find daytime length")
  @GetMapping("/sunriseandsunset/daytime")
  public ResponseEntity<DaytimeDto> findDaytimeLength(@RequestParam Integer dateId,
                                                      @RequestParam Integer locationId) {
    return ResponseEntity.ok(service.findDaytimeLength(dateId, locationId));
  }
}
