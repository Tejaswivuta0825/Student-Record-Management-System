package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentManagementGUI extends JFrame {

    // Modern Purple Theme
    private final Color primaryPurple = new Color(76, 29, 149);
    private final Color accentPurple = new Color(124, 58, 237);
    private final Color lightBackground = new Color(245, 243, 255);
    private final Color white = Color.WHITE;
    private final Color darkText = new Color(31, 41, 55);
    private final Color borderPurple = new Color(221, 214, 254);

    private JTextField idField, nameField, ageField;
    private JTextField courseField, emailField;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentManagementGUI() {

        setTitle("Student Record Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(lightBackground);

        // Heading
        JLabel heading = new JLabel(
                "Student Record Management System",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(primaryPurple);
        heading.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        add(heading, BorderLayout.NORTH);

        // Student input form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 12, 12));
        formPanel.setBackground(white);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderPurple, 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        courseField = new JTextField();
        emailField = new JTextField();

        // Style input fields
        JTextField[] fields = {
                idField, nameField, ageField, courseField, emailField
        };

        for (JTextField field : fields) {
            field.setFont(new Font("Arial", Font.PLAIN, 14));
            field.setForeground(darkText);
            field.setBackground(white);
            field.setCaretColor(accentPurple);

            field.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(borderPurple),
                            BorderFactory.createEmptyBorder(6, 8, 6, 8)
                    )
            );
        }

        addFormRow(formPanel, "Student ID:", idField);
        addFormRow(formPanel, "Name:", nameField);
        addFormRow(formPanel, "Age:", ageField);
        addFormRow(formPanel, "Course:", courseField);
        addFormRow(formPanel, "Email:", emailField);

        // Student records table
        String[] columns = {"ID", "Name", "Age", "Course", "Email"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        studentTable.setRowHeight(28);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.setForeground(darkText);
        studentTable.setBackground(white);
        studentTable.setSelectionBackground(
                new Color(221, 214, 254)
        );
        studentTable.setSelectionForeground(primaryPurple);
        studentTable.setGridColor(borderPurple);
        studentTable.setShowVerticalLines(false);
        studentTable.setFillsViewportHeight(true);

        // Purple table header
        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setBackground(primaryPurple);
        tableHeader.setForeground(white);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
        tableHeader.setPreferredSize(new Dimension(100, 35));

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.getViewport().setBackground(white);
        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(borderPurple),
                        "Student Records"
                )
        );

        // Main center panel
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(lightBackground);
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );

        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JButton addButton = createButton("Add", accentPurple);
        JButton searchButton = createButton("Search", primaryPurple);
        JButton updateButton = createButton("Update", accentPurple);
        JButton deleteButton = createButton(
                "Delete", new Color(220, 38, 38)
        );
        JButton refreshButton = createButton(
                "Refresh", primaryPurple
        );
        JButton clearButton = createButton(
                "Clear", new Color(107, 114, 128)
        );

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 15)
        );
        buttonPanel.setBackground(lightBackground);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addStudent());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());
        clearButton.addActionListener(e -> clearFields());

        // Select a row to fill the form
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = studentTable.getSelectedRow();

                if (row != -1) {
                    idField.setText(
                            tableModel.getValueAt(row, 0).toString()
                    );
                    nameField.setText(
                            tableModel.getValueAt(row, 1).toString()
                    );
                    ageField.setText(
                            tableModel.getValueAt(row, 2).toString()
                    );
                    courseField.setText(
                            tableModel.getValueAt(row, 3).toString()
                    );
                    emailField.setText(
                            tableModel.getValueAt(row, 4).toString()
                    );
                }
            }
        });

        loadStudents();
        setVisible(true);
    }

    // Add a label and field to the form
    private void addFormRow(
            JPanel panel, String labelText, JTextField field) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(darkText);

        panel.add(label);
        panel.add(field);
    }

    // Create styled buttons
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(white);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(115, 38));

        return button;
    }

    // Load all students
    private void loadStudents() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, name, age, course, email FROM students ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course"),
                        rs.getString("email")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load students:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Add student
    private void addStudent() {

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String course = courseField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty()
                || course.isEmpty() || email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all student details."
            );
            return;
        }

        try {
            int age = Integer.parseInt(ageText);

            if (age <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Age must be greater than zero."
                );
                return;
            }

            String sql =
                    "INSERT INTO students (name, age, course, email) "
                            + "VALUES (?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, course);
                ps.setString(4, email);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this, "Student added successfully!"
                );

                clearFields();
                loadStudents();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a valid age."
            );
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    // Search student by ID
    private void searchStudent() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a Student ID."
            );
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            String sql =
                    "SELECT id, name, age, course, email "
                            + "FROM students WHERE id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        nameField.setText(rs.getString("name"));
                        ageField.setText(
                                String.valueOf(rs.getInt("age"))
                        );
                        courseField.setText(rs.getString("course"));
                        emailField.setText(rs.getString("email"));

                        studentTable.clearSelection();

                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (Integer.parseInt(
                                    tableModel.getValueAt(i, 0).toString()
                            ) == id) {
                                studentTable.setRowSelectionInterval(i, i);
                                studentTable.scrollRectToVisible(
                                        studentTable.getCellRect(i, 0, true)
                                );
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this, "Student not found."
                        );
                    }
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Student ID must be a valid number."
            );
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    // Update student
    private void updateStudent() {

        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String course = courseField.getText().trim();
        String email = emailField.getText().trim();

        if (idText.isEmpty() || name.isEmpty() || ageText.isEmpty()
                || course.isEmpty() || email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all student details."
            );
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int age = Integer.parseInt(ageText);

            if (age <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Age must be greater than zero."
                );
                return;
            }

            String sql =
                    "UPDATE students SET name = ?, age = ?, "
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
                    JOptionPane.showMessageDialog(
                            this, "Student updated successfully!"
                    );
                    clearFields();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Student ID not found."
                    );
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter valid numbers for ID and age."
            );
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    // Delete student
    private void deleteStudent() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Select a student or enter a Student ID."
            );
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete Student ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = "DELETE FROM students WHERE id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this, "Student deleted successfully!"
                    );
                    clearFields();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Student ID not found."
                    );
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Student ID must be a valid number."
            );
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    // Clear form fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        courseField.setText("");
        emailField.setText("");

        studentTable.clearSelection();
    }

    // Display database errors
    private void showDatabaseError(SQLException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Database operation failed:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}