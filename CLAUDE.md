# CLAUDE.md — rotaract-bar-service

This file provides AI assistants with the context needed to navigate and contribute to this codebase effectively.

---

## Project Overview

`rotaract-bar-service` is a Spring Boot REST API for managing bar orders at a Rotaract event (Altstadtfest Ansbach). It handles products (drinks), ingredients, and purchase orders with automatic data seeding on startup.

**Tech stack:**
- Java 21 + Spring Boot 3.5.3
- PostgreSQL 15 (via Docker)
- Spring Data JPA / Hibernate
- MapStruct 1.6.3 (entity ↔ DTO mapping)
- OpenAPI 3.0 (spec-first, code generation)
- Maven (wrapper included)
- Bruno (API test scripts)

---

## Repository Structure

```
rotaract-bar-service/
├── api/
│   ├── rotaract-bar.openapi.yaml      # OpenAPI 3.0 spec (source of truth for API)
│   └── test/                          # Bruno API test scripts (.bru files)
├── src/
│   ├── main/
│   │   ├── java/com/lichius/rac/ansbach/altstadtfest/
│   │   │   ├── Application.java       # Spring Boot entry point
│   │   │   ├── application/
│   │   │   │   ├── controller/        # REST controllers
│   │   │   │   ├── service/           # Business logic + data initialization
│   │   │   │   ├── model/             # JPA entities
│   │   │   │   └── repository/        # Spring Data repositories
│   │   │   └── infrastructure/
│   │   │       └── mapper/            # MapStruct mapper interfaces
│   │   └── resources/
│   │       └── application.yaml       # App configuration
│   └── test/
│       └── java/.../AltstadtfestApplicationTests.java
├── docker-compose.yml                 # PostgreSQL 15 database
├── pom.xml                            # Maven config + plugin declarations
└── mvnw / mvnw.cmd                    # Maven wrapper scripts
```

---

## Architecture

This project uses a **layered architecture**:

```
HTTP Request
    ↓
Controller (application/controller/)
    ↓
Service (application/service/)
    ↓
Repository (application/repository/)
    ↓
PostgreSQL Database
```

**DTOs vs. Entities:**
- JPA entities live in `application/model/`
- DTOs are **generated from the OpenAPI spec** into `target/generated-sources/openapi/gen/main/java/rotaract/bar/infrastructure/api/controller/model/` with a `Dto` suffix
- MapStruct mappers in `infrastructure/mapper/` handle the conversion

**Spec-first API design:** The `api/rotaract-bar.openapi.yaml` file is the single source of truth. Editing the OpenAPI spec and regenerating is the correct way to add or modify API contracts.

---

## Domain Model

### Entities

| Entity | Table | Key Relationships |
|---|---|---|
| `Product` | `product` | ManyToMany → `Ingredient` (join: `product_ingredients`) |
| `Ingredient` | `ingredients` | — |
| `PurchaseOrder` | `purchase_orders` | OneToMany → `OrderedItem` (cascade all, orphan removal) |
| `OrderedItem` | `ordered_items` | ManyToOne → `PurchaseOrder`, ManyToOne → `Product` (lazy) |

### Product Categories (enum)
- `DRINKS`
- `BEER_WINE_NONALC`
- `SHOTS`

### Auto-Seeded Data
On every startup, `@PostConstruct` methods initialize the database:
- **28 default ingredients** (spirits, mixers, garnishes, beer types) — `IngredientService.initializeIngredients()`
- **16 default products** with prices €2.00–€7.00 — `ProductService.initializeProducts()`
- `ProductService` has `@DependsOn("ingredientService")` to ensure ingredients are seeded first

---

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/orders` | List all purchase orders |
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders/{id}` | Get a specific order |
| `GET` | `/api/orders/hi` | Health check (returns "Hello!") |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/ingredients` | List all ingredients |

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI JSON: `http://localhost:8080/api-docs`

---

## Development Setup

### Prerequisites
- Java 21
- Docker + Docker Compose
- Maven (or use the included `./mvnw` wrapper)

### Starting the database
```bash
docker-compose up -d
```

### Running the application
```bash
./mvnw spring-boot:run
```

### Building
```bash
./mvnw clean package
```

The OpenAPI code generation runs automatically during the `generate-sources` phase (before `compile`). Generated code appears in `target/generated-sources/`.

---

## Database Configuration

| Setting | Value |
|---|---|
| Host | `localhost:5432` |
| Database | `rotaract_bar` |
| Username | `baruser` |
| Password | `sichael` |
| DDL auto | `update` (schema auto-updated on startup) |
| SQL logging | enabled |

Configured in `src/main/resources/application.yaml`. Credentials are hardcoded for local dev only.

---

## Code Generation (OpenAPI)

The `openapi-generator-maven-plugin` (v7.14.0) generates:
- **Controller interfaces** into package `rotaract.bar.infrastructure.api.controller`
- **DTO model classes** into package `rotaract.bar.infrastructure.api.controller.model` (with `Dto` suffix)

Plugin configuration in `pom.xml`:
- Input: `api/rotaract-bar.openapi.yaml`
- Generator: `spring` (server-side)
- Mode: **interface-only** (no default implementations generated)
- Spring Boot 3 mode enabled

**Workflow for API changes:**
1. Edit `api/rotaract-bar.openapi.yaml`
2. Run `./mvnw generate-sources` (or `./mvnw compile`)
3. Implement or update the controller class to satisfy the regenerated interface

---

## Naming Conventions

| Concept | Convention | Example |
|---|---|---|
| Classes | PascalCase | `PurchaseOrderService` |
| Methods | camelCase | `createPurchaseOrder()` |
| Packages | lowercase, descriptive | `application.controller` |
| Database tables | snake_case (explicit via `@Table`) | `purchase_orders` |
| DTOs | `<Name>Dto` suffix | `PurchaseOrderDto` |
| Mappers | `<Name>Mapper` suffix | `PurchaseOrderMapper` |

Base package: `com.lichius.rac.ansbach.altstadtfest`

---

## MapStruct Mapper Conventions

Mappers are interfaces in `infrastructure/mapper/` annotated with `@Mapper(componentModel = "spring")`.

Key mappings to be aware of:
- `PurchaseOrderMapper`: converts `LocalDateTime orderedAt` → `OffsetDateTime` (UTC)
- `ProductMapper`: maps `List<Ingredient> ingredients` → `List<Long> ingredientIds`
- `OrderedItemMapper`: maps `product.id` → `productId`

Mappers are composed (e.g., `PurchaseOrderMapper` uses `OrderedItemMapper` and `ProductMapper`).

---

## Testing

### Unit / Integration Tests
- Location: `src/test/java/com/lichius/rac/ansbach/altstadtfest/`
- Only one test exists: `AltstadtfestApplicationTests.contextLoads()` — verifies Spring context starts
- Run with: `./mvnw test`

### API Tests (Bruno)
- Location: `api/test/`
- Requires Bruno client
- Configured to hit `localhost:8080/api/orders`
- Scripts cover: GET orders, POST order, GET by ID, GET products, GET by item name

---

## Key Patterns & Guidelines for AI Assistants

1. **Edit the OpenAPI spec first** when changing API contracts; don't modify generated code directly.
2. **MapStruct handles DTO conversion** — don't write manual mapping code.
3. **Data seeding is in services** via `@PostConstruct`; use `findByName()` guards to avoid duplicates.
4. **Repositories are Spring Data JPA** — prefer derived query method names over native queries.
5. **Cascade all + orphan removal** on `PurchaseOrder.items` means adding/removing items via the order entity is correct; do not manipulate `OrderedItem` independently.
6. **`@DependsOn`** ordering matters for `@PostConstruct` seeding — `IngredientService` must init before `ProductService`.
7. **Dependency injection** uses field injection (`@Autowired`) in some places and constructor injection in others — prefer constructor injection for new code.
8. **The `update` DDL strategy** means schema is auto-updated; no Liquibase/Flyway migration files exist.
9. **Some code is in German** (comments, warning messages like "WARNUNG: Product nicht gefunden") — this is intentional and consistent with the team's language.
10. **Test coverage is minimal** — when adding new features, consider adding at least a basic integration test.
