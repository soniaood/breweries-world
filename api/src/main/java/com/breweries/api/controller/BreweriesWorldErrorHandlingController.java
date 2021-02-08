package com.breweries.api.controller;

import com.breweries.api.exception.BreweriesWorldErrorResponse;
import com.breweries.external.open.brewery.exception.OpenBreweryNotFoundException;
import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BreweriesWorldErrorHandlingController {

    @ExceptionHandler(OpenBreweryNotFoundException.class)
    public ResponseEntity<Object> handle(OpenBreweryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BreweriesWorldErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(OpenBreweryApiClientException.class)
    public ResponseEntity<Object> handle(OpenBreweryApiClientException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new BreweriesWorldErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BreweriesWorldErrorResponse(e.getMessage()));
    }
}