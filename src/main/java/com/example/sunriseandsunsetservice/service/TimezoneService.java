package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.request.TimezoneRequest;
import com.example.sunriseandsunsetservice.dto.response.TimezoneResponse;
import java.util.List;

/**
 * Interface for timezone service.
 */
public interface TimezoneService {

  TimezoneResponse createTimezone(String timezone);

  List<TimezoneResponse> createManyTimezones(List<TimezoneRequest> timezones);

  List<TimezoneResponse> readAllTimezones();

  TimezoneResponse getById(Integer id);

  TimezoneResponse updateTimezone(Integer id, String timezone);

  TimezoneResponse deleteTimezone(Integer id);
}
