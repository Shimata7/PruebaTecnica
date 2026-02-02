# Prueba técnica - Arquitectura de Microservicios 

Este proyecto implementa una solución de **Core Bancario** utilizando una arquitectura de microservicios. El sistema gestiona clientes, cuentas y movimientos transaccionales, implementando comunicación síncrona y asíncrona entre servicios.

## 🚀 Tecnologías

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.3
* **Base de Datos:** PostgreSQL (Contenedorizada)
* **Mensajería:** Apache Kafka (Eventos asíncronos)
* **Comunicación Síncrona:** OpenFeign
* **Contenedores:** Docker & Docker Compose
* **Testing:** JUnit 5, Mockito & H2 Database (In-Memory)

##  Arquitectura

El sistema se compone de dos microservicios principales:

1.  **ms-clientes (`:8081`)**: Gestión de información personal y clientes.
2.  **ms-cuentas (`:8082`)**: Gestión de cuentas bancarias, saldos y movimientos.

### Flujos de Comunicación
* **Síncrono (Validación):** Al crear una cuenta, `ms-cuentas` consulta a `ms-clientes` vía **OpenFeign** para validar que el cliente exista y esté activo.
* **Asíncrono (Eventos):** Al eliminar un cliente, se envía un evento a **Kafka**. `ms-cuentas` escucha este evento y deshabilita automáticamente las cuentas asociadas.

---

## Despliegue con Docker

Para iniciar todo el ecosistema (Bases de datos, Kafka, Zookeeper y Microservicios), ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker-compose up -d --build
```

En la carpeta raiz se encuentra la coleccion de llamados en postman para probar todos los endpoints.

🧪 Ejecución de Pruebas (Testing)
Los proyectos están configurados para utilizar una base de datos en memoria (H2) durante la ejecución de los tests. Esto permite correr las pruebas unitarias y de integración sin necesidad de tener Docker levantado.

1. Tests de Clientes
```Bash
cd clientes
mvn test
```





