package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.model.Location;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommonServiceTest {

    @Mock
    private DateRepository dateRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RestTemplate restTemplate = new RestTemplate();

    @Mock
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private CommonService service;

    @Test
    public void notValidLatTestInvalid() {

        assertTrue(service.notValidLat(-91.0));
    }

    @Test
    public void notValidLatTestValid() {

        assertFalse(service.notValidLat(89.0));
    }

    @Test
    public void notValidLngInvalid() {

        assertTrue(service.notValidLng(-181.0));
    }

    @Test
    public void notValidLngValid() {

        assertFalse(service.notValidLng(179.0));
    }

    @Test
    public void updateDateFromCache() {

        Integer testId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedResult = new Date(testDate);

        when(cache.get("Date" + testId)).thenReturn(expectedResult);

        service.updateDate(testId, testDate);

        verify(cache).get("Date" + testId);
    }

    @Test
    public void updateDateFromDatabase() {

        Integer testId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedResult = new Date(testDate);

        when(cache.get("Date" + testId)).thenReturn(null);
        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        service.updateDate(testId, testDate);

        verify(cache).get("Date" + testId);
        verify(dateRepository).findById(testId);
    }

    @Test
    public void updateDateFromNotFound() {

        Integer testId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);

        when(cache.get("Date" + testId)).thenReturn(null);
        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.updateDate(testId, testDate));

        verify(cache).get("Date" + testId);
        verify(dateRepository).findById(testId);
    }

    @Test
    public void updateLocationFromCache() {

        Integer testId = 1;
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location("Пинск", testLat, testLng);
        Timezone testTimezone = new Timezone("Europe/Minsk");
        expectedResult.setTimezone(testTimezone);

        when(cache.get("Location" + testId)).thenReturn(expectedResult);
        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(new Location("Пинск", testLat, testLng));

        String response = service.updateLocation(testId, testLat, testLng);

        assertEquals(response, "Пинск");

        verify(cache).get("Location" + testId);
    }

    @Test
    public void updateLocationFromDatabase() {

        Integer testId = 1;
        Double testLat = 52.111385, testLng = 26.102528;
        Location expectedResult = new Location("Пинск", testLat, testLng);
        Timezone testTimezone = new Timezone("Europe/Minsk");
        expectedResult.setTimezone(testTimezone);

        when(cache.get("Location" + testId)).thenReturn(null);
        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));
        when(locationRepository.findByLatitudeAndLongitude(testLat, testLng)).thenReturn(new Location("Пинск", testLat, testLng));

        String response = service.updateLocation(testId, testLat, testLng);

        assertEquals(response, "Пинск");

        verify(cache).get("Location" + testId);
        verify(locationRepository).findById(testId);
    }

    @Test
    public void updateLocationNotFound() {

        Integer testId = 1;
        Double testLat = 52.111385, testLng = 26.102528;

        when(cache.get("Location" + testId)).thenReturn(null);
        when(locationRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.updateLocation(testId, testLat, testLng));

        verify(cache).get("Location" + testId);
        verify(locationRepository).findById(testId);
    }
}
