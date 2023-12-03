# Install and setup project

* Clone project.

* Rename `src/main/resources/application.properties.template` and make it `src/main/resources/application.properties`. Make sure an existing MercadoLibre app id and secret are available and add them to this file, replacing "myClientId" and "myClientSecret"

```
mercadolibre.clientId=myClientId
mercadolibre.clientSecret=myClientSecret
```

* In a terminal, navigate to the project directory.

* Build the project with the Maven Wrapper (this way you won't need Maven globally installed):
`./mvnw clean install`

* Run the application:

`./mvnw spring-boot:run`

(allow access if any firewall prompts come up)


# Run tests

To execute all tests just run:

`./mvnw test`

If a specific test file needs to be executed (replace *filename* with the actual file name, except the extension):

`./mvnw test -Dtest=fileName`

If a specific test needs to be executed (replace *filename* with the actual file name and *testName* with the name of the method containing the test):

`./mvnw test -Dtest=fileName#testName`


# Use the application

Make a POST request to the /coupon endpoint. If the application is being run locally, the URL is probably http://localhost:8080/coupon. The content type should be application/json.

Request body:
```
{
    "itemIds": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
    "couponAmount": 500
}
```

(where "MLA#" are actual item ids)

