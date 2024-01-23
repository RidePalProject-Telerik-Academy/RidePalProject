# RidePal Playlist Generator

## Project Description
Ride Pal is a web application that enables users to fire up a fast and efficient algorithm
that generates unique playlists.
<br>
Working alongside Microsoft Bing API the software calculates the exact duration of your journey.
<br>
With the help of Deezer API we ensure you that all your favorite tracks are only a click away !

## Swagger
     API documentation available at:
- http://localhost:8080/swagger-ui/index.html

## Installation Guide

- **Clone the repository on your local machine**
- **Make sure you have JDK 17 and MariaDB installed**
-  **run the script located at `/RidePalProject/RidePalApplication/db/create/create.sql` to create the schema**
-  **run the scripts located at `/RidePalProject/RidePalApplication/db/insert` to populate
   the Database in the following order: artists, genres, albums, roles, roles_seq, songs, tags, users, playlists, playlist_songs, playlist_tags, playlists_genres, users_roles**
- **API keys are located in application.properties for your convenience**
- **Access the application at the following url http://localhost:8080**
### Login Details
    The credentials below are specifically for testing purposes. 
    It's essential to highlight that the application, leveraging Spring Security, 
    encrypts and safeguards passwords, ensuring they are not stored in their original form.

- Admin user : **username:** `simonanedeva` **password:** `123456`
- User : **username:** `alex_ivanova` **password:** `123456`


## Technologies
- Java 17
- SpringBoot
- Spring Security
- Spring MVC
- Spring Data JPA
- MariaDB
- Thymeleaf
- HTML5
- CSS
- Javascript
- JUnit
- Mockito
### Database schema

<img src="https://github.com/RidePalProject-Telerik-Academy/RidePalProject/blob/main/RidePalApplication/src/main/resources/static/images/database_design.jpeg" alt="db schema">

## Main Functionalities


### Team Members
- **Boris Neykov** - GitHub [https://github.com/BorisNeykov1202]
- **Simona Nedeva** - GitHub [https://github.com/simonanedeva]
- **Nikolay Dimitrov** - GitHub [https://github.com/YordanoffNikolay]
