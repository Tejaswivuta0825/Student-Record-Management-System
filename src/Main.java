package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==================================");
            System.out.println(" STUDENT RECORD MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.println("==================================");

            int choice = readInteger("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    System.out.println("Thank you for using the application!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Enter 1 to 6.");
            }
        }
    }

    static int readInteger(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static int readPositiveId(String message) {
        while (true) {
            int id = readInteger(message);

            if (id > 0) {
                return id;
            }

            System.out.println("ID must be a positive number.");
        }
    }

    static String readName() {
        while (true) {
            System.out.print("Enter student name: ");
            String name = sc.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be empty.");
        }
    }

    static int readAge() {
        while (true) {
            int age = readInteger("Enter student age: ");

            if (age >= 1 && age <= 120) {
                return age;
            }

            System.out.println("Age must be between 1 and 120.");
        }
    }

    static String readCourse() {
        while (true) {
            System.out.print("Enter student course: ");
            String course = sc.nextLine().trim();

            if (!course.isEmpty()) {
                return course;
            }

            System.out.println("Course cannot be empty.");
        }
    }

    static String readEmail() {
        while (true) {
            System.out.print("Enter student email: ");
            String email = sc.nextLine().trim();

            if (email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return email;
            }

            System.out.println("Please enter a valid email address.");
        }
    }

    static void addStudent() {
        String name = readName();
        int age = readAge();
        String course = readCourse();
        String email = readEmail();

        String sql = "INSERT INTO students (name, age, course, email) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, course);
            ps.setString(4, email);

            if (ps.executeUpdate() > 0) {
                System.out.println("Student added successfully!");
            }

        } catch (Exception e) {
            System.out.println("Unable to add student: " + e.getMessage());
        }
    }

    static void viewStudents() {
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n================ STUDENT RECORDS ================");
            System.out.printf("%-5s %-20s %-5s %-18s %-30s%n",
                    "ID", "NAME", "AGE", "COURSE", "EMAIL");
            System.out.println("--------------------------------------------------"
                    + "----------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                printStudent(rs);
            }

            if (!found) {
                System.out.println("No student records found.");
            }

            System.out.println("=================================================="
                    + "========================================");

        } catch (Exception e) {
            System.out.println("Unable to retrieve records: " + e.getMessage());
        }
    }

    static void searchStudent() {
        int id = readPositiveId("Enter student ID to search: ");
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nStudent found:");
                    printStudent(rs);
                } else {
                    System.out.println("Student not found!");
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to search: " + e.getMessage());
        }
    }

    static void updateStudent() {
        int id = readPositiveId("Enter student ID to update: ");

        String name = readName();
        int age = readAge();
        String course = readCourse();
        String email = readEmail();

        String sql = "UPDATE students SET name = ?, age = ?, "
                   + "course = ?, email = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, course);
            ps.setString(4, email);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found!");
            }

        } catch (Exception e) {
            System.out.println("Unable to update student: " + e.getMessage());
        }
    }

    static void deleteStudent() {
        int id = readPositiveId("Enter student ID to delete: ");

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            System.out.print("Confirm deletion? Type YES to continue: ");
            String confirmation = sc.nextLine().trim();

            if (!confirmation.equals("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found!");
            }

        } catch (Exception e) {
            System.out.println("Unable to delete student: " + e.getMessage());
        }
    }

    static void printStudent(ResultSet rs) throws Exception {
        System.out.printf("%-5d %-20s %-5d %-18s %-30s%n",
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course"),
                rs.getString("email"));
    }
}
