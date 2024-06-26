package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.TimezoneRequest;
import com.example.sunriseandsunsetservice.dto.response.TimezoneResponse;
import com.example.sunriseandsunsetservice.service.TimezoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimezoneControllerTest {

    @Mock
    private TimezoneService timezoneService;

    @InjectMocks
    private TimezoneController timezoneController;

    @Test
    void createTimezoneTest() {

        String testTimezone = "Europe/Minsk";
        TimezoneResponse expectedResult = new TimezoneResponse(0, testTimezone);

        when(timezoneService.createTimezone(testTimezone)).thenReturn(expectedResult);

        ResponseEntity<TimezoneResponse> response = timezoneController.createTimezone(testTimezone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void createManyTimezonesTest() {

        List<TimezoneRequest> testTimezones = List.of(new TimezoneRequest("Europe/Minsk"), new TimezoneRequest("Europe/Moscow"));
        List<TimezoneResponse> expectedResult = List.of(new TimezoneResponse(0, "Europe/Minsk"), new TimezoneResponse(0, "Europe/Moscow"));

        when(timezoneService.createManyTimezones(testTimezones)).thenReturn(expectedResult);

        ResponseEntity<List<TimezoneResponse>> response = timezoneController.createManyTimezones(testTimezones);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void readAllTimezonesTest() {

        List<TimezoneResponse> expectedResult = List.of(new TimezoneResponse(0, "Europe/Minsk"), new TimezoneResponse(0, "Europe/Moscow"));

        when(timezoneService.readAllTimezones()).thenReturn(expectedResult);

        ResponseEntity<List<TimezoneResponse>> response = timezoneController.readAllTimezones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getByIdTest() {

        Integer testId = 1;
        TimezoneResponse expectedResult = new TimezoneResponse(0, "Europe/Minsk");

        when(timezoneService.getById(testId)).thenReturn(expectedResult);

        ResponseEntity<TimezoneResponse> response = timezoneController.getById(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void updateTimezoneTest() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        TimezoneResponse expectedResult = new TimezoneResponse(0, testTimezone);

        when(timezoneService.updateTimezone(testId, testTimezone)).thenReturn(expectedResult);

        ResponseEntity<TimezoneResponse> response = timezoneController.updateTimezone(testId, testTimezone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void deleteTimezoneTest() {

        Integer testId = 1;
        TimezoneResponse expectedResult = new TimezoneResponse(0, "Europe/Minsk");

        when(timezoneService.deleteTimezone(testId)).thenReturn(expectedResult);

        ResponseEntity<TimezoneResponse> response = timezoneController.deleteTimezone(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }
}
