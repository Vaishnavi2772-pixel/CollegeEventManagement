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
('Rahul Sharma', '27CSE001', 'Computer Science', 2, 'rahulsharma27cse001@gmail.com', '9876501234', 'Password@123'),
('Priya Reddy', '27IT002', 'Information Technology', 3, 'priyareddy27it002@gmail.com', '9123456780', 'SecurePass1'),
('Arjun Kumar', '27ECE003', 'Electronics', 1, 'arjunkumar27ece003@gmail.com', '9988776655', 'Arjun2026#'),
('Sneha Murthy', '27EEE004', 'Electrical Engineering', 4, 'snehamurthy27eee004@gmail.com', '9012345678', 'SnehaPass!'),
('Rohit Verma', '27ME005', 'Mechanical', 3, 'rohitverma27me005@gmail.com', '9876512340', 'Rohit@123'),
('Ananya Gupta', '27CSE006', 'Computer Science', 2, 'ananyagupta27cse006@gmail.com', '9234567890', 'Ananya@456'),
('Karthik Reddy', '27DS007', 'Data Science', 4, 'karthikreddy27ds007@gmail.com', '9345678901', 'Karthik#789'),
('Deepika Patel', '27CSE008', 'Computer Science', 1, 'deepikapatel27cse008@gmail.com', '9456789012', 'Deepika@2026'),
('Nikhil Kumar', '27IT009', 'Information Technology', 3, 'nikhilkumar27it009@gmail.com', '9567890123', 'NikhilPass1'),
('Pooja Sharma', '27ECE010', 'Electronics', 2, 'poojasharma27ece010@gmail.com', '9678901234', 'Pooja@321'),
('Mayank Joshi', '27AI011', 'AI & ML', 2, 'mayankjoshi27ai011@gmail.com', '9789012345', 'MayankAI#11'),
('Sanya Iyer', '27CSE012', 'Computer Science', 3, 'sanyaayer27cse012@gmail.com', '9890123456', 'Sanya2026!'),
('Vikram Singh', '27ME013', 'Mechanical', 4, 'vikramsingh27me013@gmail.com', '9012345679', 'Vikram@2026'),
('Riya Desai', '27EEE014', 'Electrical Engineering', 1, 'riyadesai27eee014@gmail.com', '9123456791', 'RiyaEEE14'),
('Neha Patel', '27DS015', 'Data Science', 3, 'nehapatel27ds015@gmail.com', '9234567892', 'NehaData#15'),
('Amit Sharma', '27CIV016', 'Civil', 2, 'amitsharma27civ016@gmail.com', '9345678903', 'AmitCIV16'),
('Simran Kaur', '27AI017', 'AI & ML', 4, 'simrankaur27ai017@gmail.com', '9456789014', 'SimranAI@17');

INSERT INTO events (event_name, description, event_date, venue, available_seats, category)
VALUES
('Hackathon 2026', 'A 24-hour coding challenge for innovative solutions.', '2026-08-15', 'Main Auditorium', 80, 'Technical'),
('Cultural Fest', 'Music, dance, art and cultural showcases.', '2026-09-10', 'Open Air Theatre', 120, 'Cultural'),
('Science Expo', 'Student projects and research demonstrations.', '2026-10-05', 'Science Block', 60, 'Academic'),
('Sports Meet', 'Inter-departmental competitions and fun events.', '2026-11-12', 'Sports Ground', 150, 'Sports'),
('Art Workshop', 'Hands-on creative art and design sessions.', '2026-09-20', 'Art Studio', 40, 'Cultural'),
('Startup Pitch Day', 'Pitch business ideas to mentors and judges.', '2026-08-28', 'Conference Hall', 50, 'Entrepreneurship'),
('Environmental Summit', 'Discussion panels on sustainability and climate action.', '2026-10-18', 'Auditorium B', 75, 'Academic'),
('Robotics Showcase', 'Robotic systems built by students and faculty.', '2026-11-02', 'Engineering Lab', 55, 'Technical'),
('Photography Contest', 'Capture campus life and win prizes.', '2026-09-05', 'Campus Grounds', 80, 'Cultural'),
('Leadership Bootcamp', 'Workshops to build leadership and team skills.', '2026-10-12', 'Seminar Room 1', 90, 'Professional'),
('Music Band Night', 'Live performances by college bands.', '2026-11-22', 'Open Air Theatre', 110, 'Cultural'),
('Data Science Meetup', 'Talks on AI, ML, and data analytics.', '2026-08-25', 'Lecture Hall 3', 60, 'Technical'),
('Debate Championship', 'Inter-college debate competition.', '2026-10-30', 'Conference Hall', 70, 'Academic'),
('Health Awareness Fair', 'Health checkups and wellness sessions.', '2026-09-15', 'Health Center', 100, 'Awareness'),
('Drama Night', 'Stage plays produced by student drama club.', '2026-11-05', 'Auditorium A', 90, 'Cultural'),
('Campus Festival', 'Exhibitions, food stalls, and entertainment.', '2026-12-01', 'Central Plaza', 200, 'General');

INSERT INTO registrations (student_id, event_id)
VALUES
(1, 1),
(1, 5),
(2, 2),
(2, 9),
(3, 3),
(3, 10),
(4, 1),
(4, 12),
(5, 2),
(5, 13),
(6, 4),
(6, 7),
(7, 8),
(7, 11),
(8, 3),
(8, 14),
(9, 5),
(9, 15),
(10, 6),
(10, 16),
(11, 7),
(11, 9),
(12, 4),
(12, 8),
(13, 2),
(13, 10),
(14, 1),
(14, 11),
(15, 5),
(15, 14),
(16, 12),
(16, 16),
(17, 13),
(17, 15);

INSERT INTO contact_messages (name, email, subject, message)
VALUES
('Nisha Rao', 'nisha@example.com', 'Event Registration', 'I would like to know more about the upcoming hackathon.'),
('Rahul Mehta', 'rahul@example.com', 'Cultural Fest', 'Can I volunteer for the cultural fest?'),
('Sneha Joshi', 'sneha@example.com', 'Workshop Query', 'What materials are needed for the art workshop?'),
('Vikram Singh', 'vikram@example.com', 'Registration Help', 'I am unable to register for the robotics showcase.'),
('Priya Kulkarni', 'priya.k@example.com', 'Campus Festival', 'Will there be food stalls at the campus festival?'),
('Aman Desai', 'aman@example.com', 'Health Fair', 'Do I need to sign up for the health awareness fair?'),
('Leela Sharma', 'leela@example.com', 'Music Night', 'Are outside guests allowed at the music band night?'),
('Tanvi Patel', 'tanvi@example.com', 'Debate Championship', 'How many members are allowed per debate team?'),
('Arjun Kapoor', 'arjun@example.com', 'Data Science Meetup', 'Can I present a project at the meetup?'),
('Ria Sen', 'ria@example.com', 'Environmental Summit', 'Will there be certificates for attendees?');

SELECT *
FROM students
WHERE department = 'Computer Science'
ORDER BY name ASC;

SELECT *
FROM events
WHERE event_date >= CURDATE()
ORDER BY event_date ASC
LIMIT 5 OFFSET 0;

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
