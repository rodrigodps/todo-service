FROM ubuntu
MAINTAINER Rodrigo de Paula <rodrigo.santos@jaya.tech>
RUN apt-get update
RUN apt-get install -y openjdk-8-jre
CMD mkdir -p /opt/todo/config
ADD libs/vertx-todo-service-fat.jar /opt/todo/
ADD config/config_jdbc.json /opt/todo/config/
ADD config/config_redis.json /opt/todo/config/
WORKDIR /opt/todo/
EXPOSE 8082
CMD java -jar vertx-todo-service-fat.jar -conf config/config_${VERTX_CONFIG}.json
