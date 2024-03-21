package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.DateDTO;
import com.example.sunriseandsunsetservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class DateController {

    private final DateService dateService;

    @PostMapping("/date")
    public DateDTO createDate(@RequestParam("date") LocalDate date) {
        return dateService.createDate(date);
    }

    @GetMapping("/date")
    public List<DateDTO> readAllDates() {
        return dateService.readAllDates();
    }

    @PutMapping("/date")
    public DateDTO updateDate(@RequestParam("id") Integer id,
                              @RequestParam("date") LocalDate date) {
        return dateService.updateDate(id, date);
    }

    @DeleteMapping("/date")
    public DateDTO deleteDate(@RequestParam("id") Integer id) {
        return dateService.deleteDate(id);
    }

}