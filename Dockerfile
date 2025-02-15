FROM eclipse-temurin:21.0.6_7-jre-alpine-3.21
RUN mkdir /app
WORKDIR /app
COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]