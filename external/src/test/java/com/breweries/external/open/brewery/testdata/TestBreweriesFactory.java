package com.breweries.external.open.brewery.testdata;

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

    public static String aBreweriesResponse() {
        return "[\n" + aBreweryResponse() +
                "]";
    }

    public static String aBreweryResponse() {
        return "{\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Avondale Brewing Co\",\n" +
                "        \"brewery_type\": \"micro\",\n" +
                "        \"street\": \"201 41st St S\",\n" +
                "        \"address_2\": null,\n" +
                "        \"address_3\": null,\n" +
                "        \"city\": \"Birmingham\",\n" +
                "        \"state\": \"Alabama\",\n" +
                "        \"county_province\": null,\n" +
                "        \"postal_code\": \"35222-1932\",\n" +
                "        \"country\": \"United States\",\n" +
                "        \"longitude\": \"-86.774322\",\n" +
                "        \"latitude\": \"33.524521\",\n" +
                "        \"phone\": \"2057775456\",\n" +
                "        \"website_url\": \"http://www.avondalebrewing.com\",\n" +
                "        \"updated_at\": \"2018-08-23T23:19:57.825Z\",\n" +
                "        \"created_at\": \"2018-07-24T01:32:47.255Z\"\n" +
                "}";
    }
}
