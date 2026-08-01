package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public int createStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, roll_number, department, year, email, phone, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getRollNumber());
            statement.setString(3, student.getDepartment());
            statement.setInt(4, student.getYear());
            statement.setString(5, student.getEmail());
            statement.setString(6, student.getPhone());
            statement.setString(7, student.getPassword());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean exists(String email, String rollNumber) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE email = ? OR roll_number = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, rollNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public Student authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM students WHERE email = ? AND password = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("name"),
                        resultSet.getString("roll_number"),
                        resultSet.getString("department"),
                        resultSet.getInt("year"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name ASC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(
                    resultSet.getInt("student_id"),
                    resultSet.getString("name"),
                    resultSet.getString("roll_number"),
                    resultSet.getString("department"),
                    resultSet.getInt("year"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("password")
                ));
            }
        }
        return students;
    }

    public List<Student> searchStudents(String query) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR roll_number LIKE ? ORDER BY name ASC";
        String pattern = "%" + query + "%";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("name"),
                        resultSet.getString("roll_number"),
                        resultSet.getString("department"),
                        resultSet.getInt("year"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                    ));
                }
            }
        }
        return students;
    }
}
