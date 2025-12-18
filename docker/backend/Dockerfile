# Build Stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build


COPY backend/pom.xml .
RUN mvn dependency:go-offline
COPY backend/src ./src
RUN mvn clean package -DskipTests
#RUN mvn clean package

# Runtime Stage
FROM amazoncorretto:17
WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=build /build/target/linkurto-*.jar /app/app.jar

EXPOSE 8088

CMD ["java", "-jar", "app.jar"]