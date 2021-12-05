FROM openjdk:17
EXPOSE 8098
COPY target/*.jar tomorr.jar
CMD ["java", "-jar", "./tomorr.jar"]