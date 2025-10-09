# 🚀 Backend: Sistema de Facturación (Spring Boot 3 & JWT)

Este proyecto es el servicio RESTful para el sistema de facturación, implementado con **Spring Boot 3**, **JPA/Hibernate** y **Autenticación Stateless JWT**.

---

## 🛠️ Configuración e Inicialización

### 1. Requisitos Previos

* **Java:** JDK 21 o superior.
* **Gestor de Dependencias:** Maven.
* **Base de Datos:** PostgreSQL (Debe estar corriendo localmente en el puerto 5432).

### 2. Configuración Esencial (`application.properties`)

El único archivo que debes modificar es **`src/main/resources/application.properties`**.

**¡Importante!** Reemplaza los valores de `url`, `username` y `password` con las credenciales de tu **Base de Datos PostgreSQL local**:

```properties
# 🚨 MODIFICAR ESTOS VALORES
spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_de_tu_db
spring.datasource.username=tu_usuario_postgres
spring.datasource.password=tu_contraseña_postgres

# --- Configuración Estándar ---
# Hibernate se encarga de crear las tablas automáticamente si no existen
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Puerto de la aplicación (9090)
server.port=9090

