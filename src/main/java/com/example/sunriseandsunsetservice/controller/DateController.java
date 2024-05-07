package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.DateRequest;
import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import com.example.sunriseandsunsetservice.service.DateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for date.
 */
@Tag(name = "Date CRUD")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class DateController {

  private final DateService dateService;

  @Operation(summary = "Create date")
  @PostMapping("/date")
  public ResponseEntity<DateResponse> createDate(@RequestParam LocalDate date) {

    return ResponseEntity.ok(dateService.createDate(date));
  }

  @Operation(summary = "Create many dates")
  @PostMapping("/dates")
  public ResponseEntity<List<DateResponse>> createManyDates(@RequestBody List<DateRequest> dates) {

    return ResponseEntity.ok(dateService.createManyDates(dates));
  }

  @Operation(summary = "Show all dates")
  @GetMapping("/date")
  public ResponseEntity<List<DateResponse>> readAllDates() {

    return ResponseEntity.ok(dateService.readAllDates());
  }

  @Operation(summary = "Find date by id")
  @GetMapping("/date/one")
  public ResponseEntity<DateResponse> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(dateService.getById(id));
  }

  @Operation(summary = "Update date")
  @PutMapping("/date")
  public ResponseEntity<DateResponse> updateDate(@RequestParam Integer id,
                                                 @RequestParam("date") LocalDate date) {
    return ResponseEntity.ok(dateService.updateDate(id, date));
  }

  @Operation(summary = "Delete date")
  @DeleteMapping("/date")
  public ResponseEntity<DateResponse> deleteDate(@RequestParam Integer id) {

    return ResponseEntity.ok(dateService.deleteDate(id));
  }
}