package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.TimeRequest;
import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import com.example.sunriseandsunsetservice.service.TimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeControllerTest {

    @Mock
    private TimeService timeService;

    @InjectMocks
    private TimeController timeController;

    @Test
    void createTimeTest() {

        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        TimeResponse expectedResult = new TimeResponse(testSunriseTime, testSunsetTime);

        when(timeService.createTime(testSunriseTime, testSunsetTime)).thenReturn(expectedResult);

        ResponseEntity<TimeResponse> response = timeController.createTime(testSunriseTime, testSunsetTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void createManyTimesTest() {

        List<TimeRequest> testTimes = List.of(new TimeRequest(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeRequest(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));
        List<TimeResponse> expectedResult = List.of(new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeResponse(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));

        when(timeService.createManyTimes(testTimes)).thenReturn(expectedResult);

        ResponseEntity<List<TimeResponse>> response = timeController.createManyTimes(testTimes);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void readAllTimesTest() {

        List<TimeResponse> expectedResult = List.of(new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeResponse(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));

        when(timeService.readAllTimes()).thenReturn(expectedResult);

        ResponseEntity<List<TimeResponse>> response = timeController.readAllTimes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getByIdTest() {

        Integer testId = 1;
        TimeResponse expectedResult = new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28));

        when(timeService.getById(testId)).thenReturn(expectedResult);

        ResponseEntity<TimeResponse> response = timeController.getById(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void updateTimeTest() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        TimeResponse expectedResult = new TimeResponse(testSunriseTime, testSunsetTime);

        when(timeService.updateTime(testId, testSunriseTime, testSunsetTime)).thenReturn(expectedResult);

        ResponseEntity<TimeResponse> response = timeController.updateTime(testId, testSunriseTime, testSunsetTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void deleteTimeTest() {

        Integer testId = 1;
        TimeResponse expectedResult = new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28));

        when(timeService.deleteTime(testId)).thenReturn(expectedResult);

        ResponseEntity<TimeResponse> response = timeController.deleteTime(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }
}
