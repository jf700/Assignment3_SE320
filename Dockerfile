FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:22-jre-jammy
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
WORKDIR /app
COPY --from=build /app/target/digitaltherapy-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources/knowledge-base /app/knowledge-base
RUN mkdir -p /app/data/vectors && chown -R appuser:appgroup /app
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar", "--app.cli.enabled=false"]
