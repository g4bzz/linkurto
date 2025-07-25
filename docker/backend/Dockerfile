# Build Stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build


COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/linkurto-*.jar /app/

EXPOSE 8088

ENV RECAPTCHA_SECRET_KEY=teste
ENV DB_URL=jdbc:mysql://db:3306/db_linkurto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV 

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} linkurto-${JAR_VERSION}.jar
# - JSONArgsRecommended: JSON arguments recommended for CMD to prevent unintended behavior related to OS signals (line 23)