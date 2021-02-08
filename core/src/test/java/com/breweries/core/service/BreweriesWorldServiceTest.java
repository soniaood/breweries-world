package com.breweries.core.service;


import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import com.breweries.external.open.brewery.service.OpenBreweryApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static com.breweries.core.testdata.TestBreweriesFactory.aBrewery;
import static com.breweries.core.testdata.TestBreweriesFactory.aSetOfBreweries;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BreweriesWorldServiceTest {

    BreweriesWorldService breweriesWorldService;

    @Mock
    private OpenBreweryApiService mockOpenBreweryApiService;

    @Before
    public void setUp() {
        breweriesWorldService = new BreweriesWorldService(mockOpenBreweryApiService);
    }

    @Test
    public void shouldGetBreweries() {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        given(mockOpenBreweryApiService.getBreweries()).willReturn(breweries);

        // When
        Set<Brewery> actualBreweries = breweriesWorldService.getBreweries();

        // Then
        verify(mockOpenBreweryApiService).getBreweries();
        assertEquals(breweries, actualBreweries);
    }

    @Test
    public void shouldGetBreweryById() {
        // Given
        int id = 198;
        Brewery brewery = aBrewery();
        given(mockOpenBreweryApiService.getBrewery(id)).willReturn(brewery);

        // When
        Brewery actualBrewery = breweriesWorldService.getBrewery(id);

        // Then
        verify(mockOpenBreweryApiService).getBrewery(id);
        assertEquals(brewery, actualBrewery);
    }

    @Test
    public void shouldFailGivenOpenBreweryApiServiceThrowsException() {
        // Given
        int id = 198;
        Brewery brewery = aBrewery();
        given(mockOpenBreweryApiService.getBrewery(id)).willThrow(new OpenBreweryApiClientException());

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            breweriesWorldService.getBrewery(id);
        });
        verify(mockOpenBreweryApiService).getBrewery(id);
    }
}