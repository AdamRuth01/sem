FROM openjdk:17-jdk-slim
COPY target/seMethods-0.1.0.2-jar-with-dependencies.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
