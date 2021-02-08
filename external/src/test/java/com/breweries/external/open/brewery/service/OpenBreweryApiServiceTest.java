package com.breweries.external.open.brewery.service;


import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.client.OpenBreweryApiClient;
import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aBrewery;
import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aSetOfBreweries;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenBreweryApiServiceTest {

    OpenBreweryApiService openBreweryApiService;

    @Mock
    private OpenBreweryApiClient mockOpenBreweryApiClient;

    @Before
    public void setUp() {
        openBreweryApiService = new OpenBreweryApiService(mockOpenBreweryApiClient);
    }

    @Test
    public void shouldGetBreweries() {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        given(mockOpenBreweryApiClient.getBreweries()).willReturn(breweries);

        // When
        Set<Brewery> actualBreweries = openBreweryApiService.getBreweries();

        // Then
        verify(mockOpenBreweryApiClient).getBreweries();
        assertEquals(breweries, actualBreweries);
    }

    @Test
    public void shouldGetBreweryById() {
        // Given
        int id = 198;
        Brewery brewery = aBrewery();
        given(mockOpenBreweryApiClient.getBrewery(id)).willReturn(brewery);

        // When
        Brewery actualBrewery = openBreweryApiService.getBrewery(id);

        // Then
        verify(mockOpenBreweryApiClient).getBrewery(id);
        assertEquals(brewery, actualBrewery);
    }

    @Test
    public void shouldFailGivenOpenBreweryApiClientThrowsException() {
        // Given
        int id = 198;
        given(openBreweryApiService.getBrewery(id)).willThrow(new OpenBreweryApiClientException());

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> openBreweryApiService.getBrewery(id));
        verify(mockOpenBreweryApiClient).getBrewery(id);
    }
}