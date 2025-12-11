# Customer API Documentation

## Base URL
`http://localhost:8080/api/customers`

## Endpoints (Task 8.2)

### 1. Get All Customers
**GET** `/api/customers`

**Response:** 200 OK
```json
{
    "customers": [
        {
            "id": 1,
            "customerCode": "C001",
            "fullName": "John Doe",
            "email": "john.doe@example.com",
            "phone": "+1-555-0101",
            "address": "123 Main St, New York, NY 10001",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T10:00:00"
        }
    ],
    "currentPage": 0,
    "totalItems": 1,
    "totalPages": 1
}

### 2. Get Customer by ID
**GET** `/api/customers/{id}`

**Response:** 200 OK
```json
{
    "customers": [
        {
            "id": 1,
            "customerCode": "C001",
            "fullName": "John Doe",
            "email": "john.doe@example.com",
            "phone": "+1-555-0101",
            "address": "123 Main St, New York, NY 10001",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T10:00:00"
        }  
}

### 3. Error Responses
**GET** `/api/customers/{id}`

**Response:** 404 Not Found
```json
{
    "timestamp": "2025-12-12T10:15:00",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 99",
    "path": "/api/customers/99",
    "details": null
}

#### Task 8.3: Add Examples for Each Status Code

**Response:** 200 OK
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-0101",
    "address": "123 Main St, New York, NY 10001",
    "status": "ACTIVE",
    "createdAt": "2025-12-12T10:00:00"
}

**Response:** 201 Created
```json
{
    "id": 2,
    "customerCode": "C002",
    "fullName": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+1-555-0102",
    "address": "456 Oak Ave, Los Angeles, CA 90001",
    "status": "ACTIVE",
    "createdAt": "2025-12-12T10:05:00"
}

**Response:** 400 Bad Request (Validation)
```json
{
    "timestamp": "2025-12-12T10:10:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input data",
    "details": [
        "email: Invalid email format",
        "customerCode: Customer code must start with C"
    ]
}

**Response:** 409 Conflict (Duplicate)
```json
{
    "timestamp": "2025-12-12T10:20:00",
    "status": 409,
    "error": "Conflict",
    "message": "Email already exists: john.doe@example.com",
    "path": "/api/customers"
}

**Response:** 500 Internal Server Error
```json
{
    "timestamp": "2025-12-12T10:25:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Unexpected error occurred",
    "path": "/api/customers"
}

