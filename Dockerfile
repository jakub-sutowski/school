FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src

ENV SPRING_PROFILES_ACTIVE=docker

CMD ["./mvnw", "spring-boot:run"]