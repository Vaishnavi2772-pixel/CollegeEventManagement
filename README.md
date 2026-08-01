# College Event Management System

A complete college event management application built with HTML5, CSS3, JavaScript, Java, JDBC, and MySQL. The project now includes a working student registration flow, event browsing, registration management, contact submissions, and an admin panel.

## Features
- Responsive landing, login, registration, dashboard, events, my registrations, contact, and admin pages
- Student sign-up and login backed by MySQL through JDBC
- Event search, filtering, sorting, seat tracking, and registration actions
- My registrations page with cancellation support
- Contact form persistence in the database
- Admin login and event management panel for adding, editing, and deleting events

## Project Structure
- index.html, login.html, register.html, dashboard.html, events.html, my-registrations.html, contact.html, admin.html
- css/ for styling
- js/ for frontend interactivity
- src/com/college/ for Java DAO and server classes
- database/event_management.sql for the schema and seed data

## Database Setup
1. Start MySQL.
2. Import [database/event_management.sql](database/event_management.sql).
3. Confirm the database name is event_management.
4. Update the credentials in [src/com/college/DBConnection.java](src/com/college/DBConnection.java) if your MySQL user/password differ.

## Java Backend Setup
1. Ensure the MySQL JDBC driver is available in the lib folder.
2. Compile the Java sources with the driver on the classpath.
3. Run the app with the included batch file or by launching the Main class.

## Running the Project
1. Start the Java server by running [run.bat](run.bat) or launching the Main class from the Java project.
2. Alternatively, run this exact command from the project root:

   ```powershell
   powershell.exe -NoProfile -Command "Set-Location 'C:\Users\dell\OneDrive\Desktop\college event management system'; java -cp 'out;lib\\mysql-connector-j-9.2.0.jar' com.college.Main"
   ```

3. Open the app in your browser at http://localhost:7000.
4. Use the UI to register, log in, browse events, and manage registrations.

## Admin Credentials
- Email: admin@campuspulse.edu
- Password: admin123
