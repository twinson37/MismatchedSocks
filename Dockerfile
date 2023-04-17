FROM twinson37/jdk-python

RUN apt-get update
RUN apt-get upgrade -y
# Upgrade installed packages
RUN apt update && apt upgrade -y && apt clean

RUN git clone https://github.com/twinson37/yolov5.git ./root/yolov5
RUN git clone https://github.com/twinson37/MismatchedSocks.git ./root/MismatchedSocks

COPY ./socksAI/build/libs/socksAI-0.0.1-SNAPSHOT.jar ./root/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

