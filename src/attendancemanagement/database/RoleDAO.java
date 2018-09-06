package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class RoleDAO {
    private final String TABLE_NAME = "roles";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;

    public ObservableList<RoleDBO> selectAll() {
        ObservableList<RoleDBO> roles = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY rid DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                RoleDBO dbo = new RoleDBO();
                dbo.setRid(res.getInt("rid"));
                dbo.setRole(res.getString("role"));
                dbo.setDisplay_name(res.getString("display_name"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                roles.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return roles;
    }

    public int addRole(RoleDBO dbo) {
        try{
            String query = "INSERT INTO "+TABLE_NAME+" "
                    + "("
                    + "role,"
                    + "display_name,"
                    + "added_date,"
                    + "last_modified_date"
                    + ") "
                    + "VALUE(?,?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, dbo.getRole());
            prepStatement.setString(2, dbo.getDisplay_name());
            prepStatement.setTimestamp(3, dbo.getAdded_date());
            prepStatement.setTimestamp(4, dbo.getLast_modified_date());
            // execute the java preparedstatement
            int count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int isRoleExist(String role) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE role='"+role+"'";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            int count = 0;
            while(res.next()){
                count ++;
            }
            if(count > 0){
                return 4;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 3;
    }

    public RoleDBO selectByRid(int rid) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE rid="+rid+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            int count = 0;
            while(res.next()){
                RoleDBO dbo = new RoleDBO();
                dbo.setRid(res.getInt("rid"));
                dbo.setRole(res.getString("role"));
                dbo.setDisplay_name(res.getString("display_name"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                return dbo;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return null;
    }

    public int editRole(RoleDBO dbo) {
        try {
            String query = "UPDATE  "+TABLE_NAME+" SET "
                    + "role = ?,"
                    + "display_name = ?,"
                    + "last_modified_date = ?"
                    + " WHERE rid = ?";
            
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getRole());
            prepStatement.setString(2, dbo.getDisplay_name());
            prepStatement.setTimestamp(3, dbo.getLast_modified_date());
            prepStatement.setInt(4, dbo.getRid());
            
            int count = prepStatement.executeUpdate();
            
            if(count == 1){
                return 5;
            }
            
            if(count > 1){
                return 6;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 7;
    }

    public int deleteByRid(int rid) {
        try {
            String query = "DELETE FROM  "+TABLE_NAME+ " "
                    + " WHERE rid = ?";

            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         
            prepStatement.setInt(1, rid);

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
}
