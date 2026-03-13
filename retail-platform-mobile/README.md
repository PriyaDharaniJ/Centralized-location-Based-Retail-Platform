# 🛒 RetailHub — Centralized Location-Based Retail Platform
**Dindigul District, Tamil Nadu**

## Tech Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript, Leaflet.js (OpenStreetMap), STOMP/WebSocket
- **Backend**: Spring Boot 3.2, Spring Security (JWT), Spring WebSocket
- **Database**: PostgreSQL 15+

---

## 🚀 Quick Start

### Step 1: Database Setup
```bash
# Start PostgreSQL and create database
psql -U postgres
CREATE DATABASE retailplatform;
\q

# Run schema
psql -U postgres -d retailplatform -f database/schema.sql
```

### Step 2: Backend Setup
```bash
cd backend

# Configure DB password in src/main/resources/application.properties
# Change: spring.datasource.password=YOUR_PASSWORD

# Build and run
mvn clean install -DskipTests
mvn spring-boot:run
# Backend starts at http://localhost:8080
```

### Step 3: Frontend
```bash
# Just open the HTML files in browser or use Live Server extension
# User Panel:    frontend/user/index.html
# Retailer:      frontend/retailer/index.html
# Admin Panel:   frontend/admin/index.html
```

---

## 🔑 Default Credentials

| Panel | Email | Password |
|-------|-------|----------|
| Admin | admin@retailplatform.com | admin123 |
| Retailer (Demo) | rajan@demo.com | admin123 |
| Retailer (Demo) | priya@demo.com | admin123 |
| Customer (Demo) | user@demo.com | admin123 |

---

## 📁 Project Structure

```
retail-platform/
├── database/
│   └── schema.sql              # PostgreSQL schema + seed data
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/retailplatform/
│       ├── entity/             # JPA entities
│       ├── repository/         # Spring Data repos
│       ├── service/            # Business logic
│       ├── controller/         # REST endpoints
│       ├── security/           # JWT filter
│       ├── config/             # Security, WebSocket, CORS
│       └── util/               # JWT, Haversine, TrackingID
└── frontend/
    ├── shared/api.js           # Shared API utility
    ├── user/index.html         # Customer panel
    ├── retailer/index.html     # Shop owner dashboard
    └── admin/index.html        # Admin dashboard
```

---

## 🗺️ Key Features

### User Panel
- 📍 GPS location detection
- 🔍 Search products/shops
- 🗺️ Map view of nearby shops (OpenStreetMap + Leaflet)
- 📏 Radius filter: 1km, 2km, 5km, 10km, All
- 🛒 Add to cart, wishlist
- 📋 Place orders (Delivery/Pickup)
- 🔎 Order tracking with tracking ID
- 🔔 Real-time notifications (WebSocket)
- ↕️ Sort by price, rating, discount

### Retailer Panel
- 📝 Shop registration (admin approval required)
- 📦 Product management (add/edit/delete)
- 🟢 Toggle Open/Closed & Delivery status
- 📋 Order management with status updates
- 🔔 Real-time order notifications

### Admin Panel
- ✅ Approve/reject retailer registrations
- 📊 Platform-wide statistics
- 💰 Revenue & commission tracking per retailer
- 👥 All users & retailers overview

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/login | Login |
| POST | /api/auth/register/user | User register |
| POST | /api/auth/register/retailer | Retailer register |
| GET | /api/shops/nearby?lat=&lon=&radius= | Nearby shops |
| GET | /api/products/search?q=&lat=&lon= | Search products |
| POST | /api/user/orders | Place order |
| GET | /api/track/{trackingId} | Track order |
| GET | /api/admin/dashboard | Admin stats |
| POST | /api/admin/retailers/{id}/approve | Approve retailer |

---

## 📍 Location
Dindigul District, Tamil Nadu (Default coordinates: 10.3673, 77.9803)
Uses **Haversine formula** for accurate distance calculation.
Map powered by **OpenStreetMap + Leaflet.js** (no API key required).

---
