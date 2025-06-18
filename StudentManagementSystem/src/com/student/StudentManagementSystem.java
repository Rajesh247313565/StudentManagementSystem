package com.student;

import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";         // your MySQL username
    static final String PASS = "root";         // your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> addStudent(conn, scanner);
                    case 2 -> viewStudents(conn);
                    case 3 -> updateStudent(conn, scanner);
                    case 4 -> deleteStudent(conn, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Name: ");
        scanner.nextLine();  // consume newline
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();

        System.out.print("Enter Course: ");
        scanner.nextLine();  // consume newline
        String course = scanner.nextLine();

        String query = "INSERT INTO students (name, age, course) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, course);
            ps.executeUpdate();
            System.out.println("Student added successfully.");
        }
    }

    private static void viewStudents(Connection conn) throws SQLException {
        String query = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nID | Name | Age | Course");
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course"));
            }
        }
    }

    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter new Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new Course: ");
        String course = scanner.nextLine();

        String query = "UPDATE students SET name=?, age=?, course=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, course);
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
                System.out.println("Student updated successfully.");
            else
                System.out.println("Student not found.");
        }
    }

    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM students WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
                System.out.println("Student deleted successfully.");
            else
                System.out.println("Student not found.");
        }
    }
}

