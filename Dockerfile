FROM openjdk:11 AS build

COPY . ./
RUN ./gradlew build

FROM openjdk:11
ARG JAR_FILE
COPY --from=build /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]