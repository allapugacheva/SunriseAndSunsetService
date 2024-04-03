package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.DateDto;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import com.example.sunriseandsunsetservice.service.DateService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Implementation of date service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class DateServiceImpl implements DateService {

  private final DateRepository dateRepository;
  private final CommonService commonService;

  private final InMemoryCache cache;

  private static final String DATE_KEY = "Date";
  private static final String DATE_INFO = "Date with id ";

  @Override
  @Transactional
  public DateDto createDate(LocalDate newDate) {

    Date date;
    if ((date = dateRepository.findBySunDate(newDate)) == null) {
      date = dateRepository.save(new Date(newDate));
    }

    cache.put(DATE_KEY + date.getId().toString(), date);

    return new DateDto(date.getSunDate());
  }

  @Override
  public List<DateDto> readAllDates() {

    return dateRepository.findAllDates();
  }

  @Override
  public DateDto getById(Integer id) {

    Date tempDate = (Date) cache.get(DATE_KEY + id.toString());

    if (tempDate == null) {
      tempDate = dateRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(DATE_INFO + id + " not found."));
      cache.put(DATE_KEY + id, tempDate);
    }

    return new DateDto(tempDate.getSunDate());
  }

  @Override
  @Transactional
  public DateDto updateDate(Integer id, LocalDate date) {

    commonService.updateDate(id, date);

    return new DateDto(date);
  }

  @Override
  @SneakyThrows
  @Transactional
  public DateDto deleteDate(Integer id) {

    Date date = dateRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException(DATE_INFO + id + " not found."));

    if (date.getTimes().isEmpty() && date.getLocations().isEmpty()) {
      dateRepository.delete(date);
      cache.remove(DATE_KEY + id);

    } else {
      throw new BadRequestException(DATE_INFO + id + " has connections.");
    }

    return new DateDto(date.getSunDate());
  }
}