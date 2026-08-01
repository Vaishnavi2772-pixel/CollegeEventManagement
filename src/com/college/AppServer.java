package com.college;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppServer {
    private final int port;
    private final Path rootDir;
    private HttpServer server;

    public AppServer(int port) {
        this.port = port;
        this.rootDir = Paths.get("").toAbsolutePath().normalize();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this::handleRequest);
        server.createContext("/api/register", this::handleRegister);
        server.createContext("/api/login", this::handleLogin);
        server.createContext("/api/events", this::handleEvents);
        server.createContext("/api/registrations", this::handleRegistrations);
        server.createContext("/api/contact", this::handleContact);
        server.createContext("/api/admin/login", this::handleAdminLogin);
        server.createContext("/api/admin/events", this::handleAdminEvents);
        server.createContext("/api/admin/registrations", this::handleAdminRegistrations);
        server.start();
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path == null || path.isBlank()) {
            path = "/index.html";
        }
        if (path.startsWith("/api/")) {
            writeJson(exchange, 404, "{\"success\":false,\"message\":\"Not found\"}");
            return;
        }

        if (path.equals("/")) {
            path = "/index.html";
        }

        Path requestedPath = resolvePath(path);
        if (Files.isDirectory(requestedPath)) {
            requestedPath = requestedPath.resolve("index.html");
        }

        if (!Files.exists(requestedPath)) {
            setCorsHeaders(exchange);
            writeText(exchange, 404, "Not found");
            return;
        }

        byte[] content = Files.readAllBytes(requestedPath);
        String contentType = determineContentType(requestedPath);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        setCorsHeaders(exchange);
        exchange.sendResponseHeaders(200, content.length);
        try (var out = exchange.getResponseBody()) {
            out.write(content);
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        if (!"POST".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
            return;
        }

        String body = readBody(exchange);
        Map<String, String> values = parseJson(body);
        String fullName = required(values, "fullName");
        String rollNumber = required(values, "rollNumber");
        String department = required(values, "department");
        String yearValue = required(values, "year");
        String email = required(values, "email");
        String phone = required(values, "phone");
        String password = required(values, "password");

        if (!isValidEmail(email)) {
            writeJson(exchange, 400, jsonError("Please provide a valid email address."));
            return;
        }
        if (!isValidPhone(phone)) {
            writeJson(exchange, 400, jsonError("Phone number must contain exactly 10 digits."));
            return;
        }
        if (password.length() < 8) {
            writeJson(exchange, 400, jsonError("Password must be at least 8 characters long."));
            return;
        }

        try {
            StudentDAO studentDAO = new StudentDAO();
            if (studentDAO.rollNumberExists(rollNumber)) {
                writeJson(exchange, 409, jsonError("This roll number is already registered."));
                return;
            }
            if (studentDAO.emailExists(email)) {
                writeJson(exchange, 409, jsonError("This email is already registered."));
                return;
            }
            int studentId = studentDAO.createStudent(new Student(0, fullName, rollNumber, department, Integer.parseInt(yearValue), email, phone, password));
            writeJson(exchange, 201, "{\"success\":true,\"message\":\"Registration successful. You can now log in.\",\"studentId\":" + studentId + "}");
        } catch (Exception exception) {
            writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        if (!"POST".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
            return;
        }

        String body = readBody(exchange);
        Map<String, String> values = parseJson(body);
        String email = required(values, "email");
        String password = required(values, "password");

        try {
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.authenticate(email, password);
            if (student == null) {
                writeJson(exchange, 401, jsonError("Invalid credentials. Please try again."));
                return;
            }
            String payload = "{\"success\":true,\"message\":\"Login successful.\",\"student\":{\"studentId\":" + student.getStudentId() + ",\"name\":\"" + escape(student.getName()) + "\",\"email\":\"" + escape(student.getEmail()) + "\"}}";
            writeJson(exchange, 200, payload);
        } catch (Exception exception) {
            writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
        }
    }

    private void handleEvents(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            try {
                EventDAO eventDAO = new EventDAO();
                var events = eventDAO.getAllEvents();
                StringBuilder builder = new StringBuilder("{\"success\":true,\"events\":[");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.append("{\"eventId\":")
                        .append(event.getEventId())
                        .append(",\"eventName\":\"")
                        .append(escape(event.getEventName()))
                        .append("\",\"description\":\"")
                        .append(escape(event.getDescription()))
                        .append("\",\"eventDate\":\"")
                        .append(event.getEventDate())
                        .append("\",\"venue\":\"")
                        .append(escape(event.getVenue()))
                        .append("\",\"availableSeats\":")
                        .append(event.getAvailableSeats())
                        .append(",\"category\":\"")
                        .append(escape(event.getCategory()))
                        .append("\"}");
                }
                builder.append("]}");
                writeJson(exchange, 200, builder.toString());
            } catch (Exception exception) {
                writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
            }
            return;
        }

        if ("POST".equals(method)) {
            try {
                String body = readBody(exchange);
                Map<String, String> values = parseJson(body);
                int studentId = Integer.parseInt(required(values, "studentId"));
                int eventId = Integer.parseInt(required(values, "eventId"));
                RegistrationDAO registrationDAO = new RegistrationDAO();
                EventDAO eventDAO = new EventDAO();
                if (registrationDAO.exists(studentId, eventId)) {
                    writeJson(exchange, 409, jsonError("You already registered for this event."));
                    return;
                }
                var events = eventDAO.getAllEvents();
                boolean found = false;
                for (Event event : events) {
                    if (event.getEventId() == eventId && event.getAvailableSeats() > 0) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    writeJson(exchange, 400, jsonError("This event is no longer available."));
                    return;
                }
                registrationDAO.registerStudent(studentId, eventId);
                eventDAO.adjustAvailableSeats(eventId, -1);
                writeJson(exchange, 201, "{\"success\":true,\"message\":\"Registration confirmed.\"}");
            } catch (Exception exception) {
                writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
            }
            return;
        }

        writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
    }

    private void handleRegistrations(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            int studentId = parseIntegerQueryParam(exchange, "studentId", 0);
            try {
                RegistrationDAO registrationDAO = new RegistrationDAO();
                var registrations = registrationDAO.getStudentRegistrations(studentId);
                StringBuilder builder = new StringBuilder("{\"success\":true,\"registrations\":[");
                for (int i = 0; i < registrations.size(); i++) {
                    Registration registration = registrations.get(i);
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.append("{\"registrationId\":")
                        .append(registration.getRegistrationId())
                        .append(",\"eventName\":\"")
                        .append(escape(registration.getEventName()))
                        .append("\",\"venue\":\"")
                        .append(escape(registration.getVenue()))
                        .append("\",\"eventDate\":\"")
                        .append(registration.getEventDate())
                        .append("\"}");
                }
                builder.append("]}");
                writeJson(exchange, 200, builder.toString());
            } catch (Exception exception) {
                writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
            }
            return;
        }

        if ("POST".equals(method)) {
            try {
                String body = readBody(exchange);
                Map<String, String> values = parseJson(body);
                int registrationId = Integer.parseInt(required(values, "registrationId"));
                RegistrationDAO registrationDAO = new RegistrationDAO();
                int eventId = registrationDAO.getEventIdForRegistration(registrationId);
                registrationDAO.cancelRegistration(registrationId);
                EventDAO eventDAO = new EventDAO();
                eventDAO.adjustAvailableSeats(eventId, 1);
                writeJson(exchange, 200, "{\"success\":true,\"message\":\"Registration cancelled.\"}");
            } catch (Exception exception) {
                writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
            }
            return;
        }

        writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
    }

    private void handleContact(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        if (!"POST".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
            return;
        }

        String body = readBody(exchange);
        Map<String, String> values = parseJson(body);
        ContactMessage message = new ContactMessage(required(values, "name"), required(values, "email"), required(values, "subject"), required(values, "message"));
        try {
            new ContactDAO().saveMessage(message);
            writeJson(exchange, 201, "{\"success\":true,\"message\":\"Message sent successfully. We will get back to you soon.\"}");
        } catch (Exception exception) {
            writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
        }
    }

    private void handleAdminLogin(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        if (!"POST".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
            return;
        }
        String body = readBody(exchange);
        Map<String, String> values = parseJson(body);
        String email = required(values, "email");
        String password = required(values, "password");
        AdminDAO adminDAO = new AdminDAO();
        if (adminDAO.authenticate(email, password)) {
            writeJson(exchange, 200, "{\"success\":true,\"message\":\"Admin login successful.\"}");
        } else {
            writeJson(exchange, 401, jsonError("Invalid admin credentials."));
        }
    }

    private void handleAdminEvents(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        String method = exchange.getRequestMethod();
        try {
            EventDAO eventDAO = new EventDAO();
            if ("GET".equals(method)) {
                var events = eventDAO.getAllEvents();
                StringBuilder builder = new StringBuilder("{\"success\":true,\"events\":[");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.append("{\"eventId\":")
                        .append(event.getEventId())
                        .append(",\"eventName\":\"").append(escape(event.getEventName())).append("\"")
                        .append(",\"description\":\"").append(escape(event.getDescription())).append("\"")
                        .append(",\"eventDate\":\"").append(event.getEventDate()).append("\"")
                        .append(",\"venue\":\"").append(escape(event.getVenue())).append("\"")
                        .append(",\"availableSeats\":").append(event.getAvailableSeats())
                        .append(",\"category\":\"").append(escape(event.getCategory())).append("\"")
                        .append("}");
                }
                builder.append("]}");
                writeJson(exchange, 200, builder.toString());
                return;
            }
            if ("POST".equals(method)) {
                Map<String, String> values = parseJson(readBody(exchange));
                Event event = new Event(0, required(values, "eventName"), required(values, "description"), required(values, "eventDate"), required(values, "venue"), Integer.parseInt(required(values, "availableSeats")), required(values, "category"));
                eventDAO.createEvent(event);
                writeJson(exchange, 201, "{\"success\":true,\"message\":\"Event added successfully.\"}");
                return;
            }
            if ("PUT".equals(method)) {
                Map<String, String> values = parseJson(readBody(exchange));
                Event event = new Event(Integer.parseInt(required(values, "eventId")), required(values, "eventName"), required(values, "description"), required(values, "eventDate"), required(values, "venue"), Integer.parseInt(required(values, "availableSeats")), required(values, "category"));
                eventDAO.updateEvent(event);
                writeJson(exchange, 200, "{\"success\":true,\"message\":\"Event updated successfully.\"}");
                return;
            }
            if ("DELETE".equals(method)) {
                String query = exchange.getRequestURI().getQuery();
                int eventId = Integer.parseInt(query.substring(query.indexOf('=') + 1));
                eventDAO.deleteEvent(eventId);
                writeJson(exchange, 200, "{\"success\":true,\"message\":\"Event deleted successfully.\"}");
                return;
            }
        } catch (Exception exception) {
            writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
            return;
        }
        writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
    }

    private void handleAdminRegistrations(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeOptionsResponse(exchange);
            return;
        }
        if (!"GET".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, "{\"success\":false,\"message\":\"Method not allowed\"}");
            return;
        }
        try {
            AdminDAO adminDAO = new AdminDAO();
            var registrations = adminDAO.getAllRegistrations();
            StringBuilder builder = new StringBuilder("{\"success\":true,\"registrations\":[");
            for (int i = 0; i < registrations.size(); i++) {
                if (i > 0) {
                    builder.append(',');
                }
                builder.append("\"" + escape(registrations.get(i)) + "\"");
            }
            builder.append("]}");
            writeJson(exchange, 200, builder.toString());
        } catch (Exception exception) {
            writeJson(exchange, 500, jsonError("Database error: " + exception.getMessage()));
        }
    }

    private int parseIntegerQueryParam(HttpExchange exchange, String name, int defaultValue) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || query.isBlank()) {
            return defaultValue;
        }
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2 && parts[0].equals(name)) {
                try {
                    return Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignored) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    private Path resolvePath(String path) {
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        Path target = rootDir.resolve(cleanPath).normalize();
        if (!target.startsWith(rootDir)) {
            return rootDir.resolve("index.html");
        }
        return target;
    }

    private String readBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private Map<String, String> parseJson(String body) {
        Map<String, String> values = new HashMap<>();
        Matcher matcher = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:\\s*(\\\"([^\\\"]*)\\\"|(-?\\d+))").matcher(body);
        while (matcher.find()) {
            String key = matcher.group(1);
            String rawValue = matcher.group(2);
            if (rawValue.startsWith("\"")) {
                values.put(key, rawValue.substring(1, rawValue.length() - 1));
            } else {
                values.put(key, rawValue);
            }
        }
        return values;
    }

    private String required(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required value: " + key);
        }
        return value;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    private String jsonError(String message) {
        return "{\"success\":false,\"message\":\"" + escape(message) + "\"}";
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String determineContentType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (fileName.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        if (fileName.endsWith(".json")) {
            return "application/json; charset=utf-8";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }

    private void writeJson(HttpExchange exchange, int status, String payload) throws IOException {
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        setCorsHeaders(exchange);
        exchange.sendResponseHeaders(status, bytes.length);
        try (var out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void writeText(HttpExchange exchange, int status, String payload) throws IOException {
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        setCorsHeaders(exchange);
        exchange.sendResponseHeaders(status, bytes.length);
        try (var out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void writeOptionsResponse(HttpExchange exchange) throws IOException {
        setCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Allow", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.sendResponseHeaders(204, -1);
    }

    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }
}
