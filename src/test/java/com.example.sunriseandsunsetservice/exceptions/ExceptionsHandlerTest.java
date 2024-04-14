package com.example.sunriseandsunsetservice.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExceptionsHandlerTest {

    @InjectMocks
    private ExceptionsHandler handler;

    @Test
    void badRequestExceptionTest() {

        IllegalArgumentException exception = new IllegalArgumentException("Test error message");

        ResponseEntity<Object> responseEntity = handler.badRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals("Test error message", errorResponse.getErrorText());
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void notFoundExceptionTest() {

        NoSuchElementException exception = new NoSuchElementException("Test error message");

        ResponseEntity<Object> responseEntity = handler.notFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals("Test error message", errorResponse.getErrorText());
        assertEquals(404, errorResponse.getStatus());
    }

    @Test
    void internalServerErrorExceptionTest() {

        Exception exception = new Exception("Test error message");

        ResponseEntity<Object> responseEntity = handler.internalServerErrorException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals("Test error message", errorResponse.getErrorText());
        assertEquals(500, errorResponse.getStatus());
    }
}
