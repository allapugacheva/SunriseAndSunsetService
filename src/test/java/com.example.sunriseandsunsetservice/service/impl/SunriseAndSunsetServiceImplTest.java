package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.SunriseAndSunsetRequest;
import com.example.sunriseandsunsetservice.dto.response.*;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.model.Time;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SunriseAndSunsetServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private DateRepository dateRepository;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private TimezoneRepository timezoneRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private SunriseAndSunsetServiceImpl service;

    @Test
    void findSunriseAndSunsetTimeTestExisting() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        String testTimezone = "Europe/Minsk";
        expectedLocation.setTimezone(new Timezone(testTimezone));

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(expectedLocation);
        when(commonService.getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone)).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));
        when(dateRepository.findBySunDate(testDate)).thenReturn(expectedDate);

        SunriseAndSunsetResponse response = service.findSunriseAndSunsetTime(testLat, testLng, testDate);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
        verify(dateRepository).findBySunDate(testDate);
    }

    @Test
    void findSunriseAndSunsetTimeTestNoLocation() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        String testTimezone = "Europe/Minsk";

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(null);
        when(commonService.getTimezoneAndPlace(testLat, testLng)).thenReturn(new String[] {testTimezone, testLocation});
        when(locationRepository.save(any(Location.class))).thenReturn(new Location(testLocation, testLat, testLng));
        when(timezoneRepository.findBySunTimezone(anyString())).thenReturn(new Timezone(testTimezone));
        when(dateRepository.findBySunDate(testDate)).thenReturn(expectedDate);
        when(commonService.getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone)).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));

        SunriseAndSunsetResponse response = service.findSunriseAndSunsetTime(testLat, testLng, testDate);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
        verify(commonService).getTimezoneAndPlace(testLat, testLng);
        verify(timezoneRepository).findBySunTimezone(anyString());
        verify(dateRepository).findBySunDate(testDate);
        verify(commonService).getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone);
    }

    @Test
    void findSunriseAndSunsetTimeTestNoDate() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        String testTimezone = "Europe/Minsk";
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        expectedLocation.setTimezone(new Timezone(testTimezone));

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(expectedLocation);
        when(dateRepository.findBySunDate(testDate)).thenReturn(null);
        when(dateRepository.save(any(Date.class))).thenReturn(new Date(testDate));
        when(commonService.getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone)).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));

        SunriseAndSunsetResponse response = service.findSunriseAndSunsetTime(testLat, testLng, testDate);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
        verify(dateRepository).findBySunDate(testDate);
        verify(dateRepository).save(any(Date.class));
        verify(commonService).getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone);
    }

    @Test
    void findSunriseAndSunsetTimeTestNew() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        String testTimezone = "Europe/Minsk";

        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(null);
        when(commonService.getTimezoneAndPlace(testLat, testLng)).thenReturn(new String[] {testTimezone, testLocation});
        when(locationRepository.save(any(Location.class))).thenReturn(new Location(testLocation, testLat, testLng));
        when(timezoneRepository.findBySunTimezone(anyString())).thenReturn(new Timezone(testTimezone));
        when(dateRepository.findBySunDate(testDate)).thenReturn(null);
        when(dateRepository.save(any(Date.class))).thenReturn(new Date(testDate));
        when(commonService.getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone)).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));

        SunriseAndSunsetResponse response = service.findSunriseAndSunsetTime(testLat, testLng, testDate);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(locationRepository).findByLatitudeAndLongitude(testLat, testLng);
        verify(commonService).getTimezoneAndPlace(testLat, testLng);
        verify(timezoneRepository).findBySunTimezone(anyString());
        verify(dateRepository).findBySunDate(testDate);
        verify(dateRepository).findBySunDate(testDate);
        verify(dateRepository).save(any(Date.class));
        verify(commonService).getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone);
    }

    @Test
    void createManyLocationsTest() {

        Double testLat1 = 52.111385, testLng1 = 26.102528, testLat2 = 53.132294, testLng2 = 26.018415;
        String testLocation1 = "Пинск", testLocation2 = "Барановичи";

        Location expectedLocation1 = new Location(testLocation1, testLat1, testLng1);
        Location expectedLocation2 = new Location(testLocation2, 53.132294, 26.018415);
        Timezone testTimezone = new Timezone("Europe/Minsk");
        expectedLocation1.setTimezone(testTimezone);
        expectedLocation2.setTimezone(testTimezone);

        List<SunriseAndSunsetRequest> testData = List.of(new SunriseAndSunsetRequest(testLat1, testLng1, LocalDate.of(2024, 4, 8)),
                new SunriseAndSunsetRequest(53.132294, 26.018415, LocalDate.of(2024, 4, 8)));
        List<SunriseAndSunsetResponse> expectedResult = List.of(new SunriseAndSunsetResponse(0, 0, testLocation1, testLat1, testLng1, LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)),
                new SunriseAndSunsetResponse(0, 0, testLocation2, 53.132294, 26.018415, LocalDate.of(2024, 4, 8), LocalTime.of(6, 29, 51), LocalTime.of(20, 5, 28)));

        when(locationRepository.findByLatitudeAndLongitude(testLat1, testLng1)).thenReturn(expectedLocation1);
        when(locationRepository.findByLatitudeAndLongitude(testLat2, testLng2)).thenReturn(expectedLocation2);
        when(dateRepository.findBySunDate(LocalDate.of(2024, 4, 8))).thenReturn(new Date(LocalDate.of(2024, 4, 8)));
        when(dateRepository.findBySunDate(LocalDate.of(2024, 4, 8))).thenReturn(new Date(LocalDate.of(2024, 4, 9)));
        when(commonService.getSunriseAndSunsetTime(testLat1, testLng1, LocalDate.of(2024, 4, 8), testTimezone.getSunTimezone())).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));
        when(commonService.getSunriseAndSunsetTime(testLat2, testLng2, LocalDate.of(2024, 4, 8), testTimezone.getSunTimezone())).thenReturn(new Time(LocalTime.of(6, 29, 51), LocalTime.of(20, 5, 28)));

        List<SunriseAndSunsetResponse> response = service.findManySunriseAndSunsetTimes(testData);

        assertEquals(2, response.size());
        assertEquals(expectedResult.get(0).getLocation(), response.get(0).getLocation());
        assertEquals(expectedResult.get(1).getLocation(), response.get(1).getLocation());
        assertEquals(expectedResult.get(0).getLatitude(), response.get(0).getLatitude());
        assertEquals(expectedResult.get(1).getLatitude(), response.get(1).getLatitude());
        assertEquals(expectedResult.get(0).getLongitude(), response.get(0).getLongitude());
        assertEquals(expectedResult.get(1).getLongitude(), response.get(1).getLongitude());

        verify(locationRepository).findByLatitudeAndLongitude(testLat1, testLng1);
        verify(locationRepository).findByLatitudeAndLongitude(testLat2, testLng2);
        verify(dateRepository, times(2)).findBySunDate(LocalDate.of(2024, 4, 8));
        verify(commonService).getSunriseAndSunsetTime(testLat1, testLng1, LocalDate.of(2024, 4, 8), testTimezone.getSunTimezone());
        verify(commonService).getSunriseAndSunsetTime(testLat2, testLng2, LocalDate.of(2024, 4, 8), testTimezone.getSunTimezone());
    }

    @Test
    void readAllLocationsTest() {

        Object[] row1 = {0, 0, "Пинск", 52.111385, 26.102528, java.sql.Date.valueOf(LocalDate.of(2024, 4, 8)), java.sql.Time.valueOf(LocalTime.of(6, 31, 10)), java.sql.Time.valueOf(LocalTime.of(20, 3, 28))};
        Object[] row2 = {0, 0, "Барановичи", 53.132294, 26.018415, java.sql.Date.valueOf(LocalDate.of(2024, 4, 9)), java.sql.Time.valueOf(LocalTime.of(6, 29, 51)), java.sql.Time.valueOf(LocalTime.of(20, 5, 28))};
        List<SunriseAndSunsetResponse> expectedResult = List.of(new SunriseAndSunsetResponse(0, 0, "Пинск", 52.111385, 26.102528, LocalDate.of(2024, 4, 8), LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)),
                new SunriseAndSunsetResponse(0, 0, "Барановичи", 53.132294, 26.018415, LocalDate.of(2024, 4, 8), LocalTime.of(6, 29, 51), LocalTime.of(20, 5, 28)));

        when(locationRepository.findAllData()).thenReturn(Arrays.asList(row1, row2));

        List<SunriseAndSunsetResponse> response = service.readAllSunrisesAnsSunsets();

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(SunriseAndSunsetResponse::getLocation).collect(Collectors.toList()),
                response.stream().map(SunriseAndSunsetResponse::getLocation).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(SunriseAndSunsetResponse::getLatitude).collect(Collectors.toList()),
                response.stream().map(SunriseAndSunsetResponse::getLatitude).collect(Collectors.toList()));
        assertIterableEquals(expectedResult.stream().map(SunriseAndSunsetResponse::getLongitude).collect(Collectors.toList()),
                response.stream().map(SunriseAndSunsetResponse::getLongitude).collect(Collectors.toList()));

        verify(locationRepository).findAllData();
    }

    @Test
    void getByIdTestFromCache() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        Time expectedTime = new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(cache.get("Location" + testLocationId)).thenReturn(expectedLocation);
        when(cache.get("Date" + testDateId)).thenReturn(expectedDate);
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(expectedTime);

        SunriseAndSunsetResponse response = service.getById(testLocationId, testDateId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(cache).get("Location" + testLocationId);
        verify(cache).get("Date" + testDateId);
        verify(timeRepository).findCommonTime(testDateId, testLocationId);
    }

    @Test
    void getByIdTestFromDatabase() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        Time expectedTime = new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.of(expectedLocation));
        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.of(expectedDate));
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(expectedTime);

        SunriseAndSunsetResponse response = service.getById(testLocationId, testDateId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(cache).get("Location" + testLocationId);
        verify(cache).get("Date" + testDateId);
        verify(timeRepository).findCommonTime(testDateId, testLocationId);
    }

    @Test
    void getByIdTestNoLocation() {

        Integer testDateId = 1, testLocationId = 1;

        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getById(testLocationId, testDateId));

        verify(cache).get("Location" + testLocationId);
        verify(locationRepository).findById(testLocationId);
    }

    @Test
    void getByIdTestNoCommonTime() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);

        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.of(expectedLocation));
        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.of(expectedDate));
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.getById(testLocationId, testDateId));

        verify(cache).get("Location" + testLocationId);
        verify(cache).get("Date" + testDateId);
        verify(timeRepository).findCommonTime(testDateId, testLocationId);
    }

    @Test
    void getByIdTestNoDate() {

        Integer testDateId = 1, testLocationId = 1;
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);

        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.of(expectedLocation));
        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getById(testLocationId, testDateId));

        verify(cache).get("Location" + testLocationId);
        verify(cache).get("Date" + testDateId);
        verify(locationRepository).findById(testLocationId);
        verify(dateRepository).findById(testDateId);
    }

    @Test
    void updateSunriseAnsSunsetTestValidCoordinates() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        Date expectedDate = new Date(testDate);
        String testTimezone = "Europe/Minsk";
        expectedLocation.setTimezone(new Timezone(testTimezone));
        Time expectedTime = new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));
        expectedDate.addLocation(expectedLocation);
        expectedDate.addTime(expectedTime);
        expectedLocation.addTime(expectedTime);
        expectedLocation.addDate(expectedDate);
        expectedTime.addLocation(expectedLocation);
        expectedTime.addDate(expectedDate);

        when(cache.get("Date" + testDateId)).thenReturn(expectedDate);
        when(cache.get("Location" + testLocationId)).thenReturn(expectedLocation);
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(expectedTime);
        when(commonService.notValidLat(testLat)).thenReturn(false);
        when(commonService.notValidLng(testLng)).thenReturn(false);
        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(expectedLocation);
        when(commonService.getSunriseAndSunsetTime(testLat, testLng, testDate, testTimezone)).thenReturn(new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28)));
        when(dateRepository.findBySunDate(testDate)).thenReturn(expectedDate);

        SunriseAndSunsetResponse response = service.updateSunriseAndSunset(testLocationId, testDateId, testLat, testLng, testDate);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(commonService, times(2)).notValidLat(testLat);
        verify(commonService, times(2)).notValidLng(testLng);
    }

    @Test
    void updateSunriseAnsSunsetTestInValidCoordinates() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Double testLat = 52.111385, testLng = 26.102528;

        when(commonService.notValidLat(testLat)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.updateSunriseAndSunset(testLocationId, testDateId, testLat, testLng, testDate));

        verify(commonService).notValidLat(testLat);
    }

    @Test
    void deleteSunriseAnsSunsetTestFromCache() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        Time expectedTime = new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        expectedTime.addDate(expectedDate);
        expectedTime.addLocation(expectedLocation);

        expectedDate.addLocation(expectedLocation);
        expectedDate.addTime(expectedTime);

        expectedLocation.addDate(expectedDate);
        expectedLocation.addTime(expectedTime);

        when(cache.get("Date" + testDateId)).thenReturn(expectedDate);
        when(cache.get("Location" + testLocationId)).thenReturn(expectedLocation);
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(expectedTime);

        SunriseAndSunsetResponse response = service.deleteSunriseAndSunsetTime(testLocationId, testDateId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(cache, times(2)).get(anyString());
        verify(timeRepository).findCommonTime(testDateId, testLocationId);
    }

    @Test
    void deleteSunriseAnsSunsetTestFromDatabase() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);
        String testLocation = "Пинск";
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedLocation = new Location(testLocation, testLat, testLng);
        Time expectedTime = new Time(LocalTime.of(6, 31, 10), LocalTime.of(20, 3, 28));

        expectedTime.addDate(expectedDate);
        expectedTime.addLocation(expectedLocation);

        expectedDate.addLocation(expectedLocation);
        expectedDate.addTime(expectedTime);

        expectedLocation.addDate(expectedDate);
        expectedLocation.addTime(expectedTime);

        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.of(expectedDate));
        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.of(expectedLocation));
        when(timeRepository.findCommonTime(testDateId, testLocationId)).thenReturn(expectedTime);

        SunriseAndSunsetResponse response = service.deleteSunriseAndSunsetTime(testLocationId, testDateId);

        assertEquals(testLocation, response.getLocation());
        assertEquals(testLat, response.getLatitude());
        assertEquals(testLng, response.getLongitude());
        assertEquals(testDate, response.getDate());

        verify(cache, times(2)).get(anyString());
        verify(timeRepository).findCommonTime(testDateId, testLocationId);
        verify(dateRepository).findById(testDateId);
        verify(locationRepository).findById(testLocationId);
    }

    @Test
    void deleteSunriseAnsSunsetTestNoDate() {

        Integer testDateId = 1, testLocationId = 1;

        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.deleteSunriseAndSunsetTime(testLocationId, testDateId));

        verify(cache).get(anyString());
        verify(dateRepository).findById(testDateId);
    }

    @Test
    void deleteSunriseAnsSunsetTestNoLocation() {

        Integer testDateId = 1, testLocationId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedDate = new Date(testDate);

        when(cache.get("Date" + testDateId)).thenReturn(null);
        when(dateRepository.findById(testDateId)).thenReturn(java.util.Optional.of(expectedDate));
        when(cache.get("Location" + testLocationId)).thenReturn(null);
        when(locationRepository.findById(testLocationId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.deleteSunriseAndSunsetTime(testLocationId, testDateId));

        verify(cache, times(2)).get(anyString());
        verify(dateRepository).findById(testDateId);
        verify(locationRepository).findById(testLocationId);
    }

    @Test
    void findDaytimeLengthTestValid() {

        Integer testDateId = 1, testLocationId = 1;
        long expectedDuration = 13 * 60 * 60 + 32 * 60 + 18;
        DaytimeResponse expectedResponse = new DaytimeResponse(0, LocalTime.of(13, 32, 18));

        when(cache.get("Daytime" + testDateId + testLocationId)).thenReturn(null);
        when(locationRepository.findDaytimeLength(testDateId, testLocationId)).thenReturn(expectedDuration);

        DaytimeResponse result = service.findDaytimeLength(testDateId, testLocationId);

        assertEquals(expectedResponse.getDuration(), result.getDuration());

        verify(cache).get("Daytime" + testDateId + testLocationId);
        verify(locationRepository).findDaytimeLength(testDateId, testLocationId);
    }

    @Test
    void findDaytimeLengthTestInvalid() {

        Integer testDateId = 1, testLocationId = 1;

        when(cache.get("Daytime" + testDateId + testLocationId)).thenReturn(null);
        when(locationRepository.findDaytimeLength(testDateId, testLocationId)).thenReturn(0L);

        assertThrows(NoSuchElementException.class, () -> service.findDaytimeLength(testDateId, testLocationId));

        verify(cache).get("Daytime" + testDateId + testLocationId);
        verify(locationRepository).findDaytimeLength(testDateId, testLocationId);
    }
}
