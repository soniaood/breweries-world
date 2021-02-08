package com.breweries.external.open.brewery.service;

import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.client.OpenBreweryApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OpenBreweryApiService {

    private OpenBreweryApiClient client;

    @Autowired
    public OpenBreweryApiService(OpenBreweryApiClient client) {
        this.client = client;
    }

    /**
     * Calls the Open Brewery DB API client to get breweries.
     *
     * @return {@link Set<Brewery>} Collection of Brewery defined in the Breweries World domain.
     */
    public Set<Brewery> getBreweries() {
        return client.getBreweries();
    }

    /**
     * Calls the Open Brewery DB API client to get brewery by id service.
     *
     * @return {@link Brewery} defined in the Breweries World domain.
     */
    public Brewery getBrewery(Integer id) { return client.getBrewery(id); }
}
