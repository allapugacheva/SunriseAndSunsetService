package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.dto.LocationDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.LocationModel;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.service.LocationService;
import com.example.sunriseandsunsetservice.service.CommonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CommonService commonService;

    @Override
    @Transactional
    public LocationDTO createLocation(Double lat, Double lng) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new RuntimeException("Invalid latitude or longitude.");

        LocationModel locationModel;
        if ((locationModel = locationRepository.findByLatitudeAndLongitude(lat, lng)) == null) {
            locationModel = new LocationModel();

            locationModel.setLatitude(lat);
            locationModel.setLongitude(lng);

            String[] location = commonService.getTimezoneAndPlace(lat, lng);
            locationModel.setLocation(location[1]);

            locationRepository.save(locationModel);
        }

        return new LocationDTO(locationModel.getLocation(), lat, lng);
    }

    @Override
    public List<LocationDTO> readAllLocations() {

        List<LocationDTO> locations = new ArrayList<>();

        for(LocationModel lm : locationRepository.findAll())
            locations.add(new LocationDTO(lm.getLocation(), lm.getLatitude(), lm.getLongitude()));

        return locations;
    }

    @Override
    @Transactional
    public LocationDTO updateLocation(int id, Double lat, Double lng) {

        return new LocationDTO(commonService.updateLocation(id, lat, lng), lat, lng);
    }

    @Override
    @Transactional
    public LocationDTO deleteLocation(int id) {

        LocationModel locationModel = locationRepository.findById(id);
        if(locationModel == null)
            throw new MyRuntimeException("Wrong id");

        if (locationModel.getDates().isEmpty() && locationModel.getTimes().isEmpty())
            locationRepository.delete(locationModel);
        else throw new MyRuntimeException("Location has connections.");

        return new LocationDTO(locationModel.getLocation(), locationModel.getLatitude(), locationModel.getLongitude());
    }
}
