# Employee API

Spring Boot 3 / Java 17 REST API. Employees live in a static in-memory collection (no database).

## Run / test
    mvn spring-boot:run
    mvn test

## Endpoints
| Method | Path                | Success        | Errors   |
|--------|---------------------|----------------|----------|
| GET    | /api/employees      | 200            |          |
| GET    | /api/employees/{id} | 200            | 400, 404 |
| POST   | /api/employees      | 201 + Location | 400, 409 |
| DELETE | /api/employees/{id} | 204            | 400, 404 |

## Example
    curl -X POST localhost:8080/api/employees -H 'Content-Type: application/json' \
      -d '{"firstName":"FnameTest","lastName":"LnameTest","email":"flname@acme.com","department":"Engineering"}'

## Design
- controller -> service (interface + impl) -> repository (interface + in-memory impl); constructor injection, storage is swappable.
- `EmployeeRequest` DTO with Bean Validation, separate from the immutable `Employee` record.
- `GlobalExceptionHandler` maps exceptions to RFC 7807 problem responses (400/404/409/500).
- Tests: Mockito service tests and `@WebMvcTest` controller tests.
