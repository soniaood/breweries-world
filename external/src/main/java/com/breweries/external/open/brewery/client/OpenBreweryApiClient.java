package com.breweries.external.open.brewery.client;

import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.config.OpenBreweryConfiguration;

import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import com.breweries.external.open.brewery.exception.OpenBreweryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class OpenBreweryApiClient {

    private static final String OPEN_BREWERY_CLIENT_ERROR_MESSAGE = "Could not retrieve breweries from Open Brewery DB";
    private OpenBreweryConfiguration openBreweryConfiguration;
    private ObjectMapper mapper;
    private RestTemplate restTemplate;

    @Autowired
    public OpenBreweryApiClient(OpenBreweryConfiguration openBreweryConfiguration,
                                ObjectMapper objectMapper,
                                RestTemplate restTemplate) {
        this.openBreweryConfiguration = openBreweryConfiguration;
        this.mapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Calls the Open Brewery DB API get breweries service.
     *
     * @return {@link Set<Brewery>} Collection of Brewery, defined on Breweries World Domain.
     * @throws  OpenBreweryApiClientException when an error is encountered when calling or reading answer from Open Brewery DB.
     */
    public Set<Brewery> getBreweries() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(openBreweryConfiguration.getOpenBreweriesUrl(), String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(Set.class, Brewery.class));
            } else {
                throw new OpenBreweryApiClientException();
            }
        } catch (HttpClientErrorException e) {
            throw new OpenBreweryApiClientException();
        } catch (IOException e) {
            throw new OpenBreweryApiClientException(OPEN_BREWERY_CLIENT_ERROR_MESSAGE, e);
        }
    }


    /**
     * Calls the Open Brewery DB API get brewery by id service.
     *
     * @return {@link Brewery} defined in the Breweries World Domain.
     * @throws  OpenBreweryApiClientException when an error is encountered when calling or reading answer from Open Brewery DB.
     * @throws  OpenBreweryNotFoundException when the brewery with given id is not found on Open Brewery DB.
     */
    public Brewery getBrewery(Integer id) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(String.format(openBreweryConfiguration.getOpenBreweryUrl(), id), String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapper.readValue(response.getBody(), Brewery.class);
            } else {
                throw new OpenBreweryApiClientException();
            }
        } catch (HttpClientErrorException e) {
            HttpStatus status = e.getStatusCode();
            if (NOT_FOUND == status) {
                throw new OpenBreweryNotFoundException();
            } else {
                throw new OpenBreweryApiClientException();
            }
        } catch (IOException e) {
            throw new OpenBreweryApiClientException(OPEN_BREWERY_CLIENT_ERROR_MESSAGE, e);
        }
    }
}
