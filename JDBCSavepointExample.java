package TransactionHandling;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import java.sql.SQLException;

public class JDBCSavepointExample {
    private static final String URL = "jdbc:mysql://localhost:3306/employee";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        Savepoint savepoint = null;

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
            pstmt1.setString(1, "Alice1");
            pstmt1.setString(2, "Brown");
            pstmt1.setString(3, "alice1.brown@gmail.com");
            pstmt1.setString(4, "9876943210");
            pstmt1.setDouble(5, 60000);
            pstmt1.executeUpdate();
            System.out.println("First record inserted successfully.");

            // Create Savepoint after first insertion
            savepoint = con.setSavepoint("FirstInsert");

            // Second Insert Query (Simulating an Error)
            String sql2 = "INSERT INTO employees (first_name, last_name, email, phone, salary) VALUES (?, ?, ?, ?, ?)";
            pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, "Bob1");
            pstmt2.setString(2, "Green");
            pstmt2.setString(3, "bob1.green@gmail.com");
            pstmt2.setString(4, "1234507890");
            pstmt2.setDouble(5, 70000);
            
            // Simulating an error
            int x = 10 / 0; // This will cause an exception
            
            pstmt2.executeUpdate();
            System.out.println("Second record inserted successfully.");

            // If everything is successful, commit the transaction
            con.commit();
            System.out.println("Transaction committed successfully!");

        } catch (Exception e) {
            try {
                if (con != null && savepoint != null) {
                    // Rollback only to the savepoint
                    con.rollback(savepoint);
                    System.out.println("Rolled back to savepoint: FirstInsert. First record remains, second is removed.");
                    con.commit(); // Commit the first insert
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
