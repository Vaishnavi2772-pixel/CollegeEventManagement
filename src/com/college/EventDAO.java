package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public int createEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (event_name, description, event_date, venue, available_seats, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setString(4, event.getVenue());
            statement.setInt(5, event.getAvailableSeats());
            statement.setString(6, event.getCategory());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date ASC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                events.add(new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getString("description"),
                    resultSet.getDate("event_date").toLocalDate().toString(),
                    resultSet.getString("venue"),
                    resultSet.getInt("available_seats"),
                    resultSet.getString("category")
                ));
            }
        }
        return events;
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET event_name = ?, description = ?, event_date = ?, venue = ?, available_seats = ?, category = ? WHERE event_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setString(4, event.getVenue());
            statement.setInt(5, event.getAvailableSeats());
            statement.setString(6, event.getCategory());
            statement.setInt(7, event.getEventId());
            statement.executeUpdate();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            statement.executeUpdate();
        }
    }

    public void adjustAvailableSeats(int eventId, int delta) throws SQLException {
        String sql = "UPDATE events SET available_seats = available_seats + ? WHERE event_id = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, delta);
            statement.setInt(2, eventId);
            statement.executeUpdate();
        }
    }
}
