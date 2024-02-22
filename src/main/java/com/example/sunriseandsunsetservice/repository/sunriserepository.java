package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.sunrisedata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface sunriserepository extends JpaRepository<sunrisedata, Long> {

}
