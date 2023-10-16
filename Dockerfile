FROM openjdk:17

WORKDIR /app

EXPOSE 80

ARG JAR_FILE=sseg-1.0.jar

COPY build/libs/${JAR_FILE} /app/

CMD sh -c 'java -jar /app/${JAR_FILE} --spring.profiles.active=dev'
