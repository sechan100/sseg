FROM openjdk:17

WORKDIR /app

COPY build/libs/sseg-1.0.jar /app/sseg.jar

EXPOSE 80

CMD ["java", "-jar", "/app/sseg.jar", "--spring.profiles.active=local"]
