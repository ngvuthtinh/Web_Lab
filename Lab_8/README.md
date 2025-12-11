# Customer API

## Student Information
- **Name:** Nguyễn Vũ Thành Tính
- **Student ID:** ITCSIU23039
- **Class:** Monday Morning

## API Endpoints

### Base URL
`http://localhost:8080/api/customers`

### Endpoints Implemented
- ✅ GET /api/customers - Get all customers
- ✅ GET /api/customers/{id} - Get by ID
- ✅ POST /api/customers - Create customer
- ✅ PUT /api/customers/{id} - Update customer
- ✅ DELETE /api/customers/{id} - Delete customer
- ✅ GET /api/customers/search?keyword={keyword} - Search
- ✅ GET /api/customers/status/{status} - Filter by status
- ✅ Pagination and sorting
- ✅ PATCH for partial update
- ✅ Bonus features (API Versioning, HATEOAS Links, Rate Limiting)

## How to Run
1. Create database: `customer_management`
2. Update `application.properties` with your MySQL credentials
3. Run: `mvn spring-boot:run`
4. Test: Open Thunder Client or Postman
5. Import collection: `Customer_API.postman_collection.json`

## Testing
All endpoints tested with Thunder Client.
See Report File for test results.

## Features Implemented
- DTO pattern for request/response
- Validation with @Valid
- Exception handling with @RestControllerAdvice
- Custom exceptions (404, 409)
- Proper HTTP status codes
- Search and filter
- Pagination
- Sorting
- API Versioning (v1, v2)
- HATEOAS (Hypermedia links)
- Rate Limiting (Bucket4j)

## Known Issues
- **No Authentication:** The API currently lacks security mechanisms (JWT/OAuth2), making it open to public access.
- **In-memory Rate Limiting:** Rate limiting data is stored in memory and will be reset upon application restart.
- **Hard Delete:** The delete operation permanently removes records from the database instead of using Soft Delete.
- **Phone Validation:** The regex for phone numbers only supports basic formats and may not cover all international numbers.

## Time Spent
Approximately 10 hours