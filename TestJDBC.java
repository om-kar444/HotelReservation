import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) {
        try {
            //This program is the first progra for the jdbc 
            // For MySQL 9.x, the driver class is different
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/testdb?useSSL=false",
                "root",  // your username
                "omkar444"       // your password (leave empty if none)
            );
            
            System.out.println("✓ CONNECTED TO DATABASE!");
            
            // Test query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NOW() as current_time");
            
            if(rs.next()) {
                System.out.println("✓ Current database time: " + rs.getString("current_time"));
            }
            
            conn.close();
            
        } catch(ClassNotFoundException e) {
            System.out.println("✗ DRIVER ERROR: " + e.getMessage());
            System.out.println("Make sure you have mysql-connector-j-9.4.0.jar in the folder");
        } catch(SQLException e) {
            System.out.println("✗ DATABASE ERROR: " + e.getMessage());
            System.out.println("Check: 1) MySQL running, 2) Database exists, 3) Password correct");
        }
    }
}