package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.TimezoneDTO;
import java.util.List;

public interface TimezoneService {

    TimezoneDTO createTimezone(String timezone);
    List<TimezoneDTO> readAllTimezones();
    TimezoneDTO updateTimezone(Integer id, String timezone);
    TimezoneDTO deleteTimezone(Integer id);
}
