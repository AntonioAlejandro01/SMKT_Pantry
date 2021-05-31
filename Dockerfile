FROM maven:3-openjdk-11 as Builder

WORKDIR /build

COPY pom.xml .

RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r target/

COPY src ./src

RUN mvn clean package  -Dmaven.test.skip

RUN mv ./target/smkt-pantry.jar /app.jar

FROM openjdk:11-jre-slim

WORKDIR /opt/server

COPY --from=Builder /app.jar  ./app.jar

ENV PORT=4070
ENV EUREKA_URL=http://smkt-eureka:8761/eureka
ENV LEVEL=INFO
ENV DB_NAME=smkt
ENV DB_CONNECTION=mongodb://root:secret@localhost:27017/
ENV ID_FILES_INSTANCE=smkt-files
ENV ID_OAUTH_INSTANCE=smkt-oauth


EXPOSE ${PORT}

CMD java -jar app.jar --server.port="${PORT}" --eureka.client.service-url.defaultZone="${EUREKA_URL}" --logging.level.'[com.antonioalejandro.smkt.pantry]'="${LEVEL}" --spring.data.mongodb.uri="${DB_CONNECTION}" --spring.data.mongodb.database="${DB_NAME}"  --id_files_instance="${ID_FILES_INSTANCE}"  --id_oauth_instance="${ID_OAUTH_INSTANCE}"

