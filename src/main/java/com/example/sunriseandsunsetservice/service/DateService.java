package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.DateDto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for date service.
 */
public interface DateService {

  DateDto createDate(LocalDate date);

  List<DateDto> readAllDates();

  DateDto getById(Integer id);

  DateDto updateDate(Integer id, LocalDate date);

  DateDto deleteDate(Integer id);
}
