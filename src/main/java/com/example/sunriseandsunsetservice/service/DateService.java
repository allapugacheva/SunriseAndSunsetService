package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.dto.DateDTO;
import com.example.sunriseandsunsetservice.model.DateModel;
import java.time.LocalDate;
import java.util.List;

public interface DateService{

    DateDTO createDate(LocalDate date);
    List<DateDTO> readAllDates();
    DateDTO updateDate(int id, LocalDate date);
    DateDTO deleteDate(int id);
}
