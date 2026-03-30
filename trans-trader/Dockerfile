FROM centos:7

LABEL maintainer="supermancell"
LABEL description="trans-trader Spring Boot Application"

ENV JAVA_VERSION=11
ENV APP_NAME=trans-trader
ENV APP_VERSION=1.0.0-SNAPSHOT
ENV APP_HOME=/opt/app

RUN yum install -y java-11-openjdk java-11-openjdk-devel && \
    yum clean all && \
    rm -rf /var/cache/yum

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk
ENV PATH=$JAVA_HOME/bin:$PATH

RUN mkdir -p ${APP_HOME}/logs

WORKDIR ${APP_HOME}

COPY target/${APP_NAME}-${APP_VERSION}.jar ${APP_HOME}/${APP_NAME}.jar

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENV SPRING_PROFILES_ACTIVE="prod"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar ${APP_HOME}/${APP_NAME}.jar"]
