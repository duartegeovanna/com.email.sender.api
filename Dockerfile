FROM amazoncorretto:21-alpine3.18-jdk as build
WORKDIR /build
RUN apk update
RUN apk add maven

ENV PROJECT_NAME=email-sender-api
ENV JAR_NAME=email.sender.api*.jar

COPY settings.xml /root/.m2/settings.xml

COPY pom.xml ./

COPY . .
RUN [ "mvn", "-Dspring.profiles.active=${ENV:dev}", "-Dmaven.test.skip=true", "package" ]
RUN mv ./$PROJECT_NAME/target/$JAR_NAME ./application.jar

FROM amazoncorretto:21-alpine3.18-jdk as runner
RUN apk update
RUN apk --no-cache add curl

EXPOSE 8080
WORKDIR /app
COPY --from=build /build/application.jar ./application.jar

RUN ["apk", "add", "fontconfig"]
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts


CMD [ "java", "-Dspring.profiles.active=${ENV:-dev}", \
         "-jar", "application.jar"]