FROM maven:3.8.4-openjdk-21 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=builder target/*.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]