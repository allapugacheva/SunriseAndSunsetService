package com.example.sunriseandsunsetservice.service.impl;

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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SunriseAndSunsetServiceImpl implements SunriseAndSunsetService {
    private final DateRepository dateRepository;
    private final LocationRepository locationRepository;
    private final TimeRepository timeRepository;
    private final TimezoneRepository timezoneRepository;

    private final CommonService commonService;

    @Override
    @SneakyThrows
    @Transactional
    public ResponseDTO findSunriseAndSunsetTime(Double lat, Double lng, LocalDate date) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new MyRuntimeException("Not valid latitude or longitude.");

        TimezoneModel timezoneModel;
        LocationModel locationModel = locationRepository.findByLatitudeAndLongitude(lat, lng);
        if(locationModel == null) {

            String[] timezoneAndPlace = commonService.getTimezoneAndPlace(lat, lng);
            locationModel = locationRepository.save(new LocationModel(timezoneAndPlace[1], lat, lng));

            if((timezoneModel = timezoneRepository.findByTimezone(timezoneAndPlace[0]))==null)
                timezoneModel = timezoneRepository.save(new TimezoneModel(timezoneAndPlace[0]));
        } else
            timezoneModel = locationModel.getTimezone();

        TimeModel timeModel = commonService.getSunriseAndSunsetTime(lat, lng, date, timezoneModel.getTimezone());

        DateModel dateModel = dateRepository.findByDate(date);
        if(dateModel == null)
            dateModel = dateRepository.save(new DateModel(date));

        locationModel.addDate(dateModel);
        dateModel.addLocation(locationModel);

        dateModel.addTime(timeModel);
        timeModel.addDate(dateModel);

        timezoneModel.addLocation(locationModel);
        locationModel.setTimezone(timezoneModel);

        locationModel.addTime(timeModel);
        timeModel.addLocation(locationModel);

        locationRepository.save(locationModel);

        return new ResponseDTO(locationModel.getLocation(), locationModel.getLatitude(), locationModel.getLongitude(),
                dateModel.getDate(), timeModel.getSunriseTime(), timeModel.getSunsetTime());
    }

    @Override
    public List<ResponseDTO> readAllSunrisesAnsSunsets() {
        List<ResponseDTO> responses = new ArrayList<>();

        for(LocationModel lm : locationRepository.findAll()) {
            for(DateModel dm : lm.getDates()) {
                for(TimeModel tm : dm.getTimes()) {
                    if(tm.getLocations().contains(lm) && tm.getDates().contains(dm))
                        responses.add(new ResponseDTO(lm.getLocation(), lm.getLatitude(), lm.getLongitude(),
                                dm.getDate(), tm.getSunriseTime(), tm.getSunsetTime()));
                }
            }
        }

        return responses;
    }

    @Override
    @Transactional
    public ResponseDTO updateSunriseAndSunset(Integer locationId, Integer dateId, Double lat, Double lng, LocalDate date) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new MyRuntimeException("Not valid latitude or longitude");

        commonService.updateDate(dateId, date);
        commonService.updateLocation(locationId, lat, lng);

        LocationModel lm = locationRepository.findByLatitudeAndLongitude(lat, lng);
        DateModel dm = dateRepository.findByDate(date);
        TimeModel tm = commonService.getCommonTime(dm, lm);

        return new ResponseDTO(lm.getLocation(), lm.getLatitude(), lm.getLongitude(),
                dm.getDate(), tm.getSunriseTime(), tm.getSunsetTime());
    }

    @Override
    @Transactional
    public ResponseDTO deleteSunriseAndSunsetTime(Integer locationId, Integer dateId) {

        DateModel dateModel = dateRepository.findById(dateId).orElseThrow(
                () -> new MyRuntimeException("Wrong date id."));
        LocationModel locationModel = locationRepository.findById(locationId).orElseThrow(
                () -> new MyRuntimeException("Wrong location id."));

        TimeModel timeModel = commonService.getCommonTime(dateModel, locationModel);

        if ((locationModel.getDates().contains(dateModel) && locationModel.getTimes().contains(timeModel)) &&
                (timeModel.getDates().contains(dateModel) && timeModel.getLocations().contains(locationModel)) &&
                (dateModel.getTimes().contains(timeModel) && dateModel.getLocations().contains(locationModel)))
        {
            dateModel.deleteLocation(locationModel);
            timeModel.deleteLocation(locationModel);

            locationModel.deleteDate(dateModel);
            timeModel.deleteDate(dateModel);

            locationModel.deleteTime(timeModel);
            dateModel.deleteTime(timeModel);
        }
        else
            throw new MyRuntimeException("Not connected data.");

        if(locationModel.getDates().isEmpty() && locationModel.getTimes().isEmpty())
            locationRepository.delete(locationModel);

        if(dateModel.getTimes().isEmpty() && dateModel.getLocations().isEmpty())
            dateRepository.delete(dateModel);

        if(timeModel.getDates().isEmpty() && timeModel.getLocations().isEmpty())
            timeRepository.delete(timeModel);

        return new ResponseDTO(locationModel.getLocation(), locationModel.getLatitude(),
                locationModel.getLongitude(), dateModel.getDate(), timeModel.getSunriseTime(),
                timeModel.getSunsetTime());
    }
}
