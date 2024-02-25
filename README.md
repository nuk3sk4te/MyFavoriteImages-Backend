# Contexto del proyecto
Parte backtend de aplicación creada para gestión de imagenes en la cual se puede guardar, editar, borrar y visualizar imagens favoritas de diferentes usuarios.

Enlace para la parte frontend:

https://github.com/crismouta/MyFavoriteImages-Frontend.git

# Vistas

Home Login

![home-login](https://github.com/crismouta/MyFavoriteImages-Frontend/assets/82060703/83402d78-9d66-4516-83e5-61911226d44a)

Home User

![home-auth](https://github.com/crismouta/MyFavoriteImages-Frontend/assets/82060703/dc910c30-f8a1-417a-bdbd-5233b4d8c6e6)


Galeria

![gallery](https://github.com/crismouta/MyFavoriteImages-Frontend/assets/82060703/885523d2-e8fb-47f2-b25f-7aac1b908a1b)


# Características generales

-  Hacer login con usuarios
-  Visualización de galeria de imagenes con título y descripción.
-  Visualización de cada imagen al darle click tanto a la imagen como a su título.
-  Añadir nuevas imagenes a la galeria.
-  Añadir título y description de la imagen.
-  Editar título y description de la imagen.
-  Cambiar la imagen.
-  Borrar imagenes.


# Instalación


`clonar el repositorio: https://github.com/crismouta/MyFavoriteImages-Backend.git`

`instalar Java17`

`configurar datos necesarios para conexión con Base de Datos PostgreSQL en el archivo 'src/resources/main/application.properties.`

`los datos de prueba están en el archivo DataInitializer, se puede modificar para tener datos de prueba distintos.`


## Running app

`Para el desarrollo de este proyecto se ha usado el IDE Intellij Community pero se puede ejecutar la aplicación Java desde tu IDE favorito haciendo las configuraciones necesarias.`

## Running Tests

`Para el desarrollo de este proyecto se ha usado el IDE Intellij Community pero se puede ejecutar los tests Java desde tu IDE favorito haciendo las configuraciones necesarias.`

## Metodología de trabajo, arquitectura y técnicas

-   Spring Boot (Structure by Layer)
-   Patrón de diseño Repository
-   Servicios
-   TDD
-   Tests


## Herramientas y tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Security
- Basic Auth
- Jwt
- Junit
- PostgreSQL
- PgAdmin 4
- Postman
- Intellij
