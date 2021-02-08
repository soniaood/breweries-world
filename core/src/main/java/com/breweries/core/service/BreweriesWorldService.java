package com.breweries.core.service;

import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.service.OpenBreweryApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BreweriesWorldService {


    private final OpenBreweryApiService openBreweryService;

    @Autowired
    public BreweriesWorldService(OpenBreweryApiService openBreweryService) {
        this.openBreweryService = openBreweryService;
    }

    /**
     * Calls external Open Brewery DB API service to get breweries.
     *
     * @return {@link Set<Brewery>} Collection of Brewery defined in the Breweries World domain.
     */
    public Set<Brewery> getBreweries() {
        return openBreweryService.getBreweries();
    }

    /**
     * Calls external Open Brewery DB API service to get brewery by id.
     *
     * @return {@link Brewery} Brewery model defined in the Breweries World domain.
     */
    public Brewery getBrewery(Integer id) {
        return openBreweryService.getBrewery(id);
    }
}
