FROM openjdk:21

COPY ./src src/
COPY ./pom.xml pom.xml

COPY --from=builder target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]