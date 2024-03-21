package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.TimezoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimezoneRepository extends JpaRepository<TimezoneModel, Integer> {

    TimezoneModel findByTimezone(String s);

    TimezoneModel findById(int id);
}
