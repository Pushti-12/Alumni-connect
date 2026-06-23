FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /workspace
COPY backend/pom.xml ./
COPY backend/src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/target/alumni-connect-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
