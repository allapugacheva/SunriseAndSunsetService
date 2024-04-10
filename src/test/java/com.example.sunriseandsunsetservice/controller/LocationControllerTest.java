package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.LocationRequest;
import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import com.example.sunriseandsunsetservice.dto.response.LocationResponse;
import com.example.sunriseandsunsetservice.service.LocationService;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @Test
    public void createLocationTest() {

        Double testLat = 53.132294, testLng = 26.018415;
        LocationResponse expectedResult = new LocationResponse("Барановичи", testLat, testLng);

        when(locationService.createLocation(testLat, testLng)).thenReturn(expectedResult);

        ResponseEntity<LocationResponse> response = locationController.createLocation(testLat, testLng);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void createManyLocationsTest() {

        List<LocationRequest> testLocations = List.of(new LocationRequest(53.132294, 26.018415),
                new LocationRequest(52.111385, 26.102528));
        List<LocationResponse> expectedResult = List.of(new LocationResponse("Барановичи", 53.132294, 26.018415),
                new LocationResponse("Пинск", 52.111385, 26.102528));

        when(locationService.createManyLocations(testLocations)).thenReturn(expectedResult);

        ResponseEntity<List<LocationResponse>> response = locationController.createManyLocations(testLocations);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void readAllLocationsTest() {

        List<LocationResponse> expectedResult = List.of(new LocationResponse("Барановичи", 53.132294, 26.018415),
                new LocationResponse("Пинск", 52.111385, 26.102528));

        when(locationService.readAllLocations()).thenReturn(expectedResult);

        ResponseEntity<List<LocationResponse>> response = locationController.readAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void getByIdTest() {

        Integer testId = 1;
        LocationResponse expectedResult = new LocationResponse("Барановичи", 53.132294, 26.018415);

        when(locationService.getById(testId)).thenReturn(expectedResult);

        ResponseEntity<LocationResponse> response = locationController.getById(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void updateLocationTest() {

        Integer testId = 1;
        Double testLat = 53.132294, testLng = 26.018415;
        LocationResponse expectedResult = new LocationResponse("Барановичи", testLat, testLng);

        when(locationService.updateLocation(testId, testLat, testLng)).thenReturn(expectedResult);

        ResponseEntity<LocationResponse> response = locationController.updateLocation(testId, testLat, testLng);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void deleteLocationTest() {

        Integer testId = 1;
        LocationResponse expectedResult = new LocationResponse("Барановичи", 53.132294, 26.018415);

        when(locationService.deleteLocation(testId)).thenReturn(expectedResult);

        ResponseEntity<LocationResponse> response = locationController.deleteLocation(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }
}
