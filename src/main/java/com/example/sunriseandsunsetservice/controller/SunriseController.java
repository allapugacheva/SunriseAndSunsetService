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

    @GetMapping("/show")
    public List<SunriseData> findAllData() {
        return service.findAllData();
    }

    @GetMapping("/find")
    public SunriseData findTime(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng, @RequestParam("date") LocalDate date) {
        return service.findTime(lat, lng, date);
    }

    @DeleteMapping("/delete")
    public void deleteData(@RequestParam("id") Long id){
        service.deleteData(id);
    }
}
