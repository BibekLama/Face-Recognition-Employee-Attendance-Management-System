package attendancemanagement.database;

import attendancemanagement.model.AttendanceData;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class AttendanceDAO {
    
    private final String TABLE_NAME = "attendances";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;

   

    public List<AttendanceDBO> getAllAttendances() {
        List<AttendanceDBO> attendanceDBOs = new ArrayList<>();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY time DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceDBO dbo = new AttendanceDBO();
                dbo.setEmpid(res.getInt("emp_id"));
                dbo.setTime(res.getTimestamp("time"));
                dbo.setStatus(res.getString("status"));
                attendanceDBOs.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return attendanceDBOs;
    }

    public ObservableList<AttendanceData> getTotalAttendanceOfMonth(String month) {
       
        ObservableList<AttendanceData> datas = FXCollections.observableArrayList();
        try {
            String query = "SELECT EXTRACT(DAY FROM time) AS day, COUNT(time) AS total FROM "+TABLE_NAME+" "
                    + "WHERE EXTRACT(YEAR_MONTH FROM time)='"+month+"'"
                    + " GROUP BY day";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceData data = new AttendanceData();
                data.setDay(res.getInt("day"));
                data.setTotal(res.getInt("total"));
                datas.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return datas;
    }

    public ObservableList<AttendanceData> getTotalAttendanceOfDay(int i) {
        ObservableList<AttendanceData> datas = FXCollections.observableArrayList();
        try {
            String query = "SELECT EXTRACT(YEAR_MONTH FROM time) AS month,  COUNT(time) AS total FROM "+TABLE_NAME+" "
                    + "WHERE EXTRACT(DAY FROM time)='"+i+"'"
                    + " GROUP BY month";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceData data = new AttendanceData();
                data.setDay(i);
                data.setMonth(res.getString("month"));
                data.setTotal(res.getInt("total"));
                datas.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return datas;
    }

    public List<AttendanceDBO> getTodayAttendance(String yearMonth, String day) {
        List<AttendanceDBO> dbos = new ArrayList<>();
        try {
            String query = "SELECT  * FROM "+TABLE_NAME+" "
                    + "WHERE EXTRACT(DAY FROM time)='"+day+"' "
                    + "AND EXTRACT(YEAR_MONTH FROM time)='"+yearMonth+"' "
                    + " ORDER BY time DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceDBO data = new AttendanceDBO();
                data.setEmpid(res.getInt("emp_id"));
                data.setTime(res.getTimestamp("time"));
                data.setStatus(res.getString("status"));
                dbos.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return dbos;
    }
    
    public List<AttendanceDBO> getTodayAttendance(int empid, String yearMonth, String day) {
      
        List<AttendanceDBO> dbos = new ArrayList<>();
        try {
            String query = "SELECT  * FROM "+TABLE_NAME+" "
                    + "WHERE EXTRACT(DAY FROM time)='"+day+"' "
                    + "AND EXTRACT(YEAR_MONTH FROM time)='"+yearMonth+"' "
                    + "AND emp_id ="+empid+" "
                    + " ORDER BY time DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceDBO data = new AttendanceDBO();
                data.setEmpid(res.getInt("emp_id"));
                data.setTime(res.getTimestamp("time"));
                data.setStatus(res.getString("status"));
                dbos.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return dbos;
    }

    public boolean insertAttendanceTime(AttendanceDBO dbo) {
        
        int count = 0;
        try{
            String query = "INSERT INTO "+TABLE_NAME+" ( "
                    + "emp_id, "
                    + "time, "
                    + "status )"
                    + " VALUE(?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setInt(1, dbo.getEmpid());
            prepStatement.setTimestamp(2, dbo.getTime());
            prepStatement.setString(3, dbo.getStatus());

            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);

            if(count > 0 ){
                return true;
            }
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return false;
    }

    public ObservableList<AttendanceDBO> getEmpAttendances(int empid) {
        ObservableList<AttendanceDBO> attendanceDBOs = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE emp_id ="+empid+" ORDER BY time DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceDBO dbo = new AttendanceDBO();
                dbo.setEmpid(res.getInt("emp_id"));
                dbo.setTime(res.getTimestamp("time"));
                dbo.setStatus(res.getString("status"));
                attendanceDBOs.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return attendanceDBOs;
    }
    
    public ObservableList<AttendanceDBO> getEmpAttendances(String value) {
        ObservableList<AttendanceDBO> attendanceDBOs = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE EXTRACT(YEAR FROM time)='"+value+"' "
                    + " OR EXTRACT(MONTH FROM time) ='"+value+"'"
                    + " OR EXTRACT(DAY FROM time) ='"+value+"'"
                    + " OR emp_id = (SELECT emp_id FROM employees WHERE emp_code = '"+value+"'"
                    + " OR CONCAT(emp_firstname,' ',emp_lastname) ='"+value+"')"
                    + "ORDER BY time DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                AttendanceDBO dbo = new AttendanceDBO();
                dbo.setEmpid(res.getInt("emp_id"));
                dbo.setTime(res.getTimestamp("time"));
                dbo.setStatus(res.getString("status"));
                attendanceDBOs.add(dbo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return attendanceDBOs;
    }

    public int getTotalMaxOfMonth(String month) {
        try {
            String query = "SELECT COUNT(time) AS total FROM "+TABLE_NAME+" "
                    + "WHERE EXTRACT(YEAR_MONTH FROM time)='"+month+"'"
                    + " GROUP BY (EXTRACT(DAY FROM time)) ORDER BY total DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return 0;
    }

}