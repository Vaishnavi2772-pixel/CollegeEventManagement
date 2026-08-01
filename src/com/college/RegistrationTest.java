package com.college;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RegistrationTest {
    public static void main(String[] args) {
        System.out.println("Registration test script started.");
        String endpoint = "http://localhost:7000/api/register";

        String[] samples = new String[]{
            jsonPayload("Rahul Sharma", "27CSE001", "Computer Science", 2, "rahul.sharma.test27cse001@gmail.com", "9876501234", "Password@123"),
            jsonPayload("Priya Reddy", "27CSE002", "Information Technology", 3, "priya.reddy.test27cse002@gmail.com", "9123456780", "SecurePass1"),
            jsonPayload("Nikhil Kumar", "27IT001", "Information Technology", 1, "nikhil.kumar.test27it001@gmail.com", "9988776655", "NikhilPass1")
        };

        for (String sample : samples) {
            System.out.println("\nPosting registration:\n" + sample);
            String response = postJson(endpoint, sample);
            System.out.println("Response:\n" + response);
        }

        System.out.println("\nPosting duplicate roll number test (same 27CSE001 roll number):");
        String duplicate = jsonPayload("Aman Verma", "27CSE001", "Computer Science", 2, "aman.verma.duplicate@gmail.com", "9012345678", "Duplicate@123");
        System.out.println(duplicate);
        System.out.println("Response:\n" + postJson(endpoint, duplicate));

        System.out.println("Registration test script finished.");
    }

    private static String jsonPayload(String name, String rollNumber, String department, int year, String email, String phone, String password) {
        return String.format(
            "{\"fullName\":\"%s\",\"rollNumber\":\"%s\",\"department\":\"%s\",\"year\":%d,\"email\":\"%s\",\"phone\":\"%s\",\"password\":\"%s\"}",
            escape(name), escape(rollNumber), escape(department), year, escape(email), escape(phone), escape(password)
        );
    }

    private static String postJson(String endpoint, String json) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream out = connection.getOutputStream()) {
                out.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 400 ? connection.getInputStream() : connection.getErrorStream(),
                StandardCharsets.UTF_8
            ));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(System.lineSeparator());
            }
            return String.format("HTTP %d\n%s", responseCode, response.toString().trim());
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
