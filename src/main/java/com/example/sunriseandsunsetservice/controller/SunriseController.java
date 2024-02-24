package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.model.SunriseData;
import com.example.sunriseandsunsetservice.service.SunriseService;
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
    public SunriseData findTime(@PathVariable Double lat, @PathVariable Double lng, @PathVariable LocalDate date) {
        return service.findTime(lat, lng, date);
    }

    @DeleteMapping("/{id}")
    public void deleteData(@PathVariable Long id){
        service.deleteData(id);
    }
}
