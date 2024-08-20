FROM amazoncorretto:21
EXPOSE 8084
ADD target/clients.jar clients.jar
ENTRYPOINT ["java", "-jar", "/clients.jar"]