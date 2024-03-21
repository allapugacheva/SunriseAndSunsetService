package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.dto.DateDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.DateModel;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.service.DateService;
import com.example.sunriseandsunsetservice.service.CommonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DateServiceImpl implements DateService{

    private final DateRepository dateRepository;
    private final CommonService commonService;

    @Override
    @Transactional
    public DateDTO createDate(LocalDate date) {

        DateModel dateModel;
        if((dateModel = dateRepository.findByDate(date)) == null)
            dateModel = dateRepository.save(new DateModel(date));

        return new DateDTO(dateModel.getDate());
    }

    @Override
    public List<DateDTO> readAllDates() {

        List<DateDTO> dates = new ArrayList<>();

        for(DateModel dm : dateRepository.findAll())
            dates.add(new DateDTO(dm.getDate()));

        return dates;
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

        DateModel dateModel = dateRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (dateModel.getTimes().isEmpty() && dateModel.getLocations().isEmpty())
            dateRepository.delete(dateModel);
        else throw new MyRuntimeException("Date has connections.");

        return new DateDTO(dateModel.getDate());
    }
}
