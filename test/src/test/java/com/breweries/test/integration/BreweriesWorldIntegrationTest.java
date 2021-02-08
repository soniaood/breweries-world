package com.breweries.test.integration;

import com.breweries.api.config.BreweriesWorldApplication;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebAppConfiguration
@SpringBootTest(classes = BreweriesWorldApplication.class)
public class BreweriesWorldIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldReturnAccessTokenGivenValidOAuthTokenRequestAndCredentials() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "user");
        params.add("password", "password");

        mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client","password"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.access_token", Matchers.notNullValue()));
    }

    @Test
    public void shouldReturnUnauthorizedGivenOAuthTokenRequestWithBadCredentials() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "wrongUser");
        params.add("password", "wrongPassword");

        mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client","password"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.error_description", Matchers.is("Bad credentials")));
    }

    @Test
    public void shouldReturnUnauthorizedGivenGetBreweriesRequestButNoAuthorization() throws Exception {
        mockMvc.perform(get("/breweries"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error_description", Matchers.is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnauthorizedGivenGetBreweriesRequestWithInvalidTokenAuthorization() throws Exception {
        String invalidAccessToken = "e90a03db-0683-4a04-961a-809cdfe47dfa";
        mockMvc.perform(get("/breweries")
                .header("Authorization", "Bearer " + invalidAccessToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error_description", Matchers.is("Invalid access token: e90a03db-0683-4a04-961a-809cdfe47dfa")));
    }

    @Test
    public void shouldReturnBreweriesGivenGetBreweriesRequestWithValidAuthorization() throws Exception {
        String accessToken = obtainAccessToken("user", "password");

        String breweryString = "{\"email\":\"jim@yahoo.com\",\"name\":\"Jim\"}";

        mockMvc.perform(get("/breweries")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void shouldReturnBreweryByIdGivenGetBreweriesRequestWithValidAuthorizationAndBreweryId() throws Exception {
        String accessToken = obtainAccessToken("user", "password");

        mockMvc.perform(get("/breweries/1")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("5 Rivers Brewing LLC")));
    }

    @Test
    public void shouldReturnNotFoundGivenGetBreweriesRequestWithValidAuthorizationAndNonExistingBreweryId() throws Exception {
        String accessToken = obtainAccessToken("user", "password");

        mockMvc.perform(get("/breweries/9999")
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.error_message", Matchers.is("Brewery not found on Open Brewery DB")));
    }


    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client","password"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
