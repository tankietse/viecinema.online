# viecinema.online
# Online Movie Ticket Booking System

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

This Spring Boot application provides a user-friendly platform for online movie ticket booking. Users can browse movies, select showtimes and seats, and securely purchase tickets. The system also includes an admin interface for managing movie listings, theaters, showtimes, and bookings.
## UI
![](UI/iPad Mockup.png)
## Features

### User Interface:

* **Movie Browsing:**
    * Detailed movie information (posters, trailers, synopses, cast, reviews)
    * Filtering and sorting options
* **Booking Process:**
    * Showtimes and theater selection
    * Interactive seat map with real-time availability
    * Secure payment processing
* **User Account:**
    * Registration and login
    * Booking history
    * Profile management

### Admin Interface:

* **Movie Management:**
    * Add, edit, and delete movie listings
    * Manage movie details (title, description, genre, director, cast, poster, trailer)
* **Theater and Showtime Management:**
    * Add, edit, and delete theaters
    * Schedule and manage showtimes for each theater
* **Booking Management:**
    * View and manage all bookings
    * Generate reports

### REST API:

* Exposes endpoints for movie listings, showtimes, bookings, and user management
* Allows for easy integration with other systems or mobile apps

### Additional Features:

* **Seat Booking Validation:**
    * Prevents users from booking an entire cinema hall
    * Optimizes seat allocation to avoid leaving single empty seats
* **User Registration Validation:**
    * Ensures accurate and complete user information
    * Validates email and phone number
* **Email Confirmation:**
    * Requires users to confirm registration via email

## Non-Functional Requirements

* **Performance:**
    * Handles at least 10,000 concurrent requests without interruption
    * Page load time under 2 seconds for 95% of requests
* **Security:**
    * Encrypts sensitive user information
    * Uses HTTPS for secure transactions
    * Complies with data protection regulations (GDPR or equivalent)
* **Scalability:**
    * Designed for easy scaling to handle growth
    * Microservices architecture for component-level scaling
* **Availability:**
    * 99.9% uptime during operational hours
    * Backup and recovery mechanisms
* **User-Friendliness:**
    * Intuitive interface for all users
    * Initial support for Vietnamese, with English planned (optional)
* **Compatibility:**
    * Works with popular browsers (Chrome, Firefox, Safari, Edge)
    * Responsive design for mobile and desktop
* **Support and Maintenance:**
    * Detailed user and admin guides
    * 24/7 technical support

## Technologies Used

* **Backend:**
    * Spring Boot
    * Spring Data JPA
    * Spring Security
    * MySQL
* **Frontend:**
    * Thymeleaf
    * HTML, CSS, JavaScript

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/tankietse/viecinema.online.git](https://github.com/tankietse/viecinema.online.git)

