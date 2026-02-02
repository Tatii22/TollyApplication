# Implementación de Category

## Estructura Creada

```
src/main/java/com/rentaherramientas/tolly/
├── application/
│   ├── dto/category/
│   │   ├── CategoryResponse.java
│   │   ├── CreateCategoryRequest.java
│   │   └── UpdateCategoryRequest.java
│   ├── mapper/
│   │   └── CategoryMapper.java
│   └── usecase/category/
│       ├── GetCategoriesUseCase.java
│       ├── GetCategoryByIdUseCase.java
│       ├── CreateCategoryUseCase.java
│       ├── UpdateCategoryUseCase.java
│       └── DeleteCategoryUseCase.java
└── infrastructure/
    └── persistence/adapters/in/rest/
        └── CategoryController.java (refactorizado)
```

---

## DTOs

| DTO | Uso |
|-----|-----|
| **CategoryResponse** | Devuelve categorías en respuestas |
| **CreateCategoryRequest** | Recibe datos para crear categoría |
| **UpdateCategoryRequest** | Recibe datos para actualizar categoría |

---

## Mapper

**CategoryMapper.java** - Convierte entre modelos de dominio y DTOs

- `toCategoryResponse()` - Category → CategoryResponse
- `toCategory(CreateCategoryRequest)` - CreateCategoryRequest → Category
- `toCategory(UpdateCategoryRequest)` - UpdateCategoryRequest → Category

---

## Use Cases

| UseCase | Método HTTP | Descripción |
|---------|-------------|-------------|
| **GetCategoriesUseCase** | GET /categories | Listar todas las categorías |
| **GetCategoryByIdUseCase** | GET /categories/{id} | Obtener categoría por ID |
| **CreateCategoryUseCase** | POST /categories | Crear nueva categoría (con validación de duplicados) |
| **UpdateCategoryUseCase** | PUT /categories/{id} | Actualizar categoría (con validación) |
| **DeleteCategoryUseCase** | DELETE /categories/{id} | Eliminar categoría |

---

## Cambios en Puertos

**domain/ports/CategoryRepository.java**
- ✅ Método nuevo: `Optional<Category> update(Long id, Category category);`

---

## Cambios en Adaptadores

**infrastructure/persistence/adapters/out/persistence/CategoryRepositoryAdapter.java**
- ✅ Implementado método `update()` que busca la categoría, actualiza el nombre y guarda

---

## Refactorización del Controlador

**infrastructure/persistence/adapters/in/rest/CategoryController.java**

El controlador ahora inyecta 5 UseCases y está completamente refactorizado:

```java
@Tag(name = "Gestión de Categorías")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    // Constructor con inyección de UseCases
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() { ... }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) { ... }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) { ... }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) { ... }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) { ... }
}
```

**Características implementadas:**
- ✅ 5 UseCases inyectados
- ✅ Anotaciones Swagger completas (@Operation, @Tag, @ApiResponse)
- ✅ Validación con @Valid
- ✅ Seguridad con @PreAuthorize("isAuthenticated()")
- ✅ DTOs (CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest)
- ✅ Status HTTP correctos (201, 204)
- ✅ Manejo de excepciones con DomainException


---

## Flujo de Datos

```
Cliente REST
    ↓
CategoryController (recibe request)
    ↓
UseCase (lógica de negocio)
    ↓
CategoryMapper (convierte DTO → Domain)
    ↓
CategoryRepository (puerto)
    ↓
CategoryRepositoryAdapter (implementación)
    ↓
CategoryJpaRepository (Spring Data JPA)
    ↓
Base de Datos
```

---

## Endpoints REST

| Método | Endpoint | Autenticación | Autorización | Respuesta |
|--------|----------|---------------|--------------|-----------|
| GET | `/categories` | ❌ No | ❌ No | `List<CategoryResponse>` (200) |
| GET | `/categories/{id}` | ❌ No | ❌ No | `CategoryResponse` (200) o 404 |
| POST | `/categories` | ✅ Sí | `hasRole('ADMIN')` | `CategoryResponse` (201) |
| PUT | `/categories/{id}` | ✅ Sí | `hasRole('ADMIN')` | `CategoryResponse` (200) |
| DELETE | `/categories/{id}` | ✅ Sí | `hasRole('ADMIN')` | Vacío (204) |

---

## Validaciones

✅ Nombre no puede estar vacío (@NotBlank)  
✅ No permite categorías duplicadas  
✅ Valida existencia antes de actualizar/eliminar  
✅ Lanza `DomainException` si validación falla  

---

## Ejemplo de Uso

**Crear categoría:**
```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"name": "Herramientas Manuales"}'
```

**Listar categorías:**
```bash
curl http://localhost:8080/categories
```

**Actualizar categoría:**
```bash
curl -X PUT http://localhost:8080/categories/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"name": "Herramientas Eléctricas"}'
```

**Eliminar categoría:**
```bash
curl -X DELETE http://localhost:8080/categories/1 \
  -H "Authorization: Bearer {token}"
```

---

## Estado ✅

Todos los componentes implementados y listos para producción.

---

## Validación en Postman

### 1️⃣ Listar categorías (GET - Sin autenticación)

**URL:** `GET http://localhost:8080/categories`

**Headers:** Ninguno requerido

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Herramientas Manuales"
  },
  {
    "id": 2,
    "name": "Herramientas Eléctricas"
  }
]
```

---

### 2️⃣ Crear categoría SIN ADMIN (Debe fallar - 403 Forbidden)

**URL:** `POST http://localhost:8080/categories`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {TOKEN_USER_NO_ADMIN}
```

**Body:**
```json
{
  "name": "Herramientas de Precisión"
}
```

**Respuesta esperada (403 FORBIDDEN):**
```json
{
  "error": "Access Denied",
  "message": "Acceso denegado"
}
```

---

### 3️⃣ Crear categoría CON ADMIN (Debe exitoso - 201 Created)

**URL:** `POST http://localhost:8080/categories`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {TOKEN_ADMIN}
```

**Body:**
```json
{
  "name": "Herramientas de Precisión"
}
```

**Respuesta esperada (201 CREATED):**
```json
{
  "id": 3,
  "name": "Herramientas de Precisión"
}
```

---

### 4️⃣ Listar nuevamente (GET - Verificar que se creó)

**URL:** `GET http://localhost:8080/categories`

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Herramientas Manuales"
  },
  {
    "id": 2,
    "name": "Herramientas Eléctricas"
  },
  {
    "id": 3,
    "name": "Herramientas de Precisión"
  }
]
```

---

## Pasos de Validación en Postman

| # | Paso | Esperado | Estado |
|---|------|----------|--------|
| 1 | GET /categories sin token | ✅ Lista todas (200) | [ ] |
| 2 | POST /categories con USER token | ❌ 403 Forbidden | [ ] |
| 3 | POST /categories con ADMIN token | ✅ 201 Created | [ ] |
| 4 | GET /categories después de crear | ✅ Incluye nueva categoría (200) | [ ] |
| 5 | GET /categories/{id} sin token | ✅ Devuelve la categoría (200) | [ ] |

---

**Nota:** Para obtener tokens, primero debes autenticarte en `/auth/login` con credenciales ADMIN.


