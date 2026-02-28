# 🔐 API Security Example -- Spring Boot + JWT

Proyecto de ejemplo desarrollado con **Spring Boot 3**, **Spring
Security** y **JWT (JSON Web Tokens)** para demostrar un sistema de
autenticación y autorización basado en roles.

Este proyecto está pensado como recurso didáctico para explicar:

-   🔑 Autenticación con JWT
-   👤 Gestión de usuarios y roles
-   📚 API REST protegida
-   🛡️ Spring Security con filtro personalizado
-   🗄️ Persistencia con JPA + MySQL
-   🧪 Perfil de demostración con datos automáticos

------------------------------------------------------------------------

## 🚀 Tecnologías utilizadas

-   Java 17
-   Spring Boot 3.2.x
-   Spring Security 6
-   Spring Data JPA
-   MySQL
-   JJWT (io.jsonwebtoken)
-   Hibernate Validator
-   Maven

------------------------------------------------------------------------

## ⚙️ Configuración

### 1️⃣ Base de datos

Crear una base de datos en MySQL:

``` sql
CREATE DATABASE tubasededatos;
```

Configurar en `application.properties`:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/tubasededatos
spring.datasource.username=root
spring.datasource.password=
```

------------------------------------------------------------------------

### 2️⃣ Perfil DEMO

El proyecto incluye un perfil `demo` que:

-   Crea automáticamente usuarios y libros.
-   Muestra credenciales en consola.
-   Facilita pruebas en clase.

Activar perfil:

``` properties
spring.profiles.active=demo
```

------------------------------------------------------------------------

## 🔐 Autenticación

### Endpoint de login

    POST /api/v1/auth/signin

Body:

``` json
{
  "email": "alice.johnson@example.com",
  "password": "password123"
}
```

Respuesta:

``` json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

------------------------------------------------------------------------

## 📚 Endpoints protegidos

Ejemplo:

    GET /api/v1/libros

Header obligatorio:

    Authorization: Bearer <TOKEN>

------------------------------------------------------------------------

## 🛡️ Seguridad

-   Autenticación stateless.
-   Filtro JWT personalizado.
-   Roles: `ROLE_USER`, `ROLE_ADMIN`.
-   Contraseñas encriptadas con BCrypt.
-   Tokens firmados con HS256.

------------------------------------------------------------------------

## 📌 Flujo de autenticación

1.  Usuario envía email y password.
2.  Spring Security autentica.
3.  Se genera JWT.
4.  Cliente envía JWT en cada petición.
5.  Filtro valida firma y expiración.
6.  Se autoriza acceso según rol.

------------------------------------------------------------------------

## 🎓 Uso educativo

Este proyecto permite explicar:

-   Diferencia entre autenticación y autorización.
-   Funcionamiento interno de JWT.
-   Estructura Header / Payload / Signature.
-   Validación de firma.
-   Seguridad basada en roles.

Ideal para 2º DAW -- Desarrollo Web en Entorno Servidor.

------------------------------------------------------------------------

## 👨‍🏫 Proyecto con fines educativos para demostración en clase.
# JWT Authentication and Authorization with Spring Boot 3 and Spring Security 6

Medium
Article: https://medium.com/@truongbui95/jwt-authentication-and-authorization-with-spring-boot-3-and-spring-security-6-2f90f9337421
