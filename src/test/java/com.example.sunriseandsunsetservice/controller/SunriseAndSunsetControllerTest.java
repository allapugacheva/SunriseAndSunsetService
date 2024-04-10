package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.SunriseAndSunsetRequest;
import com.example.sunriseandsunsetservice.dto.response.DaytimeResponse;
import com.example.sunriseandsunsetservice.dto.response.SunriseAndSunsetResponse;
import com.example.sunriseandsunsetservice.service.SunriseAndSunsetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SunriseAndSunsetControllerTest {

    @Mock
    SunriseAndSunsetService sunService;

    @InjectMocks
    SunriseAndSunsetController sunController;

    @Test
    void findSunriseAndSunsetTimeTest() {

        Double testLat = 52.111385, testLng = 26.102528;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        SunriseAndSunsetResponse expectedResult = new SunriseAndSunsetResponse("Пинск", testLat, testLng,
                testDate, LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(sunService.findSunriseAndSunsetTime(testLat, testLng, testDate)).thenReturn(expectedResult);

        ResponseEntity<SunriseAndSunsetResponse> response = sunController.findSunriseAndSunsetTime(testLat, testLng, testDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void findManySunriseAndSunsetTimesTest() {

        List<SunriseAndSunsetRequest> testData = List.of(new SunriseAndSunsetRequest(52.111385, 26.102528, LocalDate.of(2024, 4, 8)),
                new SunriseAndSunsetRequest(53.132294, 26.018415, LocalDate.of(2024, 4, 8)));
        List<SunriseAndSunsetResponse> expectedResult = List.of(new SunriseAndSunsetResponse("Пинск", 52.111385, 26.102528, LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)),
                new SunriseAndSunsetResponse("Барановичи", 53.132294, 26.018415, LocalDate.of(2024, 4, 8), LocalTime.of(6, 29, 51), LocalTime.of(20, 5, 28)));

        when(sunService.findManySunriseAndSunsetTimes(testData)).thenReturn(expectedResult);

        ResponseEntity<List<SunriseAndSunsetResponse>> response = sunController.findManySunriseAndSunsetTimes(testData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void readAllSunrisesAnsSunsetsTest() {

        List<SunriseAndSunsetResponse> expectedResult = List.of(new SunriseAndSunsetResponse("Пинск", 52.111385, 26.102528, LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)),
                new SunriseAndSunsetResponse("Барановичи", 53.132294, 26.018415, LocalDate.of(2024, 4, 8), LocalTime.of(6, 29, 51), LocalTime.of(20, 5, 28)));

        when(sunService.readAllSunrisesAnsSunsets()).thenReturn(expectedResult);

        ResponseEntity<List<SunriseAndSunsetResponse>> response = sunController.readAllSunrisesAnsSunsets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getByIdTest() {

        Integer testDateId = 1, testLocationId = 1;
        SunriseAndSunsetResponse expectedResult = new SunriseAndSunsetResponse("Пинск", 52.111385, 26.102528,
                LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(sunService.getById(testLocationId, testDateId)).thenReturn(expectedResult);

        ResponseEntity<SunriseAndSunsetResponse> response = sunController.getById(testLocationId, testDateId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void updateSunriseAndSunsetTest() {

        Integer testDateId = 1, testLocationId = 1;
        Double testLat = 52.111385, testLng = 26.102528;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        SunriseAndSunsetResponse expectedResult = new SunriseAndSunsetResponse("Пинск", testLat, testLng,
                testDate, LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(sunService.updateSunriseAndSunset(testLocationId, testDateId, testLat, testLng, testDate)).thenReturn(expectedResult);

        ResponseEntity<SunriseAndSunsetResponse> response = sunController.updateSunriseAndSunset(testLocationId, testDateId, testLat, testLng, testDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void deleteSunriseAndSunsetTimeTest() {

        Integer testDateId = 1, testLocationId = 1;
        SunriseAndSunsetResponse expectedResult = new SunriseAndSunsetResponse("Пинск", 52.111385, 26.102528,
                LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(sunService.deleteSunriseAndSunsetTime(testLocationId, testDateId)).thenReturn(expectedResult);

        ResponseEntity<SunriseAndSunsetResponse> response = sunController.deleteSunriseAndSunsetTime(testLocationId, testDateId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void findDaytimeLengthTest() {

        Integer testDateId = 1, testLocationId = 1;
        DaytimeResponse expectedResult = new DaytimeResponse(LocalTime.of(13, 32, 18));

        when(sunService.findDaytimeLength(testDateId, testLocationId)).thenReturn(expectedResult);

        ResponseEntity<DaytimeResponse> response = sunController.findDaytimeLength(testDateId, testLocationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }
}
