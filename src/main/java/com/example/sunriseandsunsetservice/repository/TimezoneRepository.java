package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.TimezoneDTO;
import com.example.sunriseandsunsetservice.model.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Integer> {

    Timezone findByTimezone(String s);

    @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto.TimezoneDTO(tz.timezone) " +
            "FROM Timezone AS tz")
    List<TimezoneDTO> findAllTimezones();
}
