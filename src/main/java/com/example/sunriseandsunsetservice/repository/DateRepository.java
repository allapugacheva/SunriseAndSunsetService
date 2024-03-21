package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.DateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface DateRepository extends JpaRepository<DateModel, Integer> {

    DateModel findByDate(LocalDate date);

    DateModel findById(int id);
}
