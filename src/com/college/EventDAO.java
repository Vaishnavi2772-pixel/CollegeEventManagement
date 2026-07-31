package com.college;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public void createEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (event_name, description, event_date, venue, available_seats, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setString(4, event.getVenue());
            statement.setInt(5, event.getAvailableSeats());
            statement.setString(6, event.getCategory());
            statement.executeUpdate();
        }
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
}
