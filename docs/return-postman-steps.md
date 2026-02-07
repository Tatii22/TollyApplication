# Pasos para probar Returns en Postman

> Estos pasos asumen que la app esta corriendo en `http://localhost:8080` y la base de datos esta limpia.

## 1) Login ADMIN
**POST** `/auth/login`

```json
{ "email": "admintoll@example.com", "password": "password" }
```

Guarda el `accessToken` como `ADMIN_TOKEN`.

## 2) Crear CLIENTE (admin)
**POST** `/admin/users/clients`

Headers:
- `Authorization: Bearer ADMIN_TOKEN`
- `Content-Type: application/json`

```json
{
  "email": "cliente@correo.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Perez",
  "address": "Calle 123",
  "document": "1234567890",
  "phone": "3101234567"
}
```

## 3) Crear SUPPLIER (admin)
**POST** `/admin/users/suppliers`

Headers:
- `Authorization: Bearer ADMIN_TOKEN`
- `Content-Type: application/json`

```json
{
  "email": "proveedor@empresa.com",
  "password": "password123",
  "phone": "3142875690",
  "company": "MiCompania",
  "identification": "9876543210",
  "contactName": "Laura Perez"
}
```

## 4) Crear CATEGORY (admin o supplier)
**POST** `/categories`

Headers:
- `Authorization: Bearer ADMIN_TOKEN`
- `Content-Type: application/json`

```json
{ "name": "Electricas" }
```

## 5) Crear TOOL STATUS (admin o supplier)
**POST** `/tool-statuses`

Headers:
- `Authorization: Bearer ADMIN_TOKEN`
- `Content-Type: application/json`

```json
{ "name": "AVAILABLE" }
```

## 6) Login CLIENT
**POST** `/auth/login`

```json
{ "email": "cliente@correo.com", "password": "password123" }
```

Guarda el `accessToken` como `CLIENT_TOKEN`.

## 7) Crear RESERVA (cliente)
**POST** `/api/reservations`

Headers:
- `Authorization: Bearer CLIENT_TOKEN`
- `Content-Type: application/json`

```json
{
  "clientId": 1,
  "startDate": "2026-02-01",
  "endDate": "2026-02-03",
  "totalPrice": 51.00,
  "statusName": "IN_PROGRESS"
}
```

## 8) Crear RETURN STATUS (admin)
**POST** `/return-statuses`

Headers:
- `Authorization: Bearer ADMIN_TOKEN`
- `Content-Type: application/json`

```json
{ "name": "PENDING" }
```

## 9) Crear RETURN (cliente)
**POST** `/returns`

Headers:
- `Authorization: Bearer CLIENT_TOKEN`
- `Content-Type: application/json`

```json
{
  "reservationId": 1,
  "returnDate": "2026-02-03",
  "details": [
    { "toolId": 1, "quantity": 1, "observations": "Sin novedades" }
  ],
  "observations": "OK"
}
```

## 10) Ver RETURNS (cliente)
**GET** `/returns`

Headers:
- `Authorization: Bearer CLIENT_TOKEN`

---

Notas:
- El `clientId` se toma del token, no se envia en el body.
- Si el `reservationId` o `toolId` son distintos en tu BD, usa los que te devuelva la API.
- El telefono debe tener 10 digitos y empezar por 3.
