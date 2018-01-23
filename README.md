# cookBook
CookBook REST API.
This project was made to learn building REST API.

## Technology stack:

  * Java 8
  * SpringBoot 2.0.0 M7
  * JPA/Hibernate
  * Maven 3.5
  * Thymeleaf 3.0
  * Mockito, JUnit


## How to run project:
* Clone or download this repository 
* Make sure you are using JDK 1.8 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:

```
        java -jar target/cookbook-1.0.jar
or
        mvn cookbook:run
```


## About Service

This is REST service enabling posting and evaluating cooking recipes. To post a recipe or comment, user must register and authenticate.

Main activities that are managed:

  * Registration
  * Creation and updating recipes
  * Posting comments on recipes
  * Evaluating recipes

Here is some of what this project cover:

* MVC and DTO patterns
* Integration with JPA/Hibernate
* Pagination handling
* Data validation
* Activation account/Reset password via email
* Brute force authentication attempts prevention
* Use of Project Lombok
* Mockito and JUnit tests
* API documented by Swagger2

## To preview all endpoints in Swagger 2 API docs

Run server and browse to ```localhost:8080/v2/api-docs```
