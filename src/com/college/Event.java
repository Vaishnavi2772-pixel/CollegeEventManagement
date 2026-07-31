package com.college;

public class Event {
    private int eventId;
    private String eventName;
    private String description;
    private String eventDate;
    private String venue;
    private int availableSeats;
    private String category;

    public Event() {
    }

    public Event(int eventId, String eventName, String description, String eventDate, String venue, int availableSeats, String category) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.venue = venue;
        this.availableSeats = availableSeats;
        this.category = category;
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
