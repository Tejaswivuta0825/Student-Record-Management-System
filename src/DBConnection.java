
package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_db";

    public static Connection getConnection()
            throws SQLException {

        Properties properties = new Properties();

        try (FileInputStream input =
                     new FileInputStream(".env")) {
            properties.load(input);
        } catch (IOException e) {
            throw new SQLException(
                    "Could not read .env file.", e);
        }

        String user = properties.getProperty("DB_USER");
        String password = properties.getProperty("DB_PASSWORD");

        if (user == null || password == null) {
            throw new SQLException(
                    "DB_USER or DB_PASSWORD is missing in .env");
        }

        return DriverManager.getConnection(
                URL, user.trim(), password.trim());
    }
}
