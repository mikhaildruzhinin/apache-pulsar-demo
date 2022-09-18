FROM openjdk:8u232

ARG SBT_VERSION=1.7.1

RUN \
  mkdir /working/ && \
  cd /working/ && \
  wget https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz && \
  tar xzvf sbt-$SBT_VERSION.tgz -C /usr/share/ && \
  update-alternatives --install /usr/bin/sbt sbt /usr/share/sbt/bin/sbt 9999 && \

  rm sbt-$SBT_VERSION.tgz && \
  cd && \
  rm -r /working/ && \
  sbt sbtVersion

RUN mkdir -p /root/build/src
ADD ./build.sbt /root/build/
ADD ./src/ /root/build/src/
RUN cd /root/build && sbt compile

WORKDIR /root/build
