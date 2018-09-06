package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OptionDAO {
    private final String TABLE_NAME = "options";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;

   
    public OptionDBO selectOption(String name) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE "
                    + "name ='"+name+"'";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                OptionDBO dbo = new OptionDBO();
                dbo.setId(res.getInt("id"));
                dbo.setName(res.getString("name"));
                dbo.setValue(res.getString("value"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                return dbo;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return null;
    }

    public int addTime(OptionDBO dbo) {
        try {
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "name, "
                    + "value, "
                    + "added_date, "
                    + "last_modified_date) "
                    + "VALUE(?,?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getName());
            prepStatement.setString(2, dbo.getValue());
            prepStatement.setTimestamp(3, dbo.getAdded_date());
            prepStatement.setTimestamp(4, dbo.getLast_modified_date());
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(OptionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int updateTime(OptionDBO dbo) {
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "value = ?, "
                    + "last_modified_date = ? "
                    + "WHERE name=?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getValue());
            prepStatement.setTimestamp(2, dbo.getLast_modified_date());
            prepStatement.setString(3, dbo.getName());
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(OptionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    } 
}
