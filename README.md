# nashorn-rest-api

The Multithreading Nashorn + Spring Boot project.
Client sends a script into the server by the POST method. If script is ready than server response 200 OK, otherwise 202 Accepted.
Also the server has functionality to kill the script by request.

How to build:

- Import and build up the project by Maven or Gradle
- Run NashornRestApiApplication class and open up http://localhost:8090/
- Select and upload one of the following JS files - resources/templates/js/
