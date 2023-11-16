FROM gradle:8.4.0-jdk21 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build -x test --no-daemon

FROM amazoncorretto:21
EXPOSE 8081
RUN mkdir /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
