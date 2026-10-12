# ⛽ PETROFLOW — Petrol Station Management & Automation System

PetroFlow is a full-stack web application that simulates and manages a real-world petrol station. It automates fuel dispensing, inventory tracking, sales logging, and invoice generation.

---

## 🌟 What Does This Project Do?

In a real petrol station, when a customer buys fuel:
1. The **Pump Dispenser** meters the fuel.
2. The **Tank Inventory** reduces by that exact quantity.
3. A **Sale Record** is created.
4. An **Invoice / Bill** is printed for the customer.
5. The **Dashboard** updates today's sales and revenue.

**PetroFlow automates this entire lifecycle** using Java Spring Boot on the backend and an interactive digital dispenser dashboard on the frontend.

---

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot 3, Spring Data JPA, Hibernate
- **Database:** MySQL 8 *(built-in automatic fallback storage included for instant setup)*
- **Frontend:** HTML5, CSS3, Vanilla JavaScript, Chart.js
- **Build Tool:** Maven

---

## 🚀 Quick Start (Run in 2 Minutes)

### 1. Prerequisites
- Java 21 or higher installed
- Maven installed

### 2. Run the Application
Open your terminal in the project folder:
```bash
mvn spring-boot:run
```

### 3. Open in Browser
Visit: **[http://localhost:8080](http://localhost:8080)**

*(Sample fuels, pumps, tanks, and staff are seeded automatically on first run!)*

---

## 🎮 How to Test the Pump Simulator (Demo Walkthrough)

1. Open **[http://localhost:8080](http://localhost:8080)**.
2. Go to **Pump Simulator** in the sidebar.
3. Select **PUMP-01 (Petrol)**.
4. Enter quantity: `20` Litres.
5. Notice the system calculates the price: `20 × ₹108.50 = ₹2,170.00`.
6. Click **START** — watch the live fuel flow animation and LED meter progress.
7. Click **COMPLETE TRANSACTION**:
   - Petrol stock drops from **1000 L → 980 L**.
   - Transaction **`TXN-2026-000001`** is recorded.
   - An **Official Printable Invoice (`INV-2026-000001`)** pops up.
8. Click **Dashboard** — see **Today's Sales (+1)** and **Today's Revenue (+₹2,170)** updated live!

---

## 🔑 Key Features

- **Automated Pump Simulator:** Interactive digital dispenser with real-time LED meters and fueling animations.
- **Hidden Database IDs:** Shows user-friendly business numbers (`TXN-2026-000001`, `INV-2026-000001`, `EMP-001`, `PUMP-01`) instead of raw database primary keys (`id=1`).
- **Never Sells Out of Stock:** Uses database-level row locking (`SELECT ... FOR UPDATE`) so multiple pumps cannot dispense more fuel than is physically in the tank.
- **Low Stock Warnings:** Automatically alerts the station supervisor when fuel in any tank drops below safe levels.
- **Printable Invoices:** Instant tax invoice generator with clean print layout.
- **Forecourt Dashboard & Charts:** Real-time visual graphs for daily revenue trends and fuel share percentages.

---

## 📁 Project Structure

```text
petroflow/
├── src/main/java/com/petroflow/
│   ├── controller/     # REST API Endpoints (/api/dashboard, /api/pumps, /api/sales, etc.)
│   ├── service/        # Core Business Logic (Atomic dispensing, ID generators)
│   ├── repository/     # Database queries and row-locking logic
│   ├── entity/         # Database models (Fuel, Pump, Inventory, Sale, Invoice)
│   ├── dto/            # Safe Data Transfer Objects
│   └── exception/      # Global error handlers
├── src/main/resources/
│   ├── static/         # Frontend (index.html, CSS styles, JavaScript modules)
│   └── application.properties # App configuration
└── pom.xml             # Maven dependencies
```

---

## 📡 Main REST API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/dashboard` | Returns today's sales, revenue, tank levels, and charts |
| `POST` | `/api/pump-transactions/dispense` | Dispenses fuel, deducts stock, creates sale & invoice |
| `GET` | `/api/pumps` | Lists all forecourt fuel dispensers |
| `GET` | `/api/inventory` | Lists underground fuel tank levels and warnings |
| `POST` | `/api/inventory/adjust` | Restocks an underground tank |
| `GET` | `/api/sales` | Returns sales transaction ledger |
| `GET` | `/api/invoices/{invNo}` | Fetches detailed invoice by invoice number |

---

## 💡 Quick Interview Talking Points

1. **Transaction Atomicity (`@Transactional`):** If any step fails during billing, the fuel deduction, sale record, and invoice generation all roll back together to maintain data consistency.
2. **Concurrency Safety:** Protected against race conditions using JPA Pessimistic Write Locking so two pumps dispensing fuel at the same time cannot make inventory negative.
3. **Domain-Driven Identifiers:** Exposes realistic reference codes (`TXN-2026-000001`, `PUMP-01`) instead of leaking database IDs (`id=1`).

---

## 📄 License
Open source under the [MIT License](LICENSE).
