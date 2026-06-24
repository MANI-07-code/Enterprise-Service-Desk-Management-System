#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY target/main-project-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar","app.jar"]
# Build Stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]