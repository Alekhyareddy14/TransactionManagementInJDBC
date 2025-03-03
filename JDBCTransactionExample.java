package TransactionHandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCTransactionExample {
    private static final String URL = "jdbc:mysql://localhost:3306/employee";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Disable auto-commit to start transaction
            con.setAutoCommit(false);

            // First Insert Query
            String sql1 = "INSERT INTO employees (first_name, last_name, email, phone, salary) VALUES (?, ?, ?, ?, ?)";
            pstmt1 = con.prepareStatement(sql1);
            pstmt1.setString(1, "Alice");
            pstmt1.setString(2, "Brown");
            pstmt1.setString(3, "alice.brown@gmail.com");
            pstmt1.setString(4, "9876543210");
            pstmt1.setDouble(5, 60000);
            pstmt1.executeUpdate();

            // Second Insert Query (Simulating an Error)
            String sql2 = "INSERT INTO employees (first_name, last_name, email, phone, salary) VALUES (?, ?, ?, ?, ?)";
            pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, "Bob");
            pstmt2.setString(2, "Green");
            pstmt2.setString(3, "bob.green@gmail.com");
            pstmt2.setString(4, "1234567890");
            pstmt2.setDouble(5, 70000);
            pstmt2.executeUpdate();

            // If everything is successful, commit the transaction
            con.commit();
            System.out.println("Transaction committed successfully!");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback(); // Rollback changes if any error occurs
                    System.out.println("Transaction rolled back due to an error.");
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (con != null) con.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
