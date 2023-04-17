FROM adoptopenjdk/openjdk11

# sources.list 교체 후 적용

RUN apt-get update
RUN apt-get upgrade -y
# Upgrade installed packages
RUN apt update && apt upgrade -y && apt clean

# install python 3.7.10 (or newer)
RUN apt update && \
    apt install --no-install-recommends -y build-essential software-properties-common && \
    add-apt-repository -y ppa:deadsnakes/ppa && \
    apt install --no-install-recommends -y python3.7 python3.7-dev python3.7-distutils && \
    apt clean && rm -rf /var/lib/apt/lists/*

# Register the version in alternatives (and set higher priority to 3.7)
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.6 1
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.7 2

# Upgrade pip to latest version
RUN curl -s https://bootstrap.pypa.io/get-pip.py -o get-pip.py && \
    python3 get-pip.py --force-reinstall && \
    rm get-pip.py
# container에 git 설치
RUN apt-get install git -y
RUN git clone https://github.com/twinson37/yolov5.git ./root/yolov5
RUN git clone https://github.com/twinson37/MismatchedSocks.git ./root/MismatchedSocks
RUN pip install -r ./root/yolov5/requirements.txt

COPY ./socksAI//build/libs/socksAI-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

