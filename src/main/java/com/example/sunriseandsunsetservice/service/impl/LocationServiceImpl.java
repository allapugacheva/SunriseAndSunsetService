package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.LocationDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.service.LocationService;
import com.example.sunriseandsunsetservice.service.CommonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CommonService commonService;

    private final InMemoryCache cache;

    private static final String Key = "Location";

    @Override
    @Transactional
    public LocationDTO createLocation(Double lat, Double lng) {

        if (commonService.notValidLat(lat) || commonService.notValidLng(lng))
            throw new MyRuntimeException("Invalid latitude or longitude.");

        Location location;
        if ((location = locationRepository.findByLatitudeAndLongitude(lat, lng)) == null) {
            location = new Location();

            location.setLatitude(lat);
            location.setLongitude(lng);

            String[] place = commonService.getTimezoneAndPlace(lat, lng);
            location.setSunLocation(place[1]);

            locationRepository.save(location);
        }

        cache.put(Key + location.getId().toString(), location);

        return new LocationDTO(location.getSunLocation(), lat, lng);
    }

    @Override
    public List<LocationDTO> readAllLocations() { return locationRepository.findAllLocations(); }

    @Override
    public LocationDTO getById(Integer id) {

        Location tempLocation = (Location) cache.get(Key + id.toString());

        if (tempLocation == null) {
            tempLocation = locationRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Location not found."));

            cache.put(Key + id, tempLocation);
        }

        return new LocationDTO(tempLocation.getSunLocation(), tempLocation.getLatitude(), tempLocation.getLongitude());
    }

    @Override
    @Transactional
    public LocationDTO updateLocation(Integer id, Double lat, Double lng) {

        return new LocationDTO(commonService.updateLocation(id, lat, lng), lat, lng);
    }

    @Override
    @Transactional
    public LocationDTO deleteLocation(Integer id) {

        Location location = locationRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (location.getDates().isEmpty() && location.getTimes().isEmpty()) {
            locationRepository.delete(location);
            cache.remove(Key + id);

        } else throw new MyRuntimeException("Location has connections.");

        return new LocationDTO(location.getSunLocation(), location.getLatitude(), location.getLongitude());
    }
}
