package com.breweries.external.open.brewery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OpenBreweryNotFoundException extends RuntimeException {

    private static final String OPEN_BREWERY_NOT_FOUND_MESSAGE = "Brewery not found on Open Brewery DB";

    public OpenBreweryNotFoundException() {
        super(OPEN_BREWERY_NOT_FOUND_MESSAGE);
    }

}
