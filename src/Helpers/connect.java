package Helpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
	    private static final String DB_URL = "jdbc:mysql://localhost:3306/mailtcp";
	    
	    private static final String USER = "root";
	    private static final String PASS = "";

	    public static Connection connect() {
	        Connection connection = null;
	        try {
	   
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(DB_URL, USER, PASS);
	        } catch (SQLException | ClassNotFoundException e) {
	        	e.printStackTrace();
	            System.out.println("Error " + e.getMessage());
	        }
	        return connection;
	    }

	    public static void main(String[] args) {
	        Connection conn = connect.connect();
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

}

