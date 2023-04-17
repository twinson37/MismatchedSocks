FROM adoptopenjdk/openjdk11

# sources.list 교체 후 적용
COPY sources.list /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y
 
# container에 git 설치
RUN apt-get install git -y
RUN git clone https://github.com/twinson37/yolov5.git
RUN git clone https://github.com/twinson37/MismatchedSocks.git


COPY ./socksAI//build/libs/socksAI-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

