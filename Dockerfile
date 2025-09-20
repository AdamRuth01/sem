FROM openjdk:17-jdk-slim
COPY ./target/seMethods-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
