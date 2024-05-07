package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import com.example.sunriseandsunsetservice.model.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for date.
 */
@Repository
public interface DateRepository extends JpaRepository<Date, Integer> {

  Date findBySunDate(LocalDate date);

  @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto.response"
         + ".DateResponse(d.id, d.sunDate) FROM Date AS d")
  List<DateResponse> findAllDates();
}
