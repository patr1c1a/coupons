# Install and setup project

* Clone project.

* Rename `src/main/resources/application.properties.template` to `src/main/resources/application.properties`. Make sure an existing MercadoLibre app id and secret are available and add them to this file, replacing "myClientId" and "myClientSecret"

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


# Endpoint description


`/coupon` (**POST**)

Given a list of favorite item IDs (`itemIds`) and a coupon amount (`couponAmount`), returns a list of MercadoLibre active items that can be bought with the coupon amount, maximizing the number of items.

Request body:

```
{
    "itemIds": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
    "couponAmount": 500
}
```
(where "MLA#" are actual item ids)

Returns:

```
{
    "itemIds": ["MLA1", "MLA2", "MLA4", "MLA5"],
    "totalExpenditure": 480.0
}
```


`/top-favorited` (**GET**)

Returns the top 5 favorited items.

Returns:

```
{
    "MLA1": 15,
    "MLA4": 9,
    "MLA3": 7,
    "MLA2": 6,
    "MLA5": 3
}
```

`top-favorited?top=N` (**GET**)

Returns the top N favorited items (N is an optional parameter). E.g.: `top-favorited?top=2`.

Returns:

```
{
    "MLA1": 15,
    "MLA4": 9
}
```


# Project structure

`src\main\resources`: configuration files (properties)

`src\main\java\com\challenge\coupons`: source files

- `controller`: endpoint implementation.

- `service`: handles logic for controller.

- `model`: api entities.

- `dto`: third party api entities.

- `config`: contains Rest Template configuration file

`src\test`: test files


# Improvements

Some important details that weren't implemented due to time constraints, but should be taken into consideration:

- Persistence: the `/top-favorited` endpoint relies on in-memory storage. Depending on the production requirements, database persistence would probably be better suited for this. The choice would heavily depend on other details but for a simple implementation something like H2 (with disk persistence) might be an option to consider.

- Caching: in order to handle multiple requests that might include the same item ids, caching would improve performance.

- Logging.

- Documentation: improve documentation by using a tool that provides better formatting and functionality (like Swagger).
