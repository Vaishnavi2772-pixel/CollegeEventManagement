package com.college;

public class Registration {
    private int registrationId;
    private int studentId;
    private String eventName;
    private String venue;
    private String eventDate;

    public Registration() {
    }

    public Registration(int registrationId, int studentId, String eventName, String venue, String eventDate) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.eventName = eventName;
        this.venue = venue;
        this.eventDate = eventDate;
    }

    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
}
