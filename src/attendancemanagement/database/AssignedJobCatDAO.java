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


public class AssignedJobCatDAO {
    private final String TABLE_NAME = "assigned_job_category";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;
    
    public int insertAssignJobCat(int empid, int jid, Timestamp date) {
        try{
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "emp_id,"
                    + "jid,"
                    + "assigned_date) "
                    + " VALUE( ?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setInt(1, empid);
            prepStatement.setInt(2, jid);
            prepStatement.setTimestamp(3, date);
            // execute the java preparedstatement
            return prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int updateAssignJobCat(int empid, int jid, Timestamp date) {
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "jid = ?,"
                    + "assigned_date = ? "
                    + " WHERE emp_id = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setInt(1, jid);
            prepStatement.setTimestamp(2, date);
             prepStatement.setInt(3, empid);
            // execute the java preparedstatement
            return prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int getJobCategory(int empid) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+"  WHERE emp_id="+empid+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("jid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public int deleteJobCatAssignedByEmpid(int empid) {
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
    
    public int deleteJobCatAssignedByJid(int jid) {
        try {
            String query = "DELETE FROM "+TABLE_NAME+" WHERE jid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, jid);
            return prepStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssignedRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
