FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp
ARG JAR_FILE=/*.jar
COPY ./target/${JAR_FILE} producer.jar

ENTRYPOINT ["java", "-jar", "producer.jar"]