package src;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewStudents {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM students";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("Student Records:");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Course: " + rs.getString("course"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("-------------------------");
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

