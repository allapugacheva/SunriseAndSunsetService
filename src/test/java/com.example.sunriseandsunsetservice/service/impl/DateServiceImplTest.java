package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.request.DateRequest;
import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import com.example.sunriseandsunsetservice.model.Date;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.service.CommonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateServiceImplTest {

    @Mock
    private DateRepository dateRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private DateServiceImpl dateService;

    @Test
    void createDateTestNew() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedResult = new Date(testDate);

        when(dateRepository.findBySunDate(testDate)).thenReturn(null);
        when(dateRepository.save(any(Date.class))).thenReturn(expectedResult);

        DateResponse response = dateService.createDate(testDate);

        assertEquals(testDate, response.getDate());

        verify(dateRepository).findBySunDate(testDate);
        verify(dateRepository).save(any(Date.class));
    }

    @Test
    void createDateTestExisting() {

        LocalDate testDate = LocalDate.of(2024, 4, 8);
        Date expectedResult = new Date(testDate);

        when(dateRepository.findBySunDate(testDate)).thenReturn(expectedResult);

        DateResponse response = dateService.createDate(testDate);

        assertEquals(testDate, response.getDate());

        verify(dateRepository).findBySunDate(testDate);
    }

    @Test
    void createManyDatesTest() {

        List<DateRequest> testDates = List.of(new DateRequest(LocalDate.of(2024, 4,8)),
                new DateRequest(LocalDate.of(2024, 4,9)));
        List<DateResponse> expectedResult = List.of(new DateResponse(LocalDate.of(2024, 4,8)),
                new DateResponse(LocalDate.of(2024, 4,9)));

        when(dateRepository.findBySunDate(testDates.get(0).getDate())).thenReturn(new Date(testDates.get(0).getDate()));
        when(dateRepository.findBySunDate(testDates.get(1).getDate())).thenReturn(new Date(testDates.get(1).getDate()));

        List<DateResponse> response = dateService.createManyDates(testDates);

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(DateResponse::getDate).collect(Collectors.toList()),
                response.stream().map(DateResponse::getDate).collect(Collectors.toList()));

        verify(dateRepository).findBySunDate(LocalDate.of(2024,4,8));
        verify(dateRepository).findBySunDate(LocalDate.of(2024,4,9));
    }

    @Test
    void readAllDatesTest() {

        List<DateResponse> expectedResult = List.of(new DateResponse(LocalDate.of(2024, 4,8)),
                new DateResponse(LocalDate.of(2024, 4,9)));

        when(dateRepository.findAllDates()).thenReturn(expectedResult);

        List<DateResponse> response = dateService.readAllDates();

        assertEquals(expectedResult.size(), response.size());
        assertIterableEquals(expectedResult.stream().map(DateResponse::getDate).collect(Collectors.toList()),
                response.stream().map(DateResponse::getDate).collect(Collectors.toList()));

        verify(dateRepository).findAllDates();
    }

    @Test
    void getByIdTestFromCache() {

        Integer testId = 1;
        Date expectedResult = new Date(LocalDate.of(2024, 4, 8));

        when(cache.get("Date" + testId)).thenReturn(expectedResult);

        DateResponse response = dateService.getById(testId);

        assertEquals(expectedResult.getSunDate(), response.getDate());

        verify(cache).get("Date" + testId);
    }

    @Test
    void getByIdTestFromDatabase() {

        Integer testId = 1;
        Date expectedResult = new Date(LocalDate.of(2024, 4, 8));

        when(cache.get("Date" + testId)).thenReturn(null);
        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        DateResponse response = dateService.getById(testId);

        assertEquals(expectedResult.getSunDate(), response.getDate());

        verify(cache).get("Date" + testId);
        verify(dateRepository).findById(testId);
    }

    @Test
    void getByIdTestNotFound() {

        Integer testId = 1;

        when(cache.get("Date" + testId)).thenReturn(null);
        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> dateService.getById(testId));

        verify(cache).get("Date" + testId);
        verify(dateRepository).findById(testId);
    }

    @Test
    void updateDateTest() {

        Integer testId = 1;
        LocalDate testDate = LocalDate.of(2024, 4, 8);

        DateResponse response = dateService.updateDate(testId, testDate);

        assertEquals(testDate, response.getDate());

        verify(commonService).updateDate(testId, testDate);
    }

    @Test
    void deleteDateTestExisting() {

        Integer testId = 1;
        Date expectedResult = new Date(LocalDate.of(2024, 4, 8));

        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.of(expectedResult));

        DateResponse response = dateService.deleteDate(testId);

        assertEquals(expectedResult.getSunDate(), response.getDate());

        verify(dateRepository).findById(testId);
    }

    @Test
    void deleteDateTestNotFound() {

        Integer testId = 1;

        when(dateRepository.findById(testId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> dateService.deleteDate(testId));

        verify(dateRepository).findById(testId);
    }
}
