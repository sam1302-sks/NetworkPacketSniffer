# ✈️ AeroManage - Airplane Management System DBMS  

<p align="center">
  <img src="https://images.unsplash.com/photo-1544091466-859de6c66e58?q=80&w=2070&auto=format&fit=crop" alt="Airplane wing against a sunset sky" width="800"/>
</p>

<p align="center">
  <em>
    A comprehensive Database Management System for managing all core operations of a modern airline.  
    This project showcases advanced database design, complex querying, and data integrity enforcement using MySQL.
  </em>
</p>

---

## 📋 Table of Contents  
- [About The Project](#about-the-project)  
- [Entity-Relationship (ER) Diagram](#entity-relationship-er-diagram)  
- [Core Features](#core-features)  
- [Tech Stack](#tech-stack)  
- [Project Setup](#project-setup)  
- [Example Queries](#example-queries)  

---

## 📖 About The Project  

**AeroManage** is a robust back-end system designed to handle the intricate data relationships within an airline's operations.  
From tracking passengers and booking tickets to managing flight schedules, crew assignments, and aircraft maintenance, this project serves as a **centralized data hub**.  

The primary goal is to demonstrate a **normalized, efficient, and reliable database schema** that can support an airline's critical daily functions.  

---

## 📊 Entity-Relationship (ER) Diagram  

The entire database is structured around the following schema, ensuring **data normalization up to the Third Normal Form (3NF)** to minimize redundancy and enhance data integrity.  

📌 *[Insert your ER Diagram image here]*  

---

## ✨ Core Features  

- 👨‍✈️ **Passenger & Ticket Management** – Manage passenger information and link them to flights via tickets.  
- 🛫 **Flight & Route Scheduling** – Track flights, routes, and status updates across terminals.  
- 🛩️ **Aircraft & Maintenance Tracking** – Monitor fleet details, capacity, and complete maintenance logs.  
- 👩‍💼 **Crew Assignment** – Assign pilots, copilots, and cabin crew to specific flights.  
- 🔐 **Data Integrity** – Enforced with `PRIMARY KEY`, `FOREIGN KEY`, `CHECK`, and `NOT NULL` constraints.  
- ⚙️ **Advanced Operations** – Supports `VIEW`s, `STORED PROCEDURE`s, and `TRIGGER`s for business logic.  

---

## 🛠️ Tech Stack  

<p align="left">
  <a href="https://www.mysql.com/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original-wordmark.svg" alt="MySQL Logo" width="60" height="60"/>
  </a>  
  <em style="margin-left: 10px; font-size: 1.1rem; color: #555;">MySQL for database management and querying</em>
</p>  

---

## 🚀 Project Setup  

To set up **AeroManage** locally, follow these steps:  

### 🔧 Clone the Repository  
```bash
git clone https://github.com/your_username/your_repository.git
cd your_repository
