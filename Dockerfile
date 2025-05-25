FROM amazoncorretto:21-alpine3.18-jdk as build
WORKDIR /build
RUN apk update
RUN apk add maven

ENV PROJECT_NAME=email-sender-api
ENV JAR_NAME=email.sender.api*.jar

COPY pom.xml ./

COPY . .
RUN mv ./$PROJECT_NAME/target/$JAR_NAME ./application.jar

FROM amazoncorretto:21-alpine3.18-jdk as runner
RUN apk update
RUN apk --no-cache add curl

EXPOSE 8080
WORKDIR /app
COPY --from=build /build/application.jar ./application.jar

CMD [ "java", "-jar", "application.jar"]
