# SMKT_Pantry

Service to manage products in SmartKitchen App

[![Build Dev](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/buildDevVersion.yml/badge.svg?branch=develop)](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/buildDevVersion.yml) [![Build Snapshot](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/BuildSnapshot.yml/badge.svg?branch=main)](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/buildDevVersion.yml) [![Build Release](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/BuildRelease.yml/badge.svg?branch=main)](https://github.com/AntonioAlejandro01/SMKT_Pantry/actions/workflows/buildDevVersion.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AntonioAlejandro01_SMKT_Pantry&metric=alert_status)](https://sonarcloud.io/dashboard?id=AntonioAlejandro01_SMKT_Pantry) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AntonioAlejandro01_SMKT_Pantry&metric=coverage)](https://sonarcloud.io/dashboard?id=AntonioAlejandro01_SMKT_Pantry)

## Use With Docker

Use this Service with Docker as Docker container. The Repo have 3 types of images. 

### Types

- **Stable**: this are the images that in her tag is a specific version ex.: ```antonioalejandro01/smkt-pantry:vX.X.X```. the last tag version have latest tag. 
```bash
    docker pull antonioalejandro01/smkt-pantry:v1.0.0
    # The last stable version
    docker pull antonioalejandro01/smkt-pantry:latest
 ```

- **Snapshot**: this are the images that in her tag is snapshot ex.: ```antonioalejandro01/smkt-pantry:snapshot```
```bash 
    docker pull antonioalejandro01/smkt-pantry:snapshot
```

- **Dev**: this image is only for developers and in her tag have dev ```antonioalejandro01/smkt-pantry:dev```
```bash
    docker pull antonioalejandro01/smkt-pantry:dev
 ```

### Environment variables for Docker image

<table align="center" width="100%" style="margin:1em;">
<thead>
    <tr>
        <th>Name</th>
        <th>Default Value</th>
        <th>Description</th>
    </tr>
</thead>
<tbody>
    <tr>
        <td>PORT</td>
        <td>4070</td>
        <td>Micro service port</td>
    </tr>
    <tr>
        <td>EUREKA_URL</td>
        <td>http://smkt-eureka:8761/eureka</td>
        <td>The url where the smkt-eureka be</td>
    </tr>
    <tr>
        <td>LEVEL</td>
        <td>INFO</td>
        <td>Log level for all log relational for this repo. <i>Recommend only change for development</i></td>
    </tr>
    <tr>
        <td>DB_NAME</td>
        <td>smkt</td>
        <td>Name for mongo database</td>
    </tr>
    <tr>
        <td>DB_COLLECTION</td>
        <td>products</td>
        <td>Name for Collection in mongo database</td>
    </tr>
    <tr>
        <td>DB_CONNECTION</td>
        <td>mongodb://root:secret@localhost:27017/</td>
        <td>String connection to mongo database</td>
    </tr>
    <tr>
        <td>id_files_instance</td>
        <td>smkt-files</td>
        <td>Id that service <a href="http://github.com/antonioAlejandro01/SMKT_Files">smkt-files</a> have it in <a href="http://github.com/antonioAlejandro01/SMKT_Eureka">smkt-eureka</a></td>
    </tr>
    <tr>
        <td>id_oauth_instance</td>
        <td>smkt-oauth</td>
        <td>Id that service <a>smkt-oauth</a> have it in <a href="http://github.com/antonioAlejandro01/SMKT_Eureka">smkt-eureka</a></td>
    </tr>
    
</tbody>
</table>


#### Docker command

```bash
    docker run -d -p4070:4070 -ePORT=4070 -eEUREKA_URL=http://127.0.0.1:8761/eureka -eDB_NAME=smkt -eDB_COLLECTION=products -eDB_CONNECTION=mongodb://root:secret@127.0.0.1:27017/ -t antonioalejandro01/smkt-pantry:latest
 ```

## Use in Docker Compose

```yaml
    cookbook:
        image: antonioalejandro01/smkt-pantry:latest
        container_name: smkt-pantry
        environment:
            PORT: 4080
            EUREKA_URL: http://127.0.0.1:8761/eureka
            DB_NAME: smkt
            DB_COLLECTION: products
            DB_CONNECTION: mongodb://root@secret@mongo:27017/
        expose:
            - "4070"
        ports: 
            - "4070:4070"
    mongo: # Mongo database for microservice
        image: mongo
        container_name: smkt-mongo
        restart: always
        environment:
            MONGO_INITDB_ROOT_USERNAME: root
            MONGO_INITDB_ROOT_PASSWORD: secret
```


