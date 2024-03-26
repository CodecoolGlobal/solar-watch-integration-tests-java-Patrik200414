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
Clone the repository.

Add the following environment variables:
`SPRING_JPA_HIBERNATE_DDL_AUTO`: [update, create-drop]
`SPRING_DATASOURCE_URL`: [Your database URL]
`SPRING_DATASOURCE_USERNAME`: [Your database username]
`SPRING_DATASOURCE_PASSWORD`: [Your database password]
`JWT_SECRET`: [Your JWT key]
`JWT_EXPIRATION`: [JWT token expiration time in millisecond]

To build project run `mvn clean package` command.
To run project run `java -jar target/SolarWatchApplication-0.0.1-SNAPSHOT.jar`


Docker Setup:
Clone the repository.

Add the following environment variables:
`DB_NAME`: [Your database name]
`DB_USER`: [Your database username]
`DB_PASSWORD`: [Your database password]
`DDL_AUTO`: [update, create-drop]
`JWT_EXPIRATION`: [JWT token expiration time in millisecond]
`JWT_SECRET`: [Your JWT key]

Run `docker-compose up` command.

## API Endpoints
Registration:

- Endpoint: `http://localhost:8080/api/registration`
- Request Body: {
                  "password": "password1",
                  "userName": "User1"
                }

Sign-in:

- Endpoint: `http://localhost:8080/api/signin`
- Request Body: {
                  "password": "password1",
                  "userName": "User1"
                  }
- Response: JWT token for further requests.

Get Sunrise Information:

- Endpoint: `http://localhost:8080/api/sunrise?city=Los%20Angeles`
- Authorization Header: Bearer {JWT token}
- Response: Sunrise time for the specified city on the given date.
