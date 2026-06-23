FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/target/alumni-connect-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
CMD ["java", "-jar", "app.jar"]