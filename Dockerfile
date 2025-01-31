FROM openjdk:22
WORKDIR /app
COPY target/Banking-0.0.1-SNAPSHOT.jar banking-app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "banking-app.jar"]
