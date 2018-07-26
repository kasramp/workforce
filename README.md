# Run

## Developer profile
    $ mvn clean compile -Pdeveloper
    
## Package
    $ mvn clean package
    
## Unit test onlu
    $ mvn clean test
    
## Unit and integration tests
    $ mvn clean verify


# Database configuration

By default it uses database. However, it's configured in such a way that by override following environment variables 
can use H2 in memory database.

- `DB_NAME`: override database name, `employee` is default.
- `DB_USERNAME`: username of database. `root` is default.
- `DB_PASSWORD`: password of database. `` is default.
- `DB_PORT`: port of connection to database. `3306` is default
- `JDBC_URL`: datasource url. `jdbc:mysql://localhost:3306/` is default.
- `JDBC_DRIVER`: driver of jdbc connection. Currently supports:
    - `com.mysql.jdbc.Driver` : MySQL jdbc driver [default].
    - `org.h2.Driver`: H2 in memory jdbc driver.
- `HIBERNATE_DDL_AUTO`: schema creation policy, `update` is default.
- `PLATFORM`: sql file that is used to insert some initial data. Currently supports:
     - `h2`: for H2 in memory db.
     - `mysql`: for production [default].
- `SHOW_SQL`: logs Hibernate queries. `true` is default.
- `INIT_DATA`: execute sql file to initialize some data. `always` is default.


# Authentication
Currently the application uses basic auth. If `INIT_DATA` sets to `always` application has two users as follow:
   - `admin`, `password`
   - `user`, `password`
   
All the endpoints related to `employee` resource are secured. 
Hence, need to authenticate first. Authentication can be done via Swagger ui.

# Paths

- `/` redirects to `swagger-ui.html`
- `/apidocs` redirects to `swagger-ui.html`
- `swagger-ui.html` allows interaction with endpoints



# Application design flows

- Entity classes are dual purpose, used as DTOs as well. In real production app, this should be avoided. Separate DTOs needed and can be mapped to Entities using mappers such as Orika.
- Basic auth is not sufficient. It should be replaced with a better authentication solutions (OAuth, JWT). And currently, the password is not encoded.
- Currently, UUID field of Employee table is fully indexed which can slow down the app. Better to only partially index it.
- The delete mechanism is hard delete. This is could be dangerous depends on business logic.
- More robust unit and integration tests are needed.