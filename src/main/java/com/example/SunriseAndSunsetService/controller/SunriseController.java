package com.example.SunriseAndSunsetService.controller;

import com.example.SunriseAndSunsetService.model.SunriseData;
import com.example.SunriseAndSunsetService.service.SunriseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/suntime")
@AllArgsConstructor
public class SunriseController {

    private final SunriseService service;

    @GetMapping
    public List<SunriseData> findAllData() {
        return service.findAllData();
    }

    @GetMapping("/{lat}/{lng}/{date}")
    public SunriseData findTime(@PathVariable Double lat,@PathVariable Double lng, @PathVariable LocalDate date) {
        return service.findTime(lat, lng, date);
    }
}
