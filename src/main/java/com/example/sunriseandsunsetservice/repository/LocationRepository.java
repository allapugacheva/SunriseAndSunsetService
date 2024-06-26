package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import com.example.sunriseandsunsetservice.model.Location;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for location.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

  Location findByLatitudeAndLongitude(Double lat, Double lng);

  @Query(value = "SELECT EXTRACT(EPOCH FROM (t.sunset_time - t.sunrise_time)) FROM date AS d"
          + " JOIN location_date_mapping AS ldm ON d.id = ldm.date_id"
          + " JOIN location l ON ldm.location_id = l.id"
          + " JOIN date_time_mapping AS dtm ON d.id = dtm.date_id"
          + " JOIN location_time_mapping AS ltm ON l.id = ltm.location_id"
          + " JOIN sunrise_and_sunset_time AS t ON dtm.sunrise_and_sunset_time_id = t.id"
          + " AND ltm.sunrise_and_sunset_time_id = t.id"
          + " WHERE d.id = :date_id AND l.id = :location_id", nativeQuery = true)
  Long findDaytimeLength(@Param("date_id") Integer dateId,
                         @Param("location_id") Integer locationId);

  @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto.response"
          + ".LocationResponse(l.id, l.sunLocation, l.latitude, l.longitude) FROM Location AS l")
  List<LocationResponse> findAllLocations();

  @Query(value = "SELECT l.id, d.id, l.sun_location,"
          + " l.latitude, l.longitude, d.sun_date, t.sunrise_time, t.sunset_time FROM date AS d"
          + " JOIN location_date_mapping AS ldm ON d.id = ldm.date_id"
          + " JOIN location l ON ldm.location_id = l.id"
          + " JOIN date_time_mapping AS dtm ON d.id = dtm.date_id"
          + " JOIN location_time_mapping AS ltm ON l.id = ltm.location_id"
          + " JOIN sunrise_and_sunset_time AS t ON dtm.sunrise_and_sunset_time_id = t.id"
          + " AND ltm.sunrise_and_sunset_time_id = t.id", nativeQuery = true)
  List<Object[]> findAllData();
}