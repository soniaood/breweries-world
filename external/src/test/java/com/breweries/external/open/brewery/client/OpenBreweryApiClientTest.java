package com.breweries.external.open.brewery.client;

import com.breweries.domain.model.Brewery;
import com.breweries.external.open.brewery.config.OpenBreweryConfiguration;
import com.breweries.external.open.brewery.exception.OpenBreweryApiClientException;
import com.breweries.external.open.brewery.exception.OpenBreweryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;;
import java.util.Set;

import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aBreweriesResponse;
import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aBrewery;
import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aBreweryResponse;
import static com.breweries.external.open.brewery.testdata.TestBreweriesFactory.aSetOfBreweries;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class OpenBreweryApiClientTest {

    private OpenBreweryApiClient openBreweryApiClient;

    @Mock
    private OpenBreweryConfiguration mockOpenBreweryConfiguration;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        openBreweryApiClient = new OpenBreweryApiClient(mockOpenBreweryConfiguration, objectMapper, restTemplate);
    }

    @Test
    public void shouldGetOpenBreweryDbBreweries() throws IOException {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        String url = "https://api.openbrewerydb.org/breweries/";
        given(mockOpenBreweryConfiguration.getOpenBreweriesUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity(aBreweriesResponse(), HttpStatus.OK);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        given(objectMapper.getTypeFactory()).willReturn(TypeFactory.defaultInstance());
        given(objectMapper.readValue(anyString(), any(JavaType.class))).willReturn(breweries);

        // When
        Set<Brewery> actualBreweries = openBreweryApiClient.getBreweries();

        // Then
        assertEquals(breweries, actualBreweries);
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenJsonMappingExceptionWhenParsingBreweriesResponse() throws IOException {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        String url = "https://api.openbrewerydb.org/breweries/";
        given(mockOpenBreweryConfiguration.getOpenBreweriesUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity(aBreweriesResponse(), HttpStatus.OK);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        given(objectMapper.getTypeFactory()).willReturn(TypeFactory.defaultInstance());
        given(objectMapper.readValue(anyString(), any(JavaType.class)))
                .willThrow(JsonMappingException.class);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBreweries();
        });
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenOpenBreweryDbNonCompliantResponseWhenGettingBreweries() {
        // Given
        Set<Brewery> breweries = aSetOfBreweries();
        String url = "https://api.openbrewerydb.org/breweries/";
        given(mockOpenBreweryConfiguration.getOpenBreweriesUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity("{\n\"message\": \"Internal Server Error\"}", HttpStatus.SERVICE_UNAVAILABLE);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBreweries();
        });
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenOpenBreweryDbThrowsExceptionWhenGettingBreweries() {
        // Given
        String url = "https://api.openbrewerydb.org/breweries/";
        given(mockOpenBreweryConfiguration.getOpenBreweriesUrl()).willReturn(url);

        given(restTemplate.getForEntity(url, String.class)).willThrow(HttpClientErrorException.class);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBreweries();
        });
    }

    @Test
    public void shouldGetOpenBreweryDbBreweryById() throws IOException {
        // Given
        Brewery brewery = aBrewery();
        String url = "https://api.openbrewerydb.org/breweries/198";
        given(mockOpenBreweryConfiguration.getOpenBreweryUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity(aBreweryResponse(), HttpStatus.OK);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        given(objectMapper.readValue(anyString(), any(Class.class))).willReturn(brewery);

        // When
        Brewery actualBrewery = openBreweryApiClient.getBrewery(198);

        // Then
        assertEquals(brewery, actualBrewery);
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenJsonProcessingExceptionWhenParsingBreweryResponse() throws IOException {
        // Given
        Brewery brewery = aBrewery();
        String url = "https://api.openbrewerydb.org/breweries/198";
        given(mockOpenBreweryConfiguration.getOpenBreweryUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity(aBreweryResponse(), HttpStatus.OK);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        given(objectMapper.readValue(anyString(), any(Class.class)))
                .willThrow(JsonProcessingException.class);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBrewery(198);
        });
    }

    @Test
    public void shouldThrowOpenBreweryNotFoundExceptionGivenNoBreweryIsFoundOnOpenBreweryDb() {
        // Given
        Brewery brewery = aBrewery();
        String url = "https://api.openbrewerydb.org/breweries/999999";
        given(mockOpenBreweryConfiguration.getOpenBreweryUrl()).willReturn(url);

        given(restTemplate.getForEntity(url, String.class)).willThrow( new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Then
        assertThrows(OpenBreweryNotFoundException.class, () -> {
            openBreweryApiClient.getBrewery(999999);
        });
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenOpenBreweryDbNonCompliantResponseWhenGettingBrewery() {
        // Given
        String url = "https://api.openbrewerydb.org/breweries/1";
        given(mockOpenBreweryConfiguration.getOpenBreweryUrl()).willReturn(url);

        ResponseEntity<String> response = new ResponseEntity("{\n\"message\": \"Internal Server Error\"}", HttpStatus.SERVICE_UNAVAILABLE);
        given(restTemplate.getForEntity(url, String.class)).willReturn(response);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBrewery(1);
        });
    }

    @Test
    public void shouldThrowOpenBreweryApiExceptionGivenOpenBreweryDbInternalErrorWhenGettingBrewery() {
        // Given
        Brewery brewery = aBrewery();
        String url = "https://api.openbrewerydb.org/breweries/999999";
        given(mockOpenBreweryConfiguration.getOpenBreweryUrl()).willReturn(url);

        given(restTemplate.getForEntity(url, String.class)).willThrow(HttpClientErrorException.class);

        // Then
        assertThrows(OpenBreweryApiClientException.class, () -> {
            openBreweryApiClient.getBrewery(999999);
        });
    }
}
