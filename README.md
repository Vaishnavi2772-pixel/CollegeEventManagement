# College Event Management System

A complete full-stack college event management application built with HTML5, CSS3, JavaScript, Java, JDBC, and MySQL.

## Features
- Responsive landing page with navigation, hero section, featured events, about section, contact section, and footer.
- Student registration and login flows with client-side validation.
- Student dashboard with event statistics and navigation cards.
- Event listing page with search, filter, sort, registration, and seat management.
- My Registrations page with cancellation support.
- Contact form that stores messages in the database.
- Java JDBC backend with DAO classes for students, events, registrations, contact messages, and admin access.

## Project Structure
- index.html
- login.html
- register.html
- dashboard.html
- events.html
- my-registrations.html
- contact.html
- css/
- js/
- java/
- database/

## Database Setup
1. Start MySQL.
2. Import [database/event_management.sql](database/event_management.sql).
3. Confirm that the database name is `college_event_management`.

## Java Setup
1. Ensure the MySQL JDBC driver is available in the classpath.
2. Update the database credentials in [java/DBConnection.java](java/DBConnection.java) if needed.
3. Compile and run [java/Main.java](java/Main.java).

## How to Run the Frontend
Open the HTML files in a browser, or serve the project from a simple local web server.

## Notes
The frontend uses localStorage for demo interactions. The Java backend uses JDBC PreparedStatement and MySQL for persistence.
