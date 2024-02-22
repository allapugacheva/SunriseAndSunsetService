package com.example.SunriseAndSunsetService.repository;

import com.example.SunriseAndSunsetService.model.SunriseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SunriseRepository extends JpaRepository<SunriseData, Long> {

}
