FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY .env .env
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]