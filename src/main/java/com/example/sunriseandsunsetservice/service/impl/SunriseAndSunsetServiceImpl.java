package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.DaytimeDTO;
import com.example.sunriseandsunsetservice.dto.ResponseDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.*;
import com.example.sunriseandsunsetservice.repository.*;
import com.example.sunriseandsunsetservice.service.CommonService;
import com.example.sunriseandsunsetservice.service.SunriseAndSunsetService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SunriseAndSunsetServiceImpl implements SunriseAndSunsetService {
    private final DateRepository dateRepository;
    private final LocationRepository locationRepository;
    private final TimeRepository timeRepository;
    private final TimezoneRepository timezoneRepository;

    private final CommonService commonService;
    private final InMemoryCache cache;

    @Override
    @SneakyThrows
    @Transactional
    public ResponseDTO findSunriseAndSunsetTime(Double lat, Double lng, LocalDate newDate) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new MyRuntimeException("Not valid latitude or longitude.");

        Timezone timezone;
        Location location = locationRepository.findByLatitudeAndLongitude(lat, lng);
        if(location == null) {

            String[] timezoneAndPlace = commonService.getTimezoneAndPlace(lat, lng);
            location = locationRepository.save(new Location(timezoneAndPlace[1], lat, lng));

            if((timezone = timezoneRepository.findByTimezone(timezoneAndPlace[0]))==null)
                timezone = timezoneRepository.save(new Timezone(timezoneAndPlace[0]));
        } else
            timezone = location.getTimezone();

        Time time = commonService.getSunriseAndSunsetTime(lat, lng, newDate, timezone.getTimezone());

        Date date = dateRepository.findByDate(newDate);
        if(date == null)
            date = dateRepository.save(new Date(newDate));

        location.addDate(date);
        date.addLocation(location);

        date.addTime(time);
        time.addDate(date);

        timezone.addLocation(location);
        location.setTimezone(timezone);

        location.addTime(time);
        time.addLocation(location);

        locationRepository.save(location);

        cache.put("Date" + date.getId().toString(), date);
        cache.put("Location" + location.getId().toString(), location);

        return new ResponseDTO(location.getLocation(), location.getLatitude(), location.getLongitude(),
                date.getDate(), time.getSunriseTime(), time.getSunsetTime());
    }

    @Override
    public List<ResponseDTO> readAllSunrisesAnsSunsets() {

        return locationRepository.findAllData().stream()
                .map(row -> new ResponseDTO((String)row[0], (Double)row[1], (Double)row[2],
                        ((java.sql.Date)row[3]).toLocalDate(),
                        ((java.sql.Time)row[4]).toLocalTime(),
                        ((java.sql.Time)row[5]).toLocalTime()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDTO getById(Integer locationId, Integer dateId) {

        Location tempLocation = (Location) cache.get("Location" + locationId);
        if(tempLocation == null) {
            tempLocation = locationRepository.findById(locationId).orElseThrow(
                    () -> new MyRuntimeException("Location not found."));
            cache.put("Location" + locationId, tempLocation);
        }

        Date tempDate = (Date) cache.get("Date" + dateId);
        if(tempDate == null) {
            tempDate = dateRepository.findById(dateId).orElseThrow(
                    () -> new MyRuntimeException("Date not found."));
            cache.put("Date" + dateId, tempDate);
        }

        Time tempTime = timeRepository.findCommonTime(dateId, locationId);

        return new ResponseDTO(tempLocation.getLocation(), tempLocation.getLatitude(), tempLocation.getLongitude(),
                tempDate.getDate(), tempTime.getSunriseTime(), tempTime.getSunsetTime());
    }

    @Override
    @Transactional
    public ResponseDTO updateSunriseAndSunset(Integer locationId, Integer dateId, Double lat, Double lng, LocalDate date) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new MyRuntimeException("Not valid latitude or longitude");

        deleteSunriseAndSunsetTime(locationId, dateId);
        return findSunriseAndSunsetTime(lat, lng, date);
    }

    @Override
    @Transactional
    public ResponseDTO deleteSunriseAndSunsetTime(Integer locationId, Integer dateId) {

        Date date = (Date) cache.get("Date" + dateId);
        if(date == null)
            date = dateRepository.findById(dateId).orElseThrow(
                () -> new MyRuntimeException("Wrong date id."));
        
        Location location = (Location) cache.get("Location" + locationId);
        if(location == null)
            location = locationRepository.findById(locationId).orElseThrow(
                () -> new MyRuntimeException("Wrong location id."));

        Time time = timeRepository.findCommonTime(dateId, locationId);

        if ((location.getDates().contains(date) && location.getTimes().contains(time)) &&
                (time.getDates().contains(date) && time.getLocations().contains(location)) &&
                (date.getTimes().contains(time) && date.getLocations().contains(location)))
        {
            date.deleteLocation(location);
            time.deleteLocation(location);

            location.deleteDate(date);
            time.deleteDate(date);

            location.deleteTime(time);
            date.deleteTime(time);

            cache.remove("Date" + dateId);
            cache.remove("Location" + locationId);
            cache.remove("Time" + time.getId());
        }
        else
            throw new MyRuntimeException("Not connected data.");

        if(location.getDates().isEmpty() && location.getTimes().isEmpty())
            locationRepository.delete(location);

        if(date.getTimes().isEmpty() && date.getLocations().isEmpty())
            dateRepository.delete(date);

        if(time.getDates().isEmpty() && time.getLocations().isEmpty())
            timeRepository.delete(time);

        return new ResponseDTO(location.getLocation(), location.getLatitude(),
                location.getLongitude(), date.getDate(), time.getSunriseTime(),
                time.getSunsetTime());
    }

    @Override
    public DaytimeDTO findDaytimeLength(Integer dateId, Integer locationId) {

        DaytimeDTO response = (DaytimeDTO) cache.get("Daytime" + dateId + locationId);

        if(response == null) {
            response = new DaytimeDTO(LocalTime.MIDNIGHT.plusSeconds(locationRepository
                    .findDaytimeLength(dateId, locationId)));

            cache.put("Daytime" + dateId + locationId, response);
        }

        return response;
    }
}
