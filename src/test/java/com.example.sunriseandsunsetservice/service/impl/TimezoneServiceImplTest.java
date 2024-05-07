package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.TimezoneRequest;
import com.example.sunriseandsunsetservice.dto.response.TimezoneResponse;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimezoneServiceImplTest {

    @Mock
    private TimezoneRepository timezoneRepository;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private TimezoneServiceImpl timezoneService;

    @Test
    void createTimezoneTestNew() {

        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(timezoneRepository.findBySunTimezone(testTimezone)).thenReturn(null);
        when(timezoneRepository.save(any(Timezone.class))).thenReturn(expectedResult);

        TimezoneResponse response = timezoneService.createTimezone(testTimezone);

        assertEquals(testTimezone, response.getTimezone());

        verify(timezoneRepository).findBySunTimezone(testTimezone);
        verify(timezoneRepository).save(any(Timezone.class));
    }

    @Test
    void createTimeTestExisting() {

        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(timezoneRepository.findBySunTimezone(testTimezone)).thenReturn(expectedResult);

        TimezoneResponse response = timezoneService.createTimezone(testTimezone);

        assertEquals(testTimezone, response.getTimezone());

        verify(timezoneRepository).findBySunTimezone(testTimezone);
    }

    @Test
    void createManyTimesTest() {

        List<TimezoneRequest> testTimezones = List.of(new TimezoneRequest("Europe/Minsk"), new TimezoneRequest("Europe/Moscow"));
        List<TimezoneResponse> expectedResult = List.of(new TimezoneResponse(0, "Europe/Minsk"), new TimezoneResponse(0, "Europe/Moscow"));

        when(timezoneRepository.findBySunTimezone("Europe/Minsk")).thenReturn(new Timezone("Europe/Minsk"));
        when(timezoneRepository.findBySunTimezone("Europe/Moscow")).thenReturn(new Timezone("Europe/Moscow"));

        List<TimezoneResponse> response = timezoneService.createManyTimezones(testTimezones);

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(TimezoneResponse::getTimezone).collect(Collectors.toList()),
                response.stream().map(TimezoneResponse::getTimezone).collect(Collectors.toList()));

        verify(timezoneRepository, times(2)).findBySunTimezone(anyString());
    }

    @Test
    void readAllTimesTest() {

        List<TimezoneResponse> expectedResult = List.of(new TimezoneResponse(0, "Europe/Minsk"), new TimezoneResponse(0, "Europe/Moscow"));

        when(timezoneRepository.findAllTimezones()).thenReturn(expectedResult);

        List<TimezoneResponse> response = timezoneService.readAllTimezones();

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(TimezoneResponse::getTimezone).collect(Collectors.toList()),
                response.stream().map(TimezoneResponse::getTimezone).collect(Collectors.toList()));

        verify(timezoneRepository).findAllTimezones();
    }

    @Test
    void getByIdTestFromCache() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(expectedResult);

        TimezoneResponse response = timezoneService.getById(testId);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
    }

    @Test
    void getByIdTestFromDatabase() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(null);
        when(timezoneRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimezoneResponse response = timezoneService.getById(testId);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
        verify(timezoneRepository).findById(testId);
    }

    @Test
    void getByIdTestNotFound() {

        Integer testId = 1;

        when(cache.get("Timezone" + testId)).thenReturn(null);
        when(timezoneRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> timezoneService.getById(testId));

        verify(cache).get("Timezone" + testId);
        verify(timezoneRepository).findById(testId);
    }

    @Test
    void updateTimeTestFromCache() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(expectedResult);

        TimezoneResponse response = timezoneService.updateTimezone(testId, testTimezone);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
    }

    @Test
    void updateTimeTestFromDatabase() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(null);
        when(timezoneRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimezoneResponse response = timezoneService.updateTimezone(testId, testTimezone);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
        verify(timezoneRepository).findById(testId);
    }

    @Test
    void deleteTimeTestFromCache() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(expectedResult);

        TimezoneResponse response = timezoneService.deleteTimezone(testId);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
    }

    @Test
    void deleteTimeTestFromDatabase() {

        Integer testId = 1;
        String testTimezone = "Europe/Minsk";
        Timezone expectedResult = new Timezone(testTimezone);

        when(cache.get("Timezone" + testId)).thenReturn(null);
        when(timezoneRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        TimezoneResponse response = timezoneService.deleteTimezone(testId);

        assertEquals(testTimezone, response.getTimezone());

        verify(cache).get("Timezone" + testId);
        verify(timezoneRepository).findById(testId);
    }

    @Test
    void deleteTimeTestNotFound() {

        Integer testId = 1;

        when(timezoneRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> timezoneService.deleteTimezone(testId));

        verify(timezoneRepository).findById(testId);
    }
}
