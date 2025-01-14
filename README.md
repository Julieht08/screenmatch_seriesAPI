# 🎬 Movie Database API

# screenmatch_seriesAPI
Proyecto de screenmatch con OMDB API. Echo para un curso de back-end con Spring, para **Alura Latam y ONE - Oracle Next Education**.

## Descripción:
Este proyecto es una aplicación back-end que utiliza la API de OMDb, para gestionar información sobre películas y series. Los datos se almacenan en una base de datos PostgreSQL y se gestionan a través de un servidor construido con Spring Boot.

## 🛠️ Tecnologías:
- **Lenguaje:** Java 
- **Framework:** Spring Boot 3.3.5
- **Base de Datos:** PostgreSQL
- **API:** OMDb API
- **Gestión de Dependencias:** Maven

## 📦 Instalación
Sigue estos pasos para configurar el proyecto localmente:

1- Clonar el repositorio, la parte del front-end y la parte del back-end:

- git clone: `https://github.com/Julieht08/screenmatch_seriesAPI.git`
  
2- Configurar PostgreSQL:

- Instala PostgreSQL en tu equipo si aún no lo tienes.
- Crea una base de datos con el nombre DB_NAME.
  
3- Configurar las credenciales de la base de datos: En el archivo application.properties de Spring Boot, configura las credenciales de tu base de datos PostgreSQL:

spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}

spring.datasource.username=${DB_USER}

spring.datasource.password=${DB_PASSWORD}

spring.datasource.driver-class-name=org.postgresql.Driver

hibernate.dialect=org.hibernate.dialect.HSQLDialect

4- Ejecutar el proyecto: Abre el proyecto en IntelliJ IDEA y ejecutalo:
- Abrir el archivo 2086-screenmatch-front-end-main:
- Abrir el archivo index.html en el navegador para poder visualizar el proyecto.

## Puerto del Servidor:
server.port=8081

## Interactuar con el Proyecto:
La aplicación estará disponible en `http://localhost:8081`.

