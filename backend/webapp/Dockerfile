FROM maven:3.9.7 as maven

WORKDIR /app
COPY . /app

RUN mvn clean package

FROM eclipse-temurin:21
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY --from=maven /app/webapp/target/webapp-1.0-SNAPSHOT.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]