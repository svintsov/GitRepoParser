# Spring Boot Git Repository Parser

This is a sample Java / Maven / Spring Boot (version 3.0.0) application that can be used as a
starter for creating a microservice capable of fetching various data from Git repositories.

## How to Run

This application is packaged as a jar which has Tomcat 9 embedded. No Tomcat or JBoss installation
is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 17 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Obtain the secret token. Refer to [GitHub App Authentication Guide](https://developer.github.com/apps/building-github-apps/authenticating-with-github-apps/) for more information.
* Once successfully built, you can run the service by one of these two methods:

```
        java -jar -Dsecurity.token={paste_your_token_here} target/git-repo-parser.jar
or
        mvn spring-boot:run -Drun.arguments="security.token={paste_your_token_here}"
```

## Docker

Also, the service could be packaged as Docker image and later you run it using ```docker run``` command.

```
docker run --env ACCESS_TOKEN={paste_your_token_here} {image_id}   
```

## About the Service

The service is just a simple REST Github repository parser. It main and so far the only
feature is to fetch all public repositories for user that are not fork and for each repository retrieve the list of branches.

Here is what this little application demonstrates:

* Full integration with the latest **Spring** Framework: inversion of control, dependency injection,
  etc.
* Exception mapping from application exceptions to the right HTTP response with exception details in
  the body
* MockMVC test framework with associated libraries
* Calls to Github API with a help of WebClient
* Processing API responses in a reactive way
* APIs are "self-documented" by Swagger3 using annotations

Here are some endpoints you can call:

### Retrieve a list of public repositories for user

```
GET http://localhost:8080/repositories/danvega
Accept: application/json

Response: HTTP 200
Content: [
    {
        "name": "abstracts",
        "ownerName": "danvega",
        "branches": [
            {
                "name": "main",
                "lastCommitSHA": "9785e7697b1a6afea4637683688f7d4d6df9b242"
            }
        ]
    }
]
```

### To view Swagger 3 API docs

Run the server and browse to http://localhost:8080/swagger-ui/index.html

#### Questions and Comments: svintcov95@gmail.com

