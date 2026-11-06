# Declaración de uso de IA

## 1. Resumen

| Campo | Respuesta |
|---|---|
| Ejercicio/Proyecto | Mini Spring Boot Tasks (UD02a - Ejercicio 01) |
| Alumno/a o grupo | David Muñoz Valdés |
| Fecha | 06/11/2026 |
| ¿Se ha usado IA? | Sí |
| Herramientas | Asistente de codificación (Antigravity / Claude) |
| Ámbitos de uso | Depuración de tests, redacción de documentación y consultas de sintaxis |

## 2. Registro de usos materiales

| # | Herramienta y propósito | Resultado usado, modificado o rechazado | Verificación realizada y evidencia | Fuentes o criterio de contraste | Archivo, decisión o prueba afectada |
|---|---|---|---|---|---|
| 1 | Consulta sobre compatibilidad de Mockito con JDK moderno | Se configuró `mock-maker-subclass` para evitar problemas de agent attachment | Ejecución de `mvn test` en verde (21/21 tests pasando) | Documentación de Mockito 5 y Spring Boot 4 | `pom.xml` / tests |
| 2 | Redacción y síntesis técnica del documento de aprendizaje | Se estructuraron las responsabilidades por capas y el flujo TDD | Revisión manual para asegurar que coincide con el código implementado | Contenidos de la UD2a y sesiones de Battleship | `aprendizaje.md` |

## 3. Explicación y defensa

- **Comprensión del flujo:** Comprendo y puedo defender cada capa: `TaskControllerV4` recibe los DTOs y valida con `@Valid`; `TaskService` gestiona las transacciones y la conversión de entidad a `TaskResponseDTO`; y `TaskRepository` accede a la base de datos H2 con Spring Data JPA.
- **TDD:** Puedo explicar el ciclo rojo-verde de la sesión 3 donde `GET /v1/tasks/{id}` fallaba primero con `405 Method Not Allowed` antes de implementar el método en el controlador.
- **Excepciones:** El mapeo del error `404` se gestiona limpiamente mediante `TaskNotFoundException` interceptada por `ApiExceptionHandler`.

## 4. Declaración

Declaro que he registrado el uso material de IA, he verificado y comprendo el código y las pruebas entregadas, y puedo explicar y defender cualquiera de las decisiones técnicas adoptadas.

Firma: David Muñoz Valdés