FROM maven:3-openjdk-11 as build

WORKDIR /opt/build

COPY . .

RUN mvn  clean compile install

RUN mv ./target/smkt-pantry.jar /app.jar

FROM openjdk:11

WORKDIR /opt/server

COPY --from=build /app.jar  ./app.jar

ARG port=4070
ARG eureka_url=http://smkt-eureka:8761/eureka
ARG level=INFO
ARG db_name=smkt
ARG db_collection=products
ARG db_connection=mongodb://root:secret@localhost:27017/
ARG id_files_instance=smkt-files
ARG id_oauth_instance=smkt-oauth



ENV PORT ${port}
ENV EUREKA_URL ${eureka_url}
ENV LEVEL ${level}
ENV DB_NAME ${db_name}
ENV DB_COLLECTION ${db_collection}
ENV DB_CONNECTION ${db_connection}
ENV ID_FILES_INSTANCE ${id_files_instance}
ENV ID_OAUTH_INSTANCE ${id_oauth_instance}


EXPOSE ${PORT}

CMD java -jar app.jar --server.port="${PORT}" --eureka.client.service-url.defaultZone="${EUREKA_URL}" --logging.level.'[com.antonioalejandro.smkt.pantry]'="${LEVEL}" --mongodb.connection="${DB_CONNECTION}" --mongodb.database.name="${DB_NAME}" --mongo.database.collection="${DB_COLLECTION}" --id_files_instance="${ID_FILES_INSTANCE}"  --id_oauth_instance="${ID_OAUTH_INSTANCE}"

