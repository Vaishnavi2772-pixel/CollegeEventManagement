package com.college;

public class Main {
    public static void main(String[] args) {
        try {
            AppServer server = new AppServer(7000);
            server.start();
            System.out.println("College Event Management System is ready on port 7000.");
        } catch (Exception exception) {
            System.err.println("Unable to start the application: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
