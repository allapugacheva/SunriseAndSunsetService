package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.SunriseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SunriseRepository extends JpaRepository<SunriseData, Long> {

}
