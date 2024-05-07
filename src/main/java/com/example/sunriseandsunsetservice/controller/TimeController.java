package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.TimeRequest;
import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import com.example.sunriseandsunsetservice.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for time.
 */
@Tag(name = "Time CRUD")
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimeController {

  private final TimeService timeService;

  @Operation(summary = "Create time")
  @PostMapping("/time")
  public ResponseEntity<TimeResponse> createTime(@RequestParam LocalTime sunriseTime,
                                                 @RequestParam LocalTime sunsetTime) {
    return ResponseEntity.ok(timeService.createTime(sunriseTime, sunsetTime));
  }

  @Operation(summary = "Create many times")
  @PostMapping("/times")
  public ResponseEntity<List<TimeResponse>> createManyTimes(@RequestBody List<TimeRequest> times) {

    return ResponseEntity.ok(timeService.createManyTimes(times));
  }

  @Operation(summary = "Show all times")
  @GetMapping("/time")
  public ResponseEntity<List<TimeResponse>> readAllTimes() {

    return ResponseEntity.ok(timeService.readAllTimes());
  }

  @Operation(summary = "Get time by id")
  @GetMapping("/time/one")
  public ResponseEntity<TimeResponse> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(timeService.getById(id));
  }

  @Operation(summary = "Update time")
  @PutMapping("/time")
  public ResponseEntity<TimeResponse> updateTime(@RequestParam Integer id,
                                                 @RequestParam LocalTime sunriseTime,
                                                 @RequestParam LocalTime sunsetTime) {
    return ResponseEntity.ok(timeService.updateTime(id, sunriseTime, sunsetTime));
  }

  @Operation(summary = "Delete time")
  @DeleteMapping("/time")
  public ResponseEntity<TimeResponse> deleteTime(@RequestParam Integer id) {

    return ResponseEntity.ok(timeService.deleteTime(id));
  }
}