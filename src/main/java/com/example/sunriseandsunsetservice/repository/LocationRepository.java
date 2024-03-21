package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationModel, Integer> {

    LocationModel findByLatitudeAndLongitude(Double lat, Double lng);
}
