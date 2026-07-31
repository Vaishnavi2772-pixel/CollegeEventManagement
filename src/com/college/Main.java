package com.college;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== College Event Management System ===");
        System.out.println("main() has been called.");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1. Add a sample student");
                System.out.println("2. List all students");
                System.out.println("3. List all events");
                System.out.println("4. Test admin registration lookup");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine().trim();
                System.out.println("You selected: " + choice);

                switch (choice) {
                    case "1":
                        addSampleStudent();
                        break;
                    case "2":
                        listStudents();
                        break;
                    case "3":
                        listEvents();
                        break;
                    case "4":
                        testAdminLookup();
                        break;
                    case "5":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (Exception exception) {
            System.err.println("Unexpected error: " + exception.getMessage());
            exception.printStackTrace();
        }

        System.out.println("Application closed. Thank you!");
    }

    private static void addSampleStudent() throws SQLException {
        System.out.println("[TRACE] addSampleStudent() started.");
        StudentDAO studentDAO = new StudentDAO();
        String suffix = String.valueOf(System.currentTimeMillis());
        Student student = new Student(
                0,
                "Demo Student",
                "CS" + suffix.substring(suffix.length() - 4),
                "Computer Science",
                2,
                "demo" + suffix + "@example.com",
                "9000000000",
                "demoPass123"
        );
        studentDAO.createStudent(student);
        System.out.println("[TRACE] Sample student inserted successfully.");
    }

    private static void listStudents() throws SQLException {
        System.out.println("[TRACE] listStudents() started.");
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        System.out.println("Students:");
        for (Student student : students) {
            System.out.println("- " + student.getName() + " | " + student.getEmail());
        }
    }

    private static void listEvents() throws SQLException {
        System.out.println("[TRACE] listEvents() started.");
        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getAllEvents();
        System.out.println("Events:");
        for (Event event : events) {
            System.out.println("- " + event.getEventName() + " | Seats: " + event.getAvailableSeats());
        }
    }

    private static void testAdminLookup() throws SQLException {
        System.out.println("[TRACE] testAdminLookup() started.");
        AdminDAO adminDAO = new AdminDAO();
        List<String> registrations = adminDAO.getAllRegistrations();
        System.out.println("Registration summary:");
        for (String registration : registrations) {
            System.out.println("- " + registration);
        }
    }
}
