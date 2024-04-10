package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.TimeRequest;
import com.example.sunriseandsunsetservice.dto.response.TimeResponse;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private TimeServiceImpl timeService;

    @Test
    void createTimeTestNew() {

        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(timeRepository.findBySunriseTimeAndSunsetTime(testSunriseTime, testSunsetTime)).thenReturn(null);
        when(timeRepository.save(any(Time.class))).thenReturn(expectedResult);

        TimeResponse response = timeService.createTime(testSunriseTime, testSunsetTime);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(timeRepository).findBySunriseTimeAndSunsetTime(testSunriseTime, testSunsetTime);
        verify(timeRepository).save(any(Time.class));
    }

    @Test
    void createTimeTestExisting() {

        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(timeRepository.findBySunriseTimeAndSunsetTime(testSunriseTime, testSunsetTime)).thenReturn(expectedResult);

        TimeResponse response = timeService.createTime(testSunriseTime, testSunsetTime);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(timeRepository).findBySunriseTimeAndSunsetTime(testSunriseTime, testSunsetTime);
    }

    @Test
    void createManyTimesTest() {

        List<TimeRequest> testTimes = List.of(new TimeRequest(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeRequest(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));
        List<TimeResponse> expectedResult = List.of(new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeResponse(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));

        when(timeRepository.findBySunriseTimeAndSunsetTime(LocalTime.of(6,31,10), LocalTime.of(20,3,28))).thenReturn(new Time(LocalTime.of(6,31,10), LocalTime.of(20,3,28)));
        when(timeRepository.findBySunriseTimeAndSunsetTime(LocalTime.of(6,28,54), LocalTime.of(20,5,11))).thenReturn(new Time(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));

        List<TimeResponse> response = timeService.createManyTimes(testTimes);

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(TimeResponse::getSunriseTime).collect(Collectors.toList()),
                response.stream().map(TimeResponse::getSunriseTime).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(TimeResponse::getSunsetTime).collect(Collectors.toList()),
                response.stream().map(TimeResponse::getSunsetTime).collect(Collectors.toList()));

        verify(timeRepository).findBySunriseTimeAndSunsetTime(LocalTime.of(6,28,54), LocalTime.of(20,5,11));
        verify(timeRepository).findBySunriseTimeAndSunsetTime(LocalTime.of(6,31,10), LocalTime.of(20,3,28));
    }

    @Test
    void readAllTimesTest() {

        List<TimeResponse> expectedResult = List.of(new TimeResponse(LocalTime.of(6,31,10), LocalTime.of(20,3,28)),
                new TimeResponse(LocalTime.of(6,28,54), LocalTime.of(20,5,11)));

        when(timeRepository.findAllTimes()).thenReturn(expectedResult);

        List<TimeResponse> response = timeService.readAllTimes();

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(TimeResponse::getSunriseTime).collect(Collectors.toList()),
                response.stream().map(TimeResponse::getSunriseTime).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(TimeResponse::getSunsetTime).collect(Collectors.toList()),
                response.stream().map(TimeResponse::getSunsetTime).collect(Collectors.toList()));

        verify(timeRepository).findAllTimes();
    }

    @Test
    void getByIdTestFromCache() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(expectedResult);

        TimeResponse response = timeService.getById(testId);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
    }

    @Test
    void getByIdTestFromDatabase() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(null);
        when(timeRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimeResponse response = timeService.getById(testId);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
        verify(timeRepository).findById(testId);
    }

    @Test
    void getByIdTestNotFound() {

        Integer testId = 1;

        when(cache.get("Time" + testId)).thenReturn(null);
        when(timeRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> timeService.getById(testId));

        verify(cache).get("Time" + testId);
        verify(timeRepository).findById(testId);
    }

    @Test
    void updateTimeTestFromCache() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(expectedResult);

        TimeResponse response = timeService.updateTime(testId, testSunriseTime, testSunsetTime);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
    }

    @Test
    void updateTimeTestFromDatabase() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(null);
        when(timeRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimeResponse response = timeService.updateTime(testId, testSunriseTime, testSunsetTime);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
        verify(timeRepository).findById(testId);
    }

    @Test
    void deleteTimeTestFromCache() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(expectedResult);

        TimeResponse response = timeService.deleteTime(testId);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
    }

    @Test
    void deleteTimeTestFromDatabase() {

        Integer testId = 1;
        LocalTime testSunriseTime = LocalTime.of(6,31,10), testSunsetTime = LocalTime.of(20,3,28);
        Time expectedResult = new Time(testSunriseTime, testSunsetTime);

        when(cache.get("Time" + testId)).thenReturn(null);
        when(timeRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimeResponse response = timeService.deleteTime(testId);

        assertEquals(testSunriseTime, response.getSunriseTime());
        assertEquals(testSunsetTime, response.getSunsetTime());

        verify(cache).get("Time" + testId);
        verify(timeRepository).findById(testId);
    }

    @Test
    void deleteTimeTestNotFound() {

        Integer testId = 1;

        when(timeRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> timeService.deleteTime(testId));
    }
}
