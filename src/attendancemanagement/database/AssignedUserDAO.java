package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AssignedUserDAO {
    
    private final String TABLE_NAME = "assigned_user";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;
    
    public int assignEmployee(int uid, int empid, Timestamp date) {
        try {
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "uid, "
                    + "emp_id, "
                    + "assigned_date) "
                    + "VALUE(?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, uid);
            prepStatement.setInt(2, empid);
            prepStatement.setTimestamp(3, date);
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 4;
            }
            if(count > 1){
                return 5;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 6;
    }
    
    public int getAssignedEmpId(int uid){
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" where uid="+uid+"";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("emp_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }
    
    public int updateAssignEmp(int uid, int empid, Timestamp date) {
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_id = ?, "
                    + "assigned_date = ? "
                    + "WHERE uid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, empid);
            prepStatement.setTimestamp(2, date);
            prepStatement.setInt(3, uid);
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 4;
            }
            if(count > 1){
                return 5;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 6;
    }

    public int deleteUserAssignedByUid(int uid) {
        try {
            String query = "DELETE FROM "+TABLE_NAME+" WHERE uid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, uid);
            return prepStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssignedRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int deleteUserAssignedByEmpid(int empid) {
        try {
            String query = "DELETE FROM "+TABLE_NAME+" WHERE emp_id = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, empid);
            return prepStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssignedRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
