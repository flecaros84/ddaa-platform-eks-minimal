# Imagen de build: compila el microservicio con Maven y Java 17.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Se copia primero el pom.xml para aprovechar cache de dependencias.
COPY pom.xml ./
RUN mvn -Dmaven.test.skip=true dependency:go-offline

# Se copia el código fuente y se genera el JAR ejecutable.
COPY src ./src
RUN mvn -Dmaven.test.skip=true package

# Imagen runtime: más liviana y enfocada solo en ejecutar el JAR.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
