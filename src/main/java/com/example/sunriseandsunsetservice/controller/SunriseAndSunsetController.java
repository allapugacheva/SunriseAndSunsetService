package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.ResponseDTO;
import com.example.sunriseandsunsetservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class SunriseAndSunsetController {

    private final SunriseAndSunsetService service;

    @PostMapping("/sunriseandsunset")
    public ResponseDTO findSunriseAndSunsetTime(@RequestParam("lat") Double lat,
                                                @RequestParam("lng") Double lng,
                                                @RequestParam("date") LocalDate date) {

        return service.findSunriseAndSunsetTime(lat, lng, date);
    }

    @GetMapping("/sunriseandsunset")
    public List<ResponseDTO> readAllSunrisesAnsSunsets() {
        return service.readAllSunrisesAnsSunsets();
    }

    @PutMapping("/sunriseandsunset")
    public ResponseDTO updateSunriseAndSunset(@RequestParam("locationId") int locationId,
                                              @RequestParam("dateId") int dateId,
                                              @RequestParam("lat") Double lat,
                                              @RequestParam("lng") Double lng,
                                              @RequestParam("date") LocalDate date) {
        return service.updateSunriseAndSunset(locationId, dateId, lat, lng, date);
    }

    @DeleteMapping("/sunriseandsunset")
    public ResponseDTO deleteSunriseAndSunsetTime(@RequestParam("locationId") int locationId,
                                                  @RequestParam("dateId") int dateId) {
        return service.deleteSunriseAndSunsetTime(locationId, dateId);
    }
}
