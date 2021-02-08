package com.breweries.api.testdata;

import com.breweries.domain.model.Brewery;

import java.util.Set;

public class TestBreweriesFactory {

    public static Set<Brewery> aSetOfBreweries() {
        return Set.of(aBrewery());
    }

    public static Brewery aBrewery() {
        return Brewery.builder()
                .id(198)
                .name("State 48 Brewery")
                .breweryType("brewpub")
                .street("13823 W Bell Rd")
                .city("Surprise")
                .state("Arizona")
                .postalCode("85374-3873")
                .country("United States")
                .phone("6235841095")
                .build();
    }
}
