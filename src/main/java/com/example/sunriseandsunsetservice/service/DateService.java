package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.DateDTO;
import java.time.LocalDate;
import java.util.List;

public interface DateService{

    DateDTO createDate(LocalDate date);
    List<DateDTO> readAllDates();
    DateDTO getById(Integer id);
    DateDTO updateDate(Integer id, LocalDate date);
    DateDTO deleteDate(Integer id);
}
