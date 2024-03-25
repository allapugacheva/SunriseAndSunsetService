package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.DateDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.service.DateService;
import com.example.sunriseandsunsetservice.service.CommonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DateServiceImpl implements DateService {

    private final DateRepository dateRepository;
    private final CommonService commonService;

    private final InMemoryCache cache;

    private static final String DATE_KEY = "Date";

    @Override
    @Transactional
    public DateDTO createDate(LocalDate newDate) {

        Date date;
        if ((date = dateRepository.findBySunDate(newDate)) == null)
            date = dateRepository.save(new Date(newDate));

        cache.put(DATE_KEY + date.getId().toString(), date);

        return new DateDTO(date.getSunDate());
    }

    @Override
    public List<DateDTO> readAllDates() {
        return dateRepository.findAllDates();
    }

    @Override
    public DateDTO getById(Integer id) {

        Date tempDate = (Date)cache.get(DATE_KEY + id.toString());

        if(tempDate == null) {
            tempDate = dateRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Date not found."));
            cache.put(DATE_KEY + id, tempDate);
        }

        return new DateDTO(tempDate.getSunDate());
    }

    @Override
    @Transactional
    public DateDTO updateDate(Integer id, LocalDate date) {

        commonService.updateDate(id, date);

        return new DateDTO(date);
    }

    @Override
    @Transactional
    public DateDTO deleteDate(Integer id) {

        Date date = dateRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (date.getTimes().isEmpty() && date.getLocations().isEmpty()) {
            dateRepository.delete(date);
            cache.remove(DATE_KEY + id);

        } else throw new MyRuntimeException("Date has connections.");

        return new DateDTO(date.getSunDate());
    }
}
