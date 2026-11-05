# Aprendizaje — Mini Tasks

## Cambios entre versiones

| Versión | Qué cambia |
|---|---|
| **V1** (`/v1/tasks`) | Controlador con una lista en memoria. Devuelve objetos directamente y usa `@ResponseStatus` para el `201`/`204`. |
| **V2** (`/v2/tasks`) | Igual, en memoria, pero con `ResponseEntity`: controla el código de estado y añade la cabecera `Location` al crear. |
| **V4** (`/v4/tasks`) | Separación por capas: controlador → servicio → repositorio JPA sobre H2. Usa DTOs de entrada (`CreateTaskDTO`) y de salida (`TaskResponseDTO`). |

## Avance sesión 3: TDD de `GET /v1/tasks/{id}`

1. **Rojo.** Escribí dos tests en `TaskControllerV1Test`:
   - `obtenerPorId_existente_devuelve200ConLaTarea`: crea la tarea y la consulta
     dentro del mismo test, para no depender del orden de los tests.
   - `obtenerPorId_inexistente_devuelve404`.

   Los dos fallaron con **`405 Method Not Allowed`** en lugar de `200`/`404`.
   Ya existía `DELETE /v1/tasks/{id}`, así que Spring reconocía la ruta pero no
   el método `GET`.
2. **Verde.** Añadí `getById` con `@GetMapping("/tasks/{id}")`: busca en la lista
   y devuelve `ResponseEntity.ok(t)` o `ResponseEntity.notFound()`.
3. Último test en verde: `obtenerPorId_inexistente_devuelve404`.

## Slice vertical (JPA)

Operaciones nuevas en `/v4/tasks`:

| Petición | Respuesta |
|---|---|
| `POST /v4/tasks` `{"title":"..."}` | `201` + `Location` + DTO; `400` si el título está vacío |
| `GET /v4/tasks?done=true` | `200` con las tareas filtradas (`findByDone`) |
| `GET /v4/tasks/{id}` | `200` con el DTO, o `404` si no existe |
| `PATCH /v4/tasks/{id}/done` | `200` con la tarea marcada como hecha, o `404` si no existe |

Recorrido: HTTP → `TaskControllerV4` → `TaskService` → `TaskRepository`
(Spring Data JPA) → H2 → `TaskResponseDTO` → JSON.

## Responsabilidades de cada capa

- **Controlador (`web`)**: solo HTTP. Recibe parámetros y valida el cuerpo
  (`@Valid`), llama al servicio y elige el código de estado. No contiene
  lógica de negocio.
- **Servicio (`services`)**: reglas de negocio y transacciones
  (`@Transactional`). Si no encuentra la tarea lanza `TaskNotFoundException`.
  Convierte las entidades a DTO.
- **Repositorio (`repositories`)**: acceso a datos con `JpaRepository`. El
  método derivado `findByDone` genera la consulta automáticamente.
- **Entidad (`entities.Task`)**: el mapeo con la tabla. No sale fuera del
  servicio.
- **`ApiExceptionHandler`**: traduce `TaskNotFoundException` en `404` y los
  errores de validación en `400`, así los controladores quedan limpios.

## Decisiones

- **DTO de salida** (`TaskResponseDTO`) para no exponer la entidad JPA y
  desacoplar el contrato HTTP del modelo de datos.
- **Excepción propia** en lugar de `IllegalArgumentException`: el `404` sale
  de un error de dominio concreto y no de cualquier argumento inválido.
- En `markDone` no hace falta llamar a `save()`: la entidad está gestionada
  dentro de la transacción y el cambio se guarda al hacer commit (*dirty
  checking*). El test de integración lo comprueba volviendo a consultar la
  tarea.
- `ddl-auto=create-drop`: la base de datos H2 en memoria se crea en cada
  arranque. No se piden migraciones.

## Pruebas

```bash
mvn test   # 21 tests
```

- `TaskControllerV1Test` / `TaskControllerV2Test`: contratos HTTP con `@WebMvcTest`.
- `TaskServiceTest`: servicio aislado con un repositorio simulado (Mockito).
- `TaskControllerV4IntegrationTest`: slice completo con `@SpringBootTest` y H2,
  incluidos los casos inexistentes (`404`) y la validación (`400`).
