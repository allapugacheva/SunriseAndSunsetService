package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.TimezoneDto;
import com.example.sunriseandsunsetservice.model.Timezone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for timezone.
 */
@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Integer> {

  Timezone findBySunTimezone(String s);

  @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto."
         + "TimezoneDto(tz.sunTimezone) FROM Timezone AS tz")
  List<TimezoneDto> findAllTimezones();
}
