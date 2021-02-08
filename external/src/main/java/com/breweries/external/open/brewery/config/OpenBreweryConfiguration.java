package com.breweries.external.open.brewery.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
public class OpenBreweryConfiguration {

    private static final String OPEN_BREWERIES_GET_BREWERIES_PATH = "open.breweries.get-breweries-url";
    private static final String OPEN_BREWERIES_GET_BREWERIES_DEFAULT = "https://api.openbrewerydb.org/breweries/";
    private static final String OPEN_BREWERIES_GET_BREWERY_PATH = "open.breweries.get-brewery-url";
    private static final String OPEN_BREWERIES_GET_BREWERY_DEFAULT = OPEN_BREWERIES_GET_BREWERIES_DEFAULT + "/%s";
    private static final String OPEN_BREWERIES_GET_TIMEOUT_PATH = "open.breweries.http-client-timeout-milliseconds";
    private static final String OPEN_BREWERIES_GET_TIMEOUT_DEFAULT = "1000";
    private Environment environment;

    @Autowired
    public OpenBreweryConfiguration(Environment environment) {
        this.environment = environment;
    }

    public String getOpenBreweriesUrl() {
        return environment.getProperty(OPEN_BREWERIES_GET_BREWERIES_PATH, OPEN_BREWERIES_GET_BREWERIES_DEFAULT);
    }

    public String getOpenBreweryUrl() {
        return environment.getProperty(OPEN_BREWERIES_GET_BREWERY_PATH, OPEN_BREWERIES_GET_BREWERY_DEFAULT);
    }

    private String getHttpClientTimeoutMilliseconds() {
        return environment.getProperty(OPEN_BREWERIES_GET_TIMEOUT_PATH, OPEN_BREWERIES_GET_TIMEOUT_DEFAULT);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.setConnectTimeout(Duration.ofMillis(Long.parseLong(getHttpClientTimeoutMilliseconds())));
        return builder.build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
