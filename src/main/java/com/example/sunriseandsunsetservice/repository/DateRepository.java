package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.DateDTO;
import com.example.sunriseandsunsetservice.model.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<Date, Integer> {

    Date findBySunDate(LocalDate date);

    @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto.DateDTO(d.sunDate) FROM Date AS d")
    List<DateDTO> findAllDates();
}
