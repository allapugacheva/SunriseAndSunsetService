package com.example.sunriseandsunsetservice.controller;

import com.example.sunriseandsunsetservice.dto.request.DateRequest;
import com.example.sunriseandsunsetservice.dto.response.DateResponse;
import com.example.sunriseandsunsetservice.service.DateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateControllerTest {

    @Mock
    private DateService dateService;

    @InjectMocks
    private DateController dateController;

    @Test
    void createDateTest() {

        LocalDate testDate = LocalDate.of(2024, 4,8);
        DateResponse expectedResult = new DateResponse(0, testDate);

        when(dateService.createDate(testDate)).thenReturn(expectedResult);

        ResponseEntity<DateResponse> response = dateController.createDate(testDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void createManyDatesTest() {

        List<DateRequest> testDates = List.of(new DateRequest(LocalDate.of(2024, 4,8)),
                new DateRequest(LocalDate.of(2024, 4,9)));
        List<DateResponse> expectedResult = List.of(new DateResponse(0, LocalDate.of(2024, 4,8)),
                new DateResponse(0, LocalDate.of(2024, 4,9)));

        when(dateService.createManyDates(testDates)).thenReturn(expectedResult);

        ResponseEntity<List<DateResponse>> response = dateController.createManyDates(testDates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void readAllDatesTest() {

        List<DateResponse> expectedResult = List.of(new DateResponse(0, LocalDate.of(2024, 4,8)),
                new DateResponse(0, LocalDate.of(2024, 4,9)));

        when(dateService.readAllDates()).thenReturn(expectedResult);

        ResponseEntity<List<DateResponse>> response = dateController.readAllDates();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getByIdTest() {

        Integer testId = 1;
        DateResponse expectedResult = new DateResponse(0, LocalDate.of(2024, 4,8));

        when(dateService.getById(testId)).thenReturn(expectedResult);

        ResponseEntity<DateResponse> response = dateController.getById(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void updateDateTest() {

        Integer testId = 1;
        LocalDate testDate = LocalDate.of(2024, 4,8);
        DateResponse expectedResult = new DateResponse(0, testDate);

        when(dateService.updateDate(testId, testDate)).thenReturn(expectedResult);

        ResponseEntity<DateResponse> response = dateController.updateDate(testId, testDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void deleteDateTest() {

        Integer testId = 1;
        DateResponse expectedResult = new DateResponse(0, LocalDate.of(2024, 4,8));

        when(dateService.deleteDate(testId)).thenReturn(expectedResult);

        ResponseEntity<DateResponse> response = dateController.deleteDate(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }
}
