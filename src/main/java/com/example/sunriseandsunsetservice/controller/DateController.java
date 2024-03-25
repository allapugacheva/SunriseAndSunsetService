package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.DateDto;
import com.example.sunriseandsunsetservice.service.DateService;
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
 * Controller for date.
 */
@Tag(name = "Date CRUD")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class DateController {

  private final DateService dateService;

  @Operation(summary = "Create date")
  @PostMapping("/date")
  public ResponseEntity<DateDto> createDate(@RequestParam LocalDate date) {

    return ResponseEntity.ok(dateService.createDate(date));
  }

  @Operation(summary = "Show all dates")
  @GetMapping("/date")
  public ResponseEntity<List<DateDto>> readAllDates() {

    return ResponseEntity.ok(dateService.readAllDates());
  }

  @Operation(summary = "Find date by id")
  @GetMapping("/date/one")
  public ResponseEntity<DateDto> getById(@RequestParam Integer id) {

    return ResponseEntity.ok(dateService.getById(id));
  }

  @Operation(summary = "Update date")
  @PutMapping("/date")
  public ResponseEntity<DateDto> updateDate(@RequestParam Integer id,
                                            @RequestParam("date") LocalDate date) {
    return ResponseEntity.ok(dateService.updateDate(id, date));
  }

  @Operation(summary = "Delete date")
  @DeleteMapping("/date")
  public ResponseEntity<DateDto> deleteDate(@RequestParam Integer id) {

    return ResponseEntity.ok(dateService.deleteDate(id));
  }
}