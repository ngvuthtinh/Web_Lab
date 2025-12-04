# Product Management System

## Student Information
- **Name:** Nguyễn Vũ Thành Tính
- **Student ID:** ITCSIU23039
- **Class:** Monday Morning

## Technologies Used
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL 8.0
- Thymeleaf
- Maven
- Apache POI (Excel Export)

## Setup Instructions
1. Import project into VS Code.
2. Create database: `product_management`.
3. Update `application.properties` with your MySQL credentials.
4. Run: `mvn spring-boot:run` (or use the Run button in VS Code).
5. Open browser: http://localhost:8080/products

## Completed Features
- [x] CRUD operations (Create, Read, Update, Delete)
- [x] Simple Search functionality
- [x] Advanced search with multiple criteria (Name, Category, Price Range)
- [x] Validation (Input checks with error messages)
- [x] Sorting (Sort by ID, Name, Price, Code)
- [x] Pagination (View products page by page)
- [x] Statistics Dashboard (Charts, Inventory Value, Low Stock Alerts)
- [x] **Bonus 1:** REST API Endpoints (JSON support)
- [x] **Bonus 2:** Image Upload (View & Upload product photos)
- [x] **Bonus 3:** Export Data to Excel

## Project Structure

src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── product_management
│   │               ├── config
│   │               │   └── MvcConfig.java              # Config for exposing upload directory
│   │               ├── controller
│   │               │   ├── DashboardController.java    # Handles Dashboard & Stats
│   │               │   ├── ExportController.java       # Handles Excel Export
│   │               │   ├── ProductController.java      # Main Web Controller (Thymeleaf)
│   │               │   └── ProductRestController.java  # REST API Controller
│   │               ├── entity
│   │               │   └── Product.java                # JPA Entity with Validation
│   │               ├── repository
│   │               │   └── ProductRepository.java      # Data Access & Custom Queries
│   │               ├── service
│   │               │   ├── ProductService.java         # Service Interface
│   │               │   └── ProductServiceImpl.java     # Business Logic Implementation
│   │               └── ProductManagementApplication.java
│   └── resources
│       ├── static                                  # CSS/JS files
│       ├── templates
│       │   ├── dashboard.html                      # Statistics View
│       │   ├── product-form.html                   # Create/Edit View
│       │   └── product-list.html                   # Main List View (Sort/Filter/Search)
│       └── application.properties                  # DB & App Configuration
├── uploads                                         # Stores uploaded product images
└── pom.xml                                         # Dependencies (Maven)


## Database Schema

| Column Name    | Data Type      | Constraints                       | Description                  |
| :---           | :---           | :---                              | :---                         |
| `id`           | `BIGINT`       | `PRIMARY KEY`, `AUTO_INCREMENT`   | Unique ID for each product   |
| `product_code` | `VARCHAR(20)`  | `UNIQUE`, `NOT NULL`              | Product Code (e.g., P001)    |
| `name`         | `VARCHAR(100)` | `NOT NULL`                        | Name of the product          |
| `price`        | `DECIMAL(10,2)`| `NOT NULL`                        | Product price                |
| `quantity`     | `INT`          | `DEFAULT 0`                       | Stock quantity               |
| `category`     | `VARCHAR(50)`  | `NULLABLE`                        | Product category             |
| `description`  | `TEXT`         | `NULLABLE`                        | Detailed description         |
| `created_at`   | `TIMESTAMP`    | `DEFAULT CURRENT_TIMESTAMP`       | Date of creation             |

## Known Issues
- **Image Storage:** Images are stored locally in the `uploads/` folder within the project directory. They may be lost if the project folder is deleted or moved without including this folder.
- **Delete Constraint:** Deleting a product is permanent (Hard Delete). There is no "Trash" or "Soft Delete" feature yet.
- **Browser Caching:** Sometimes uploaded images might not refresh immediately due to browser caching (requires hard refresh Ctrl+F5).

## Time Spent
Approximately 12 hours (Including core exercises and 3 bonus tasks).