package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.request.DateRequest;
import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for date service.
 */
public interface DateService {

  DateResponse createDate(LocalDate date);

  List<DateResponse> createManyDates(List<DateRequest> dates);

  List<DateResponse> readAllDates();

  DateResponse getById(Integer id);

  DateResponse updateDate(Integer id, LocalDate date);

  DateResponse deleteDate(Integer id);
}
