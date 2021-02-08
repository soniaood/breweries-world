package com.breweries.api.controller;

import com.breweries.core.service.BreweriesWorldService;
import com.breweries.domain.model.Brewery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class BreweriesWorldController {

    private final BreweriesWorldService breweriesWorldService;

    @Autowired
    public BreweriesWorldController(BreweriesWorldService breweriesWorldService) {
        this.breweriesWorldService = breweriesWorldService;
    }

    /**
     * Retrieves a collection of breweries.
     *
     * @return {@link Set<Brewery>} representing a set of breweries available from Open Breweries DB.
     */
    @GetMapping(value = "${breweries.world.get.path}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Set<Brewery> getBreweries() {
        return this.breweriesWorldService.getBreweries();
    }

    /**
     * Retrieves a brewery by id.
     *
     * @return {@link Brewery} representing the requested brewery available from Open Breweries DB.
     */
    @GetMapping(value = "${breweries.world.get.id.path}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Brewery getBrewery(@PathVariable Integer breweryId) {
        return this.breweriesWorldService.getBrewery(breweryId);
    }
}