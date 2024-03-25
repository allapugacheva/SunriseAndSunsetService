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

    private final static String key = "Date";

    @Override
    @Transactional
    public DateDTO createDate(LocalDate newDate) {

        Date date;
        if ((date = dateRepository.findBySunDate(newDate)) == null)
            date = dateRepository.save(new Date(newDate));

        cache.put(key + date.getId().toString(), date);

        return new DateDTO(date.getSunDate());
    }

    @Override
    public List<DateDTO> readAllDates() {
        return dateRepository.findAllDates();
    }

    @Override
    public DateDTO getById(Integer id) {

        Date tempDate = (Date)cache.get(key + id.toString());

        if(tempDate == null) {
            tempDate = dateRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Date not found."));
            cache.put(key + id, tempDate);
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
            cache.remove(key + id);

        } else throw new MyRuntimeException("Date has connections.");

        return new DateDTO(date.getSunDate());
    }
}
