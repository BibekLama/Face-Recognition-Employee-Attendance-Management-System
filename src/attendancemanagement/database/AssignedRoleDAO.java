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


public class AssignedRoleDAO {
    private final String TABLE_NAME = "assigned_role";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;
    
    public int assignRole(int uid, int rid, Timestamp date) {
        try {
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "uid, "
                    + "rid, "
                    + "assigned_date) "
                    + "VALUE(?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, uid);
            prepStatement.setInt(2, rid);
            prepStatement.setTimestamp(3, date);
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 3;
    }

    
    
    public int getAssignedRoleId(int uid){
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" where uid="+uid+"";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("rid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }
    
    
    public int getAssignedRid(int uid){
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" where uid="+uid+"";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("rid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int updateAssignRole(int uid, int rid, Timestamp date) {
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "rid = ?, "
                    + "assigned_date = ? "
                    + "WHERE uid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, rid);
            prepStatement.setTimestamp(2, date);
            prepStatement.setInt(3, uid);
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 3;
    }

    public int deleteRoleAssignedByUid(int uid) {
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
    
    public int deleteRoleAssignedByRid(int rid) {
        try {
            String query = "DELETE FROM "+TABLE_NAME+" WHERE rid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, rid);
            return prepStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssignedRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
}
