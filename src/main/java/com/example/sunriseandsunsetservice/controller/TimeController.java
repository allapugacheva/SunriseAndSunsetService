package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.TimeDTO;
import com.example.sunriseandsunsetservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimeController {

    private final TimeService timeService;

    @PostMapping("/time")
    public TimeDTO createTime(@RequestParam("sunriseTime") LocalTime sunriseTime,
                              @RequestParam("sunsetTime") LocalTime sunsetTime) {
        return timeService.createTime(sunriseTime, sunsetTime);
    }

    @GetMapping("/time")
    public List<TimeDTO> readAllTimes() {
        return timeService.readAllTimes();
    }

    @GetMapping("/time/one")
    public TimeDTO getById(@RequestParam("id") Integer id) { return timeService.getById(id); }

    @PutMapping("/time")
    public TimeDTO updateTime(@RequestParam("id") Integer id,
                              @RequestParam("sunriseTime") LocalTime sunriseTime,
                              @RequestParam("sunsetTime") LocalTime sunsetTime) {
        return timeService.updateTime(id, sunriseTime, sunsetTime);
    }

    @DeleteMapping("/time")
    public TimeDTO deleteTime(@RequestParam("id") Integer id) {
        return timeService.deleteTime(id);
    }
}