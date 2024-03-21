package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.TimezoneDTO;
import com.example.sunriseandsunsetservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suntime")
public class TimezoneController {

    private final TimezoneService timezoneService;

    @PostMapping("/timezone")
    public TimezoneDTO createTimezone(@RequestParam("timezone") String timezone) {
        return timezoneService.createTimezone(timezone);
    }

    @GetMapping("/timezone")
    public List<TimezoneDTO> readAllTimezones() {
        return timezoneService.readAllTimezones();
    }

    @PutMapping("/timezone")
    public TimezoneDTO updateTimezone(@RequestParam("id") Integer id,
                                      @RequestParam("timezone") String timezone) {
        return timezoneService.updateTimezone(id, timezone);
    }

    @DeleteMapping("/timezone")
    public TimezoneDTO deleteTimezone(@RequestParam("id") Integer id) {
        return timezoneService.deleteTimezone(id);
    }
}