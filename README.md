acmeServer
==========
To install acmeServer, run the command: mvn clean install

This was built with Spring Boot, you can run it from the command line with the command: mvn spring-boot:run

There is one integration test, RestErrorHandlerTest which is currently set to @Ignore. I didn't create a separate maven profile for integration tests. The integration test works if the database has been created and the user credentials matches between the database you connect to and the user credentials in the application.properties file under src/main/resources. If you want to run the application, look over the database script database.sql and change it accordingly. I used MySQL for this project.

