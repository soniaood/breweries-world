## Breweries World REST API
This project allows an end user to search and retrieve information about breweries. The breweries data is fetched from Open Brewery DB.

### Getting started
1. Clone this repository
2. Make sure you are using `JDK 11` and Maven `3.+`
3. You can build the project and run the tests by running `mvn clean install`
4. Once successfully built, you can run the service using java -jar command:
```
   mvn clean spring-boot:run -f api
```

#### Get Breweries information
To retrieve all available breweries:
```
  curl http://localhost:8080/breweries/ -H "Authorization: Bearer {access_token}"
```

To retrieve brewery information by id:
```
  curl http://localhost:8080/breweries/{id} -H "Authorization: Bearer {access_token}"
```

Note that endpoints are protected by OAuth 2.0. Above calls will return `401 Unauthorized` when authorization is not provided or is incorrect.

To obtain an OAuth 2.0 access token:
```
  curl {client_id}:{client_secret}@localhost:8080/oauth/token -d grant_type=password -d username={username} -d password={password}
```

Additional information to request access token (configured under `application.properties`):
```
client_id: client
client_secret: password
username: user
password: password
access_token_validitity_seconds=43200
grant_types=password,authorization_code,refresh_token
refresh_token_validitity_seconds=2592000
```

### About Breweries World API
The application is organized in the following modules:

#### breweries-api
Springboot application that services HTTP breweries requests. It includes `BreweriesWorldController` and `BreweriesWorldErrorHandlingController` to handle exceptions.

#### breweries-core
Holds Breweries World Service, containing the core logic of the endpoints. At this point there is no logic involved, aside from calling Open Brewery Db external service.

#### breweries-domain
Contains the response models.

#### breweries-external
Module for external communication. Contains the client for Open Brewery Db API. It includes some logic around handling exceptions.

#### breweries-test
Parent module for integration tests. Currently, integration tests are run when building the project.
