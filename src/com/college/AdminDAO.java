package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private static final String ADMIN_EMAIL = "admin@college.edu";
    private static final String ADMIN_PASSWORD = "Admin@123";

    public boolean authenticate(String email, String password) {
        return ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password);
    }

    public List<String> getAllRegistrations() throws SQLException {
        List<String> registrations = new ArrayList<>();
        String sql = "SELECT s.name AS student_name, e.event_name FROM registrations r INNER JOIN students s ON r.student_id = s.student_id INNER JOIN events e ON r.event_id = e.event_id ORDER BY e.event_name ASC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                registrations.add(resultSet.getString("student_name") + " -> " + resultSet.getString("event_name"));
            }
        }
        return registrations;
    }
}
