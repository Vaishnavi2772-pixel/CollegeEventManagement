package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {
    public int registerStudent(int studentId, int eventId) throws SQLException {
        String sql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, studentId);
            statement.setInt(2, eventId);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean exists(int studentId, int eventId) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND event_id = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<Registration> getStudentRegistrations(int studentId) throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        String sql = "SELECT r.registration_id, e.event_id, e.event_name, e.venue, e.event_date FROM registrations r INNER JOIN events e ON r.event_id = e.event_id WHERE r.student_id = ? ORDER BY r.registration_date DESC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    registrations.add(new Registration(
                        resultSet.getInt("registration_id"),
                        studentId,
                        resultSet.getString("event_name"),
                        resultSet.getString("venue"),
                        resultSet.getDate("event_date").toLocalDate().toString()
                    ));
                }
            }
        }
        return registrations;
    }

    public int getEventIdForRegistration(int registrationId) throws SQLException {
        String sql = "SELECT event_id FROM registrations WHERE registration_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("event_id");
                }
            }
        }
        return 0;
    }

    public void cancelRegistration(int registrationId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE registration_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrationId);
            statement.executeUpdate();
        }
    }
}
