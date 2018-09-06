package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JobCategoryDAO {
    
    private final String TABLE_NAME = "job_categories";
    private final String RELATION_TABLE_NAME = "assigned_job_category";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;

    
    
    public int getJobCategoryId(int empid) {
        try {
            String query = "SELECT * FROM "+RELATION_TABLE_NAME+"  WHERE emp_id='"+empid+"'";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("jid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

    public List<JobCategoryDBO> selectAll() {
        List<JobCategoryDBO> dbos = new ArrayList<>();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY jid DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                JobCategoryDBO dbo = new JobCategoryDBO();
                dbo.setJid(res.getInt("jid"));
                dbo.setTitle(res.getString("title"));
                dbo.setDisplay_name(res.getString("display_name"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                dbos.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return dbos;
    }

    public int addJobCategory(JobCategoryDBO dbo) {
        try {
            String query = "INSERT INTO "+TABLE_NAME+" ("
                    + "title, "
                    + "display_name, "
                    + "added_date, "
                    + "last_modified_date ) "
                    + "VALUE(?,?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, dbo.getTitle());
            prepStatement.setString(2, dbo.getDisplay_name());
            prepStatement.setTimestamp(3, dbo.getAdded_date());
            prepStatement.setTimestamp(4, dbo.getLast_modified_date());
            int count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }
    
    public int isJobCatExist(String title) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE title='"+title+"'";
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

    public JobCategoryDBO selectJobCatById(int jid) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE jid="+jid+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                JobCategoryDBO dbo = new JobCategoryDBO();
                dbo.setJid(res.getInt("jid"));
                dbo.setTitle(res.getString("title"));
                dbo.setDisplay_name(res.getString("display_name"));
                dbo.setAdded_date(res.getTimestamp("added_date"));
                dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
                return dbo;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return null;
    }

    public int editJobCategory(JobCategoryDBO dbo) {
//        System.out.println(dbo.getJid());
        try {
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "title = ?, "
                    + "display_name = ?, "
                    + "last_modified_date = ? "
                    + "WHERE jid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, dbo.getTitle());
            prepStatement.setString(2, dbo.getDisplay_name());
            prepStatement.setTimestamp(3, dbo.getLast_modified_date());
            prepStatement.setInt(4, dbo.getJid());
            int count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            if(count == 1){
                return 6;
            }
            if(count > 1){
                return 7;
            }
        }catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 5;
    }

    public int deleteJobCategory(int jid) {
        try {
            String query = "DELETE FROM "+TABLE_NAME+" "
                    + "WHERE jid = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setInt(1, jid);
            int count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            if(count == 1){
                return 1;
            }
            if(count > 1){
                return 2;
            }
        }catch (SQLException ex) {
            Logger.getLogger(JobCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

   
}
