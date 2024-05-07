package com.example.sunriseandsunsetservice.repository;

import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import com.example.sunriseandsunsetservice.model.Time;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for time.
 */
@Repository
public interface TimeRepository extends JpaRepository<Time, Integer> {

  Time findBySunriseTimeAndSunsetTime(LocalTime sunrise, LocalTime sunset);

  @Query(value = "SELECT new com.example.sunriseandsunsetservice.dto.response"
         + ".TimeResponse(t.id, t.sunriseTime, t.sunsetTime) FROM Time AS t")
  List<TimeResponse> findAllTimes();

  @Query(value = "SELECT t FROM Time t"
         + " JOIN t.dates AS d"
         + " JOIN t.locations AS l"
         + " WHERE d.id = :date_id AND l.id = :location_id")
  Time findCommonTime(@Param("date_id") Integer dateId,
                      @Param("location_id") Integer locationId);
}
