-- College Event Management System
-- Database schema, seed data, and sample queries
-- MySQL 8.x compatible

DROP DATABASE IF EXISTS event_management;
CREATE DATABASE event_management;
USE event_management;

CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE NOT NULL,
    venue VARCHAR(150) NOT NULL,
    available_seats INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (student_id, event_id)
);

CREATE TABLE contact_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE students
ADD COLUMN status VARCHAR(20) DEFAULT 'active';

ALTER TABLE events
ADD COLUMN category VARCHAR(50) DEFAULT 'General';

INSERT INTO students (name, roll_number, department, year, email, phone, password)
VALUES
('Aarav Sharma', 'CS101', 'Computer Science', 2, 'aarav@example.com', '9876543210', 'password123'),
('Meera Iyer', 'EC102', 'Electronics', 3, 'meera@example.com', '9123456780', 'securepass1'),
('Rohan Patel', 'ME103', 'Mechanical', 1, 'rohan@example.com', '9988776655', 'rohan@2024');

INSERT INTO events (event_name, description, event_date, venue, available_seats, category)
VALUES
('Hackathon 2026', 'A 24-hour coding challenge for innovative solutions.', '2026-08-15', 'Main Auditorium', 80, 'Technical'),
('Cultural Fest', 'Music, dance, art and cultural showcases.', '2026-09-10', 'Open Air Theatre', 120, 'Cultural'),
('Science Expo', 'Student projects and research demonstrations.', '2026-10-05', 'Science Block', 60, 'Academic'),
('Sports Meet', 'Inter-departmental competitions and fun events.', '2026-11-12', 'Sports Ground', 150, 'Sports');

INSERT INTO registrations (student_id, event_id)
VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT INTO contact_messages (name, email, subject, message)
VALUES
('Nisha Rao', 'nisha@example.com', 'Event Registration', 'I would like to know more about the upcoming hackathon.');

UPDATE students
SET status = 'active'
WHERE student_id = 1;

UPDATE events
SET available_seats = available_seats - 1
WHERE event_id = 1;

DELETE FROM contact_messages
WHERE message_id = 1;

SELECT *
FROM students
WHERE department = 'Computer Science'
ORDER BY name ASC;

SELECT *
FROM events
WHERE event_date >= CURDATE()
ORDER BY event_date ASC
LIMIT 3 OFFSET 0;

SELECT department, COUNT(*) AS student_count
FROM students
GROUP BY department;

SELECT event_id, SUM(available_seats) AS total_seats
FROM events
GROUP BY event_id;

SELECT AVG(year) AS average_year
FROM students;

SELECT MAX(available_seats) AS highest_seats, MIN(available_seats) AS lowest_seats
FROM events;

SELECT s.name, s.email, e.event_name
FROM registrations r
INNER JOIN students s ON r.student_id = s.student_id
INNER JOIN events e ON r.event_id = e.event_id;

SELECT s.name, s.email, e.event_name
FROM students s
LEFT JOIN registrations r ON s.student_id = r.student_id
LEFT JOIN events e ON r.event_id = e.event_id;

CREATE VIEW student_registration_summary AS
SELECT s.student_id, s.name, s.roll_number, COUNT(r.registration_id) AS total_registrations
FROM students s
LEFT JOIN registrations r ON s.student_id = r.student_id
GROUP BY s.student_id, s.name, s.roll_number;

SELECT * FROM student_registration_summary ORDER BY total_registrations DESC;
