FROM openjdk:8-jdk-alpine
WORKDIR . .
ADD target/ClusterDB-Bootstrapper-1.jar ClusterDB-Bootstrapper-1.jar
VOLUME ./DatabaseUsers
VOLUME ./CluserNodes
EXPOSE 9090
COPY ./src/main/resources/application.properties ./src/main/resources/application.properties
COPY ./DatabaseUsers ./DatabaseUsers
COPY ./ClusterNodes ./CluserNodes
ENTRYPOINT ["java","-jar","ClusterDB-Bootstrapper-1.jar"]

