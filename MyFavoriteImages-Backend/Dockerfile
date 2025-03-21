# Usar una imagen base de Gradle con JDK 11
FROM 8.13.0-jdk21 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de construcción
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Construir la aplicación
RUN gradle build

# Usar una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:11-jre-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# Usar una imagen base de PostgreSQL
FROM postgres:13

# Establecer las variables de entorno para la base de datos
ENV POSTGRES_DB=images
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=mala_practica!!1

# Exponer el puerto en el que PostgreSQL se ejecutará
EXPOSE 5432
