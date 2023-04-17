FROM adoptopenjdk/openjdk11

RUN git clone https://github.com/twinson37/yolov5.git
RUN git clone https://github.com/twinson37/MismatchedSocks.git

COPY ./socksAI//build/libs/socksAI-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

