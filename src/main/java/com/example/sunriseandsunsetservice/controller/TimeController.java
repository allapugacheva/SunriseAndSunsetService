package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.TimeDto;
import com.example.sunriseandsunsetservice.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalTime;
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
 * Controller for time.
 */
@Tag(name = "Time CRUD")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimeController {

  private final TimeService timeService;

  @Operation(summary = "Create time")
  @PostMapping("/time")
  public ResponseEntity<TimeDto> createTime(@RequestParam LocalTime sunriseTime,
                                            @RequestParam LocalTime sunsetTime) {
    return ResponseEntity.ok(timeService.createTime(sunriseTime, sunsetTime));
  }

  @Operation(summary = "Show all times")
  @GetMapping("/time")
  public ResponseEntity<List<TimeDto>> readAllTimes() {

    return ResponseEntity.ok(timeService.readAllTimes());
  }

  @Operation(summary = "Get time by id")
  @GetMapping("/time/one")
  public ResponseEntity<TimeDto> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(timeService.getById(id));
  }

  @Operation(summary = "Update time")
  @PutMapping("/time")
  public ResponseEntity<TimeDto> updateTime(@RequestParam Integer id,
                                            @RequestParam LocalTime sunriseTime,
                                            @RequestParam LocalTime sunsetTime) {
    return ResponseEntity.ok(timeService.updateTime(id, sunriseTime, sunsetTime));
  }

  @Operation(summary = "Delete time")
  @DeleteMapping("/time")
  public ResponseEntity<TimeDto> deleteTime(@RequestParam Integer id) {

    return ResponseEntity.ok(timeService.deleteTime(id));
  }
}