package com.breweries.external.open.brewery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class OpenBreweryApiClientException extends RuntimeException {

    private static final String OPEN_BREWERY_ERROR_MESSAGE = "Open Brewery DB API returned unexpected error";

    public OpenBreweryApiClientException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public OpenBreweryApiClientException() {
        super(OPEN_BREWERY_ERROR_MESSAGE);
    }

}
