package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAO{
    private final String TABLE_NAME = "users";
    
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;
    
    public UserDBO login(UserDBO dbo) {
        try {
            String query = "SELECT uid,username,password FROM "+TABLE_NAME+" WHERE "
                    + "username ='"+dbo.getUsername()+"' AND password='"+dbo.getPassword()+"'";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                UserDBO user = new UserDBO();
                user.setUid(res.getInt("uid"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return null;
    }

    public List<UserDBO> selectAll() {
        List<UserDBO> dbos = new ArrayList<>();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY uid DESC";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                UserDBO dbo = new UserDBO();
                dbo.setUid(res.getInt("uid"));
                dbo.setUsername(res.getString("username"));
                dbo.setPassword(res.getString("password"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                dbos.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return dbos;
    }

    public boolean isUserExist(String username) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE username='"+username+"'";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            int count = 0;
            while(res.next()){
                count++;
            }
            if(count > 0){
                return true;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return false;
    }

    public int addNewUser(UserDBO dbo) {
        try {
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "username, "
                    + "password, "
                    + "added_date, "
                    + "last_modified_date) "
                    + "VALUE(?,?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getUsername());
            prepStatement.setString(2, dbo.getPassword());
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
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 3;
    }

    public boolean isEmpIdExist(int empid) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE emp_id="+empid+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            int count = 0;
            while(res.next()){
                count++;
            }
            if(count > 0){
                return true;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return false;
    }

    public UserDBO getUserById(int uid) {
       try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE uid="+uid+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                UserDBO dbo = new UserDBO();
                dbo.setUid(res.getInt("uid"));
                dbo.setUsername(res.getString("username"));
                dbo.setPassword(res.getString("password"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                return dbo;
            }
            
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return null;
    }

    public int updateUser(UserDBO dbo) {
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "username = ?, "
                    + "last_modified_date = ? "
                    + " WHERE uid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getUsername());
            prepStatement.setTimestamp(2, dbo.getLast_modified_date());
            prepStatement.setInt(3, dbo.getUid());
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 5;
            }
            if(count > 1){
                return 6;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 7;
    }

    public int deleteByUid(int uid) {
        try {
            String query = "DELETE FROM  "+TABLE_NAME+ " "
                    + " WHERE uid = ?";

            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         
            prepStatement.setInt(1, uid);

            int count = prepStatement.executeUpdate();

            if(count == 1){
                return 1;
            }

            if(count > 1){
                return 2;
            }

        } catch (SQLException ex) {
             Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updatePassword(UserDBO dbo) {
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "password = ? "
                    + " WHERE uid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getPassword());
            prepStatement.setInt(2, dbo.getUid());
            int count = prepStatement.executeUpdate();
            if(count == 1){
                return 2;
            }
            if(count > 1){
                return 3;
            }
        }catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 1;
    }

    
    
    
}
