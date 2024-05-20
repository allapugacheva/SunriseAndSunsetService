FROM openjdk:21

COPY ./src src/
COPY ./pom.xml pom.xml

COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]