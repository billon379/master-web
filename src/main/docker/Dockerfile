FROM billon/jdk-alpine:8
ENV TZ=Asia/Shanghai
ENV SPRING_PROFILES_ACTIVE=""
VOLUME /tmp
ADD master-web.jar master-web.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar","/master-web.jar"]