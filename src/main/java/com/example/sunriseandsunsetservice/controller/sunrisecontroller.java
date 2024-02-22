package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.model.sunrisedata;
import com.example.sunriseandsunsetservice.service.sunriseservice;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/suntime")
@AllArgsConstructor
public class sunrisecontroller {

    private final sunriseservice service;

    @GetMapping
    public List<sunrisedata> findAllData() {
        return service.findAllData();
    }

    @GetMapping("/{lat}/{lng}/{date}")
    public sunrisedata findTime(@PathVariable Double lat, @PathVariable Double lng, @PathVariable LocalDate date) {
        return service.findTime(lat, lng, date);
    }
}
