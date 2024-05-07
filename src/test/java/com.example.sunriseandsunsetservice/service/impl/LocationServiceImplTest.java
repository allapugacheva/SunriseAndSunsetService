package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.LocationRequest;
import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void createLocationTestNew() {

        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location(testLocation, testLat, testLng);

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(expectedResult);

        LocationResponse response = locationService.createLocation(testLat, testLng);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
    }

    @Test
    void createLocationTestExisting() {

        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location(testLocation, testLat, testLng);

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(expectedResult);

        LocationResponse response = locationService.createLocation(testLat, testLng);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
    }

    @Test
    void createManyLocationsTest() {

        List<LocationRequest> testLocations = List.of(new LocationRequest(53.132294, 26.018415),
                new LocationRequest(52.111385, 26.102528));
        List<LocationResponse> expectedResult = List.of(new LocationResponse(0, "Барановичи", 53.132294, 26.018415),
                new LocationResponse(0, "Пинск", 52.111385, 26.102528));

        when(locationRepository.findByLatitudeAndLongitude(testLocations.get(0).getLat(), testLocations.get(0).getLng())).thenReturn(new Location("Барановичи" ,testLocations.get(0).getLat(), testLocations.get(0).getLng()));
        when(locationRepository.findByLatitudeAndLongitude(testLocations.get(1).getLat(), testLocations.get(1).getLng())).thenReturn(new Location("Пинск" ,testLocations.get(1).getLat(), testLocations.get(1).getLng()));

        List<LocationResponse> response = locationService.createManyLocations(testLocations);

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLocation).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLocation).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLatitude).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLatitude).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLongitude).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLongitude).collect(Collectors.toList()));

        verify(locationRepository).findByLatitudeAndLongitude(53.132294, 26.018415);
        verify(locationRepository).findByLatitudeAndLongitude(52.111385, 26.102528);
    }

    @Test
    void readAllLocationsTest() {

        List<LocationResponse> expectedResult = List.of(new LocationResponse(0, "Барановичи", 53.132294, 26.018415),
                new LocationResponse(0, "Пинск", 52.111385, 26.102528));

        when(locationRepository.findAllLocations()).thenReturn(expectedResult);

        List<LocationResponse> response = locationService.readAllLocations();

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLocation).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLocation).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLatitude).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLatitude).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(LocationResponse::getLongitude).collect(Collectors.toList()),
                response.stream().map(LocationResponse::getLongitude).collect(Collectors.toList()));

        verify(locationRepository).findAllLocations();
    }

    @Test
    void getByIdTestFromCache() {

        Integer testId = 1;
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location(testLocation, testLat, testLng);

        when(cache.get("Location" + testId)).thenReturn(expectedResult);

        LocationResponse response = locationService.getById(testId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(cache).get("Location" + testId);
    }

    @Test
    void getByIdTestFromDatabase() {

        Integer testId = 1;
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location(testLocation, testLat, testLng);

        when(cache.get("Location" + testId)).thenReturn(null);
        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        LocationResponse response = locationService.getById(testId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(cache).get("Location" + testId);
        verify(locationRepository).findById(testId);
    }

    @Test
    void getByIdTestNotFound() {

        Integer testId = 1;

        when(cache.get("Location" + testId)).thenReturn(null);
        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> locationService.getById(testId));

        verify(cache).get("Location" + testId);
        verify(locationRepository).findById(testId);
    }

    @Test
    void updateLocationTest() {

        Integer testId = 1;
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;

        when(commonService.updateLocation(testId, testLat, testLng)).thenReturn(testLocation);

        LocationResponse response = locationService.updateLocation(testId, testLat, testLng);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(commonService).updateLocation(testId, testLat, testLng);
    }

    @Test
    void deleteLocationTestExisting() {

        Integer testId = 1;
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location(testLocation, testLat, testLng);

        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        LocationResponse response = locationService.deleteLocation(testId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());

        verify(locationRepository).findById(testId);
    }

    @Test
    void deleteLocationTestNotFound() {

        Integer testId = 1;

        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> locationService.deleteLocation(testId));

        verify(locationRepository).findById(testId);
    }
}
