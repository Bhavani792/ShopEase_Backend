# ShopEase Backend

## Overview

ShopEase is an e-commerce backend application developed using Java, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, and MySQL.

I built this project as a learning and practice project to understand the basics of backend development and how different parts of a real-world application work together. Through this project, I learned about REST APIs, authentication, authorization, database integration, product management, shopping cart management, checkout, order processing, pagination, search, filtering, sorting, and role-based access control.

The project helped me understand how backend layers such as controllers, services, repositories, entities, security, and database operations work together.

The application provides APIs for user registration, login, product management, cart operations, checkout, orders, and admin order management.

## Technologies Used

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- REST APIs
- Swagger / OpenAPI
- Postman
- Docker
- Docker Compose
- Git
- GitHub
- GitHub Actions
- Eclipse

## Features

- User Registration
- User Login
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization
- USER and ADMIN roles
- Product Management
- Product Search
- Category Filtering
- Price Sorting
- Pagination
- Shopping Cart
- Stock Validation
- Checkout
- Order Management
- Order Cancellation
- Admin Order Management
- Order Status Management
- Global Exception Handling
- Swagger API Documentation

## Project Structure

```text
src/main/java/com/shopease
│
├── config
│   ├── DataInitializer.java
│   └── SecurityConfig.java
│
├── controller
│   ├── AdminOrderController.java
│   ├── AuthController.java
│   ├── CartController.java
│   ├── OrderController.java
│   ├── ProductController.java
│   └── TestController.java
│
├── dto
│   ├── AuthResponse.java
│   ├── CartItemResponse.java
│   ├── LoginRequest.java
│   ├── OrderItemResponse.java
│   ├── OrderResponse.java
│   ├── OrderStatusRequest.java
│   ├── ProductRequest.java
│   ├── ProductResponse.java
│   ├── RegisterRequest.java
│   └── UserResponse.java
│
├── entity
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── OrderStatus.java
│   ├── Product.java
│   ├── Role.java
│   └── User.java
│
├── exception
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
├── repository
│   ├── CartItemRepository.java
│   ├── OrderItemRepository.java
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
│
├── security
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtService.java
│
└── service
    ├── AdminOrderService.java
    ├── AuthService.java
    ├── CartService.java
    ├── OrderService.java
    └── ProductService.java
```

## Authentication Flow

1. User registers with their details.
2. Password is encrypted using BCrypt.
3. User logs in using email and password.
4. Spring Security authenticates the user.
5. A JWT token is generated after successful login.
6. The client sends the JWT token with protected requests.
7. JwtAuthenticationFilter validates the token.
8. Spring Security checks the user's role.
9. The requested API is executed if authorization is successful.

```text
User
  ↓
Register
  ↓
Login
  ↓
JWT Token
  ↓
Authorization Header
  ↓
JWT Authentication Filter
  ↓
Spring Security
  ↓
Protected API
```

Authorization header:

```http
Authorization: Bearer JWT_TOKEN
```

## API Endpoints

### Authentication APIs

| Method | Endpoint |
|---|---|
| POST | `/auth/register` |
| POST | `/auth/login` |

### Product APIs

| Method | Endpoint |
|---|---|
| GET | `/products` |
| GET | `/products/{id}` |
| POST | `/products` |
| PUT | `/products/{id}` |
| DELETE | `/products/{id}` |

### Cart APIs

| Method | Endpoint |
|---|---|
| GET | `/cart` |
| POST | `/cart/add/{productId}` |
| PUT | `/cart/update/{cartItemId}` |
| DELETE | `/cart/remove/{cartItemId}` |

### Order APIs

| Method | Endpoint |
|---|---|
| POST | `/orders/checkout` |
| GET | `/orders` |
| GET | `/orders/{orderId}` |
| PUT | `/orders/{orderId}/cancel` |

### Admin APIs

| Method | Endpoint |
|---|---|
| GET | `/admin/orders` |
| PUT | `/admin/orders/{orderId}/status` |

## Product Features

The product API supports:

### Pagination

```http
GET /products?page=0&size=5
```

### Search

```http
GET /products?search=mouse
```

### Category Filtering

```http
GET /products?category=Electronics
```

### Price Sorting

```http
GET /products?sort=priceAsc
```

## Sample Output

### User Login

Request:

```json
{
  "email": "john@gmail.com",
  "password": "john123"
}
```

Response:

```json
{
  "token": "JWT_TOKEN_GENERATED"
}
```

### Get Products

Request:

```http
GET /products?page=0&size=5
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "name": "Wireless Headphones",
      "description": "Bluetooth wireless headphones with good sound quality",
      "price": 49.99,
      "stock": 20,
      "category": "Electronics",
      "imageUrl": "https://example.com/headphones.jpg"
    },
    {
      "id": 3,
      "name": "Gaming Mouse",
      "description": "Wireless gaming mouse",
      "price": 39.99,
      "stock": 50,
      "category": "Electronics",
      "imageUrl": "https://example.com/mouse.jpg"
    }
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 2,
  "size": 5,
  "totalElements": 2,
  "totalPages": 1
}
```

### Checkout

```http
POST /orders/checkout
Authorization: Bearer JWT_TOKEN
```

Checkout creates an order using the authenticated user's cart.

### Order Status

```text
PLACED
   ↓
CONFIRMED
   ↓
SHIPPED
   ↓
DELIVERED
```

An order can also be cancelled when permitted by the application logic.

## Database

MySQL is used as the database.

Database:

```text
shopease_db
```

Main entities:

```text
User
Product
CartItem
Order
OrderItem
```

Relationship:

```text
User
 ├── CartItem
 │      └── Product
 │
 └── Order
        └── OrderItem
               └── Product
```

## Database Configuration

Sensitive configuration is handled using environment variables.

### Local Development

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shopease_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000
```

### Docker

When running the backend with Docker Compose, the backend connects to the MySQL container using the Docker service name:

```properties
spring.datasource.url=jdbc:mysql://mysql:3306/shopease_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000
```

Real passwords and JWT secrets are not stored in the GitHub repository.

## Running the Backend

### Create Database

```sql
CREATE DATABASE shopease_db;
```

### Clone Repository

```bash
git clone https://github.com/bhavani792/ShopEase_Backend.git
```

### Open Project

```bash
cd ShopEase_Backend
```

### Run Application

Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8084
```

## Docker

The backend can also be built and run using Docker.

### Build Docker Image

```bash
docker build -t shopease-backend .
```

### Run with Docker Compose

```bash
docker compose up
```

Docker Compose runs:

```text
Spring Boot Backend
       ↓
MySQL Database
```

The Docker backend is exposed on:

```text
http://localhost:8085
```

The MySQL database runs inside the Docker network on:

```text
3306
```

Environment variables used by the Docker setup:

```text
DB_PASSWORD
JWT_SECRET
```

To stop the containers:

```bash
docker compose down
```

## Swagger

Swagger UI is available at:

```text
http://localhost:8084/swagger-ui/index.html
```

Swagger can be used to view and test the REST APIs.

## Postman

The APIs can also be tested using Postman.

Typical flow:

```text
Register
   ↓
Login
   ↓
Copy JWT Token
   ↓
Bearer Token
   ↓
Test Protected APIs
```

## CI / GitHub Actions

GitHub Actions is used to automatically verify the backend project.

The CI workflow performs:

```text
Git Push
   ↓
GitHub Actions
   ↓
Setup Java 21
   ↓
Maven Build
   ↓
Docker Build
   ↓
Success
```

The workflow verifies that:

- The Spring Boot application builds successfully.
- The Docker image can be created successfully.

## Project Flow

```text
Client Request
      ↓
JWT Validation
      ↓
Controller Layer
      ↓
Service Layer
      ↓
Repository Layer
      ↓
MySQL Database
      ↓
Response Returned
```

## Security

The application uses:

- Spring Security
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization
- Protected REST APIs
- JWT Authentication Filter
- Environment Variables for Sensitive Configuration

## Exception Handling

A global exception handler is implemented using:

```text
GlobalExceptionHandler
```

It provides structured error responses for application-level exceptions.

## Frontend Integration

The ShopEase frontend is maintained in a separate repository.

Frontend:

https://github.com/bhavani792/ShopEase_Frontend

The frontend communicates with the backend using REST APIs and JWT authentication.

## Output

The backend was tested using Postman and Swagger.

Examples of tested functionality:

```text
✓ User Registration
✓ User Login
✓ JWT Token Generation
✓ Product Creation
✓ Product Listing
✓ Product Search
✓ Category Filtering
✓ Price Sorting
✓ Pagination
✓ Cart Operations
✓ Checkout
✓ Order Creation
✓ Order Listing
✓ Order Cancellation
✓ Admin Order Management
```

## What I Learned

- Understanding the basics of Spring Boot backend development
- Building REST APIs using Spring Boot
- Understanding controller, service, repository, and entity layers
- Implementing JWT authentication
- Implementing Spring Security
- Implementing role-based authorization
- Working with MySQL
- Working with Spring Data JPA
- Understanding Hibernate
- Creating entity relationships
- Implementing product CRUD operations
- Implementing search, filtering, sorting, and pagination
- Building shopping cart functionality
- Implementing checkout and order management
- Testing APIs using Postman
- Using Swagger/OpenAPI
- Containerizing applications using Docker
- Running multiple services using Docker Compose
- Creating CI workflows using GitHub Actions
- Managing projects using Git and GitHub
- Separating frontend and backend into independent repositories
- Protecting sensitive configuration using environment variables

## Future Improvements

- Online payment integration
- Product reviews and ratings
- Wishlist
- Coupons and discounts
- Email notifications
- Product image upload
- Automated testing
- Cloud deployment

## Author

**Bhavani**

GitHub:

https://github.com/bhavani792

## Repositories

### Backend

https://github.com/bhavani792/ShopEase_Backend

### Frontend

https://github.com/bhavani792/ShopEase_Frontend

## License

This project was developed for learning, practice, and portfolio purposes.
