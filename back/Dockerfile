FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY .env .env
COPY src/main/resources/top_50_tickers.json top_50_tickers.json
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]