FROM amazoncorretto:21
EXPOSE 8761
ADD target/eureka.jar eureka.jar
ENTRYPOINT ["java", "-jar", "/eureka.jar"]