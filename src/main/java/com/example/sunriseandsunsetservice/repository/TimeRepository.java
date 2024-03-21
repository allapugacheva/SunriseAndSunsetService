package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.TimeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;

@Repository
public interface TimeRepository extends JpaRepository<TimeModel, Integer> {

    TimeModel findBySunriseTimeAndSunsetTime(LocalTime sunrise, LocalTime sunset);
}
