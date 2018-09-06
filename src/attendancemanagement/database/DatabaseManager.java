package attendancemanagement.database;

import static attendancemanagement.utils.UIUtils.errorNotification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseManager {
    public static final String URL = "jdbc:mysql://localhost:3306/mydb";
    public static final String USER = "root";
    public static final String PASSWORD = "";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
    
    private DatabaseManager() throws InstantiationException, IllegalAccessException {
            try {
                Class.forName(DRIVER_CLASS).newInstance();
            } catch (ClassNotFoundException e) {
//                errorNotification("Database Driver Not Found", e.getMessage());
                e.printStackTrace();
            }
    }
    

    public static Connection getConnection() {
            Connection connection = null;
            try {
                    connection = DriverManager.getConnection(URL+"?useSSL=false&user="+USER+"&password="+PASSWORD);
                    System.out.println("Successfully Connected to Database.");
            } catch (SQLException e) {
                    System.out.println("ERROR: Unable to Connect to Database.");
                    errorNotification("Connection Error", e.getMessage());
            }
            return connection;
    }
}
