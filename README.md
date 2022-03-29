# _Google Keep Backend Microservices Application_

_**Microservices Google Keep Spring Boot Project Using MongoDB With Spring Security**_

# _Software Requirements_
* Application Type : [Microservices](https://spring.io/microservices)
* Build Tool : [Gradle](https://spring.io/guides/gs/gradle/)
* Database : [MongoDB](https://www.mongodb.com/try/download/community)
* IDE : [Eclipse](https://www.eclipse.org/downloads/)
* Java Version : [Java 11](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)

# _Includes_
* User Microservice
* Notes Microservice
* Crud Operations with [MultipartFile](https://www.baeldung.com/spring-file-upload)
* Exception Handling
* [WebClient](https://www.baeldung.com/spring-5-webclient) for Microservices Communication
* Spring Security

# _Note_
* Add **_admin_** document in **_users__** collection of **_Microservice_Google_Keep_** Database in MongoDB
```bash
db.users_.insert({
  userName: 'AdminUser',
  password: 'AdminPassword',
  role: 'ROLE_ADMIN'
})
```
