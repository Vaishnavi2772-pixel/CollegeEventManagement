package com.college;

public class Student {
    private int studentId;
    private String name;
    private String rollNumber;
    private String department;
    private int year;
    private String email;
    private String phone;
    private String password;

    public Student() {
    }

    public Student(int studentId, String name, String rollNumber, String department, int year, String email, String phone, String password) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.year = year;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
