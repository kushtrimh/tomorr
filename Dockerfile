FROM openjdk:17

COPY target/*.jar tomorr.jar
EXPOSE 8098

CMD ["java", "-jar", "./tomorr.jar", "--spring.config.location=file:/etc/opt/tomorr/application.yml"]
