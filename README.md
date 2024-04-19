<div align="center" name="readme-top">
  <h3 align="center">Solar Watch API</h3>
</div>


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#run-project">Run Project</a></li>
      </ul>
    </li>
    <li><a href="#api-endpoints">API Endpoints</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



## About The Project

Solar Watch is an API that allows users to retrieve sunrise and sunset times for specific cities on given dates. Additionally, admin users can manage city information within the system.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

This section should list any major frameworks/libraries used to bootstrap your project. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.

* [![Spring Boot][Spring Boot]][Spring-boot-url]
* [![Java][Java]][Java-url]
* [![PostgreSQL][PostgreSQL]][PostgreSQL-url]
* [![Docker][Docker]][Docker-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Getting Started

Clone the repository to your machine: https://github.com/CodecoolGlobal/solar-watch-integration-tests-java-Patrik200414

I assume you have Maven and JRE with PostgreSQL or Docker downloaded to your machine.
If not here are some links where you can download from :
- Maven: https://maven.apache.org/download.cgi
- JRE: https://www.java.com/en/download/manual.jsp
- PostgreSQL: https://www.postgresql.org/download/
- Docker: https://www.docker.com/products/docker-desktop/

### Prerequisites

For manual setup add the following environment variables:
  - `SPRING_JPA_HIBERNATE_DDL_AUTO`: [update, create-drop]
  - `SPRING_DATASOURCE_URL`: [Your database URL]
  - `SPRING_DATASOURCE_USERNAME`: [Your database username]
  - `SPRING_DATASOURCE_PASSWORD`: [Your database password]
  - `JWT_SECRET`: [Your JWT key]
  - `JWT_EXPIRATION`: [JWT token expiration time in millisecond]

For Docker setup add the following environment variables:
  - `DB_NAME`: [Your database name]
  - `DB_USER`: [Your database username]
  - `DB_PASSWORD`: [Your database password]
  - `DDL_AUTO`: [update, create-drop]
  - `JWT_EXPIRATION`: [JWT token expiration time in millisecond]
  - `JWT_SECRET`: [Your JWT key]


### Run Project

- Manual setup:
  1. Run `mvn clean package` command
  2. To run project execute `java -jar target/SolarWatchApplication-0.0.1-SNAPSHOT.jar` command

- Docker setup:
  1. Run `docker-compose up` command

<p align="right">(<a href="#readme-top">back to top</a>)</p>



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



### Contact
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Email][email-shield]][email-address]

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/patrik-martin2004/
[email-shield]: https://img.shields.io/badge/Email-address?style=for-the-badge&logo=gmail&color=555
[email-address]: martinbpatrik@gmail.com
[Spring Boot]: https://img.shields.io/badge/Spring-Boot?style=flat&logo=spring&logoColor=%23FFFFFF
[Spring-boot-url]: https://spring.io/
[Java]: https://img.shields.io/badge/Java-21?logo=openjdk&color=%23FF0000
[Java-url]: https://www.java.com/en/
[PostgreSQL]: https://img.shields.io/badge/Postgres-SQL?style=flat&logo=postgresql&logoColor=%23FFFFFF&color=%23008bb9
[PostgreSQL-url]: https://www.postgresql.org/docs/
[Docker]: https://img.shields.io/badge/Docker-a?style=flat&logo=docker&logoColor=%23FFFFFF&color=%23384d54
[Docker-url]: https://docs.docker.com/
