### Solar Watch API
Solar Watch is an API that allows users to retrieve sunrise and sunset times for specific cities on given dates. Additionally, admin users can manage city information within the system.

## Technologies Used
- Spring Boot
- Spring Data
- Spring Security
- PostgreSQL
- Docker
## Overview
The project leverages Spring Boot along with Spring Data and Spring Security for seamless API development. PostgreSQL is utilized for storing user data, sunrise/sunset times, and city information. Docker is employed for containerizing the project, simplifying deployment and scalability.

## Challenges
Containerizing the project and configuring Spring Security presented notable challenges. Despite complexities, all desired features were successfully implemented.

## Setup Instructions
Manual Setup:
- Clone the repository.

- Add the following environment variables:
  - `SPRING_JPA_HIBERNATE_DDL_AUTO`: [update, create-drop]
  - `SPRING_DATASOURCE_URL`: [Your database URL]
  - `SPRING_DATASOURCE_USERNAME`: [Your database username]
  - `SPRING_DATASOURCE_PASSWORD`: [Your database password]
  - `JWT_SECRET`: [Your JWT key]
  - `JWT_EXPIRATION`: [JWT token expiration time in millisecond]

- To build project run `mvn clean package` command.
- To run project run `java -jar target/SolarWatchApplication-0.0.1-SNAPSHOT.jar`


Docker Setup:
- Clone the repository.

- Add the following environment variables:
  - `DB_NAME`: [Your database name]
  - `DB_USER`: [Your database username]
  - `DB_PASSWORD`: [Your database password]
  - `DDL_AUTO`: [update, create-drop]
  - `JWT_EXPIRATION`: [JWT token expiration time in millisecond]
  - `JWT_SECRET`: [Your JWT key]

- Run `docker-compose up` command.

## API Endpoints

| Functionality | Endpoint | Method | Authorization | Request body | Response | Exceptions |
|---------------|----------|--------|---------------|--------------|----------|------------|
| Registration | `/api/user/registration` | `POST` | - | {"password": <_PASSWORD_>, "userName": <USER_NAME>} | Response status 201 (Created) | DataIntegrityViolationException |
| Sign-in | `/api/user/signin` | `POST` | - | {"password": <_PASSWORD_>, "userName": <USER_NAME>} | {"jwt":<JWT_TOKEN>, "userName": <USER_NAME>,"roles": [<USER_ROLES>]} | BadCredentialsException |
| Add Admin Role | `/api/user/role/{id}`| `PATCH` | Bearer <JWT_token> | - | {"password": <_PASSWORD_>, "userName": <USER_NAME>} | {"jwt":<JWT_TOKEN>, "userName": <USER_NAME>,"roles": [<USER_ROLES>]} | NonExistingUserException |
| Create City | `/api/city` | `POST` | Bearer <JWT_token> ADMIN role required | {"name": <CITY_NAME>, "lat": <CITY_LATTITUDE>,"lon": <CITY_LONGITUDE>, "state": <STATE_NAME>, "country": <COUNTRY_NAME>} | Response status 200 (OK) | AlreadyExistingCityException |
| Update City | `/api/city/{id}` | `PUT` | Bearer <JWT_token> ADMIN role required | {"name": <CITY_NAME>, "lat": <CITY_LATTITUDE>,"lon": <CITY_LONGITUDE>, "state": <STATE_NAME>, "country": <COUNTRY_NAME>} | Response status 200 (OK) | NonExistingCityException |
| Delete City | `/api/city/{id}` | `DELETE` | Bearer <JWT_token> ADMIN role required | - | Response status 200 (OK) | NonExistingCityException |
| Get Sunrise | `/api/sunrise?city=<CITY_NAME>&date=<SUNRISE_DATE>` | `GET` | Bearer <JWT_token> | - | {"city": <CITY_NAME>, "sunrise": <SUNRISE_TIME>,"date": <SUNRISE_DATE>} | InvalidCityParameterException, InvalidCityParameterException, NonExistingSunriseException |
| Create Sunrise | `/api/sunrise` | `POST` | Bearer <JWT_token> ADMIN role required | {"date": <SUNRISE_DATE>,"sunrise": <SUNRISE_TIME>,"cityId": <CITY_ID>} | Response status 201 (Created) | NonExistingCityException |
| Update Sunrise | `/api/sunrise/{id}` | `PUT` | Bearer <JWT_token> ADMIN role required | {"date": <SUNRISE_DATE>, "sunrise": <SUNRISE_TIME>, "cityId": <CITY_ID>}| Response status 200 (OK) | NonExistingSunriseException, NonExistingCityException |
| Delete Sunrise | `/api/sunrise/{id}` | `DELETE` | Bearer <JWT_token> ADMIN role required | - | Response status 200 (OK) | - |
| Get Sunset | `/api/sunset?city=<CITY_NAME>&date=<SUNSET_DATE>` | `GET` | Bearer <JWT_token> | - | {"city": <CITY_NAME>,"sunset": <SUNSET_TIME>,"date": <SUNSET_DATE>} | InvalidCityParameterException, NonExistingSunsetException |
| Create Sunset | `/api/sunset` | `POST` | Bearer <JWT_token> ADMIN role required | {"date": <SUNSET_DATE>,"sunset": <SUNSET_TIME>,"cityId": <CITY_ID>}| Response status 201 (Created) | NonExistingSunsetException, NonExistingCityException |
| Update Sunset| `/api/sunset/{id}` | `PUT` | Bearer <JWT_token> ADMIN role required | {"date": <SUNSET_DATE>,"sunset": <SUNSET_TIME>,"cityId": <CITY_ID>,"id": <SUNSET_ID>} | Response status 200 (OK) | NonExistingSunsetException, NonExistingCityException |
| Delete Sunset | `/api/sunset/{id}` | `DELETE` | Bearer <JWT_token> ADMIN role required | - | Response status 200 (OK) | - |
