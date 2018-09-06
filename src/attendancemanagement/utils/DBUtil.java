package attendancemanagement.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBUtil {
    public static void close(Connection connection) {
            if (connection != null) {
                    try {
                            connection.close();
                    } catch (SQLException e) {
                            /*Ignore*/
                    }
            }
    }

    public static void close(Statement statement) {
            if (statement != null) {
                    try {
                            statement.close();
                    } catch (SQLException e) {
                            /*Ignore*/
                    }
            }
    }

    public static void close(ResultSet resultSet) {
            if (resultSet != null) {
                    try {
                            resultSet.close();
                    } catch (SQLException e) {
                            /*Ignore*/
                    }
            }
    }
}
