package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.TimezoneDto;
import com.example.sunriseandsunsetservice.service.TimezoneService;
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
 * Controller for timezone.
 */
@Tag(name = "Timezone CRUD")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimezoneController {

  private final TimezoneService timezoneService;

  @Operation(summary = "Create timezone")
  @PostMapping("/timezone")
  public ResponseEntity<TimezoneDto> createTimezone(@RequestParam String timezone) {

    return ResponseEntity.ok(timezoneService.createTimezone(timezone));
  }

  @Operation(summary = "Show all timezones")
  @GetMapping("/timezone")
  public ResponseEntity<List<TimezoneDto>> readAllTimezones() {

    return ResponseEntity.ok(timezoneService.readAllTimezones());
  }

  @Operation(summary = "Get timezone by id")
  @GetMapping("/timezone/one")
  public ResponseEntity<TimezoneDto> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(timezoneService.getById(id));
  }

  @Operation(summary = "Update timezone")
  @PutMapping("/timezone")
  public ResponseEntity<TimezoneDto> updateTimezone(@RequestParam Integer id,
                                                    @RequestParam String timezone) {
    return ResponseEntity.ok(timezoneService.updateTimezone(id, timezone));
  }

  @Operation(summary = "Delete timezone")
  @DeleteMapping("/timezone")
  public ResponseEntity<TimezoneDto> deleteTimezone(@RequestParam Integer id) {

    return ResponseEntity.ok(timezoneService.deleteTimezone(id));
  }
}