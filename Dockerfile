FROM adoptopenjdk/openjdk11

COPY ./socksAI//build/libs/socksAI-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

