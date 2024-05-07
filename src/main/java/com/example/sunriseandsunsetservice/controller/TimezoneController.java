package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.TimezoneRequest;
import com.example.sunriseandsunsetservice.dto.response.TimezoneResponse;
import com.example.sunriseandsunsetservice.service.TimezoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for timezone.
 */
@Tag(name = "Timezone CRUD")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimezoneController {

  private final TimezoneService timezoneService;

  @Operation(summary = "Create timezone")
  @PostMapping("/timezone")
  public ResponseEntity<TimezoneResponse> createTimezone(@RequestParam String timezone) {

    return ResponseEntity.ok(timezoneService.createTimezone(timezone));
  }

  @Operation(summary = "Create many timezones")
  @PostMapping("/timezones")
  public ResponseEntity<List<TimezoneResponse>> createManyTimezones(@RequestBody
                                                       List<TimezoneRequest> timezones) {

    return ResponseEntity.ok(timezoneService.createManyTimezones(timezones));
  }

  @Operation(summary = "Show all timezones")
  @GetMapping("/timezone")
  public ResponseEntity<List<TimezoneResponse>> readAllTimezones() {

    return ResponseEntity.ok(timezoneService.readAllTimezones());
  }

  @Operation(summary = "Get timezone by id")
  @GetMapping("/timezone/one")
  public ResponseEntity<TimezoneResponse> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(timezoneService.getById(id));
  }

  @Operation(summary = "Update timezone")
  @PutMapping("/timezone")
  public ResponseEntity<TimezoneResponse> updateTimezone(@RequestParam Integer id,
                                                         @RequestParam String timezone) {
    return ResponseEntity.ok(timezoneService.updateTimezone(id, timezone));
  }

  @Operation(summary = "Delete timezone")
  @DeleteMapping("/timezone")
  public ResponseEntity<TimezoneResponse> deleteTimezone(@RequestParam Integer id) {

    return ResponseEntity.ok(timezoneService.deleteTimezone(id));
  }
}