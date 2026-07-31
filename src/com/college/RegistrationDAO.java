package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {
    public void registerStudent(int studentId, int eventId) throws SQLException {
        String sql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, eventId);
            statement.executeUpdate();
        }
    }

    public List<Registration> getStudentRegistrations(int studentId) throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        String sql = "SELECT r.registration_id, e.event_name, e.venue, e.event_date FROM registrations r INNER JOIN events e ON r.event_id = e.event_id WHERE r.student_id = ? ORDER BY r.registration_date DESC";
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

    public void cancelRegistration(int registrationId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE registration_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrationId);
            statement.executeUpdate();
        }
    }
}
