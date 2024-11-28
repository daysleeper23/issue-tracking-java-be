FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml ./

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY .env /app/

EXPOSE 8080

#CMD ["mvn spring-boot:run"]
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]