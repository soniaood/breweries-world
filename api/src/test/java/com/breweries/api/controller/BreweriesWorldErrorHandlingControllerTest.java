package com.breweries.api.controller;

import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import com.breweries.external.open.brewery.exception.OpenBreweryNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BreweriesWorldErrorHandlingControllerTest {

    private BreweriesWorldErrorHandlingController errorHandlingController;

    @Before
    public void setup() {
        errorHandlingController = new BreweriesWorldErrorHandlingController();
    }

    @Test
    public void shouldHandleOpenBreweryNotFoundException() {
        // Given
        OpenBreweryNotFoundException openBreweryNotFoundException = new OpenBreweryNotFoundException();

        // When
        ResponseEntity<Object> responseEntity = errorHandlingController.handle(openBreweryNotFoundException);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHandleOpenBreweryApiException() {
        // Given
        OpenBreweryApiClientException openBreweryApiException = new OpenBreweryApiClientException();

        // When
        ResponseEntity<Object> responseEntity = errorHandlingController.handle(openBreweryApiException);

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHandleBreweriesWorldException() {
        // Given
        RuntimeException runtimeException = new RuntimeException();

        // When
        ResponseEntity<Object> responseEntity = errorHandlingController.handle(runtimeException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}