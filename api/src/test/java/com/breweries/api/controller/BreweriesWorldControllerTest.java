package com.breweries.api.controller;


import com.breweries.core.service.BreweriesWorldService;
import com.breweries.domain.model.Brewery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static com.breweries.api.testdata.TestBreweriesFactory.aBrewery;
import static com.breweries.api.testdata.TestBreweriesFactory.aSetOfBreweries;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BreweriesWorldControllerTest {

    private BreweriesWorldController breweriesWorldController;

    @Mock
    private BreweriesWorldService mockBreweriesWorldService;

    @Before
    public void setUp(){
        breweriesWorldController = new BreweriesWorldController(mockBreweriesWorldService);
    }

    @Test
    public void shouldGetBreweries() {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        given(mockBreweriesWorldService.getBreweries()).willReturn(breweries);

        // When
        Set<Brewery> actualBreweries = breweriesWorldController.getBreweries();

        // Then
        verify(mockBreweriesWorldService).getBreweries();
        assertEquals(breweries, actualBreweries);
    }

    @Test
    public void shouldGetBreweryById() {
        // Given
        int id = 198;
        Brewery brewery = aBrewery();
        given(mockBreweriesWorldService.getBrewery(id)).willReturn(brewery);

        // When
        Brewery actualBrewery = breweriesWorldController.getBrewery(id);

        // Then
        verify(mockBreweriesWorldService).getBrewery(id);
        assertEquals(brewery, actualBrewery);
    }
}