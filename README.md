# 🚗 Car Sharing App API

A backend application for managing a car sharing service, built with Java and Spring Boot. It provides full CRUD operations for cars, users, rentals, and payments, along with JWT-based authentication and role-based access control.

---

🎯 **Project Overview**  
This application serves as the backend for a car sharing platform, supporting two roles:

• **Customer**
- Register / Login
- Browse available cars
- Rent cars
- View rental history
- Make payments for rentals and fines

• **Manager**
- Manage cars (add, update, delete)
- Update user roles
- Monitor rentals and payments

The system is structured around 4 core domain models:  
`Car`, `User`, `Rental`, `Payment`

---

🚀 **Technologies & Tools**

| Tool               | Description                      |
|--------------------|----------------------------------|
| Java 21            | Core language                    |
| Spring Boot 3.4.1  | Main framework                   |
| Spring Security    | Authentication & authorization   |
| Spring Data JPA    | Data persistence                 |
| JWT                | Token-based authentication       |
| MapStruct          | DTO mapping                      |
| Liquibase          | DB schema management             |
| Stripe-java        | Stripe API integration for payments |
| Telegram API       | Notifications service             |
| Docker & Docker Compose | Containerization and orchestration |
| Maven              | Build automation                 |
| Checkstyle         | Code quality and style checks    |
| Swagger            | API documentation                |

---

🔌 **API Endpoints Overview**

### 🔐 Authentication
- `POST /api/register` – Register a new user
- `POST /api/login` – Login and receive JWT tokens

### 👥 Users
- `PUT /api/users/{id}/role` – Update user role (Manager only)
- `GET /api/users/me` – Get logged-in user profile
- `PUT/PATCH /api/users/me` – Update profile info

### 🚗 Cars
- `POST /api/cars` – Add a new car (Manager only)
- `GET /api/cars` – List all cars (public)
- `GET /api/cars/{id}` – Get detailed car information
- `PUT/PATCH /api/cars/{id}` – Update car details (Manager only)
- `DELETE /api/cars/{id}` – Delete a car (Manager only)

### 🚘 Rentals
- `POST /api/rentals` – Create a new rental (decreases car inventory by 1)
- `GET /api/rentals` – List rentals with optional filters (`user_id`, `is_active`)
- `GET /api/rentals/{id}` – Get rental details
- `POST /api/rentals/{id}/return` – Return rental (sets actual return date and increases inventory)

### 💳 Payments
- `GET /api/payments` – List payments (Customers see only theirs; Managers see all)
- `POST /api/payments` – Create Stripe payment session for a rental
- `GET /api/payments/success` – Stripe payment success callback
- `GET /api/payments/cancel` – Stripe payment cancellation callback

---

🌐 **Live Demo**

🧪 Swagger UI after starting the application locally:  
http://localhost:8080/swagger-ui.html

📹 YouTube Demo:  
x

---

🔐 **Default Users**

| Role    | Email              | Password      |
|---------|--------------------|---------------|
| Admin   | admin@example.com   | adminExample  |
| User    | user@example.com    | userExample   |

These accounts are automatically created on application startup via `DataInitializer`.

---

⚙️ **Setup Instructions**

✅ **Prerequisites**
- Java 17+
- Docker & Docker Compose
- Maven

🧪 **Clone & Run Locally**

```bash
# Clone repository
git clone https://github.com/your-org/car-sharing-app.git

# Build application
./mvnw clean package

# Run with Docker Compose
docker compose up
