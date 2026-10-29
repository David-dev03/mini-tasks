# Mini Tasks

API REST pequeña de tareas con Spring Boot 4, JPA y H2.

## Endpoints

| Versión | Ruta | Descripción |
|---|---|---|
| V1 | `/v1/tasks` | Controlador simple en memoria |
| V2 | `/v2/tasks` | En memoria con `ResponseEntity` y `Location` |
| V4 | `/v4/tasks` | Controlador → Servicio → Repositorio JPA (H2) con DTOs |

## Ejecutar

```bash
mvn spring-boot:run
```

## Tests

```bash
mvn test
```
