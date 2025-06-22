
# 📦 Digital Time Capsule

A full-stack web application that lets users write, store, and share time-locked messages with optional images or videos — to be delivered or revealed in the future. Users can create memories, schedule automatic delivery, and even receive email notifications when messages unlock!

---

## 🚀 Features

- 🔒 **JWT Authentication** (Login / Register / Secure API)
- 📝 **Create & Edit Messages** with:
  - Rich text
  - Images/videos (optional)
  - Delivery & unlock dates
- 📬 **Scheduled Email Delivery** on delivery date
- 📩 **Email Notification** when messages unlock
- 🌐 **Sharable Links** for public viewing
- 📤 **Media Upload Support** with previews
- 🗂️ **Filter, Sort, Update & Delete Messages**
- 🔐 Messages remain **hidden until unlocked**
- 🌈 **Responsive UI** with dark theme and modern design
- 🔔 Toast alerts, loading states, and user-friendly interactions

---

## 🛠️ Tech Stack

| Frontend                | Backend                | Others                 |
|------------------------|------------------------|------------------------|
| React + Vite           | Spring Boot (Java)     | JWT (Auth)             |
| React Router DOM       | MySQL (JPA/Hibernate)  | Email Scheduler        |
| Axios (private client) | Spring Security        | CSS Modules            |
| CSS Modules (Dark UI)  | Multipart Upload       | Postman Tested         |

---

## 🔧 Setup Instructions

### 1. Clone the repo

git clone https://github.com/PrathamSingh31/digital-time-capsule.git
cd digital-time-capsule

### 2. Backend Setup (Spring Boot)
cd backend
# Set up your DB credentials in application.properties
# Run using IDE or:
./mvnw spring-boot:run
Ensure MySQL is running and schema exists.

### 3. Frontend Setup (React + Vite)
cd frontend
npm install
npm run dev

### 📬 Email & Delivery Features
Emails sent via EmailService
Messages unlock based on delivery and unlock date
Uses Spring @Scheduled task to check every 24 hours

### 🔐 Authentication
Register/Login using JWT token-based system
Frontend uses axiosPrivate with token header
Secured backend routes 

### 📁 Folder Structure

digital-time-capsule/
├── backend/
│   └── src/main/java/com/capsule/...
├── frontend/
│   └── src/components/
│       └── Dashboard.jsx, Create.jsx, etc.
├── README.md

### 📄 License
MIT License © [Pratham Singh]

🙋‍♂️ Author
Pratham Singh

Let’s connect on [ prathamsingh31] (https://www.linkedin.com/in/prathamsingh31/)
GitHub: PrathamSingh31
