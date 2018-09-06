package attendancemanagement.database;

import attendancemanagement.utils.DBUtil;
import attendancemanagement.model.MyAreaChart;
import attendancemanagement.model.MyPieChart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EmployeeDAO {
    private final String TABLE_NAME = "employees";
    private Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet res;
    private List<EmployeeDBO> employeeDBOs;
    
    private List<MyAreaChart> areaCharts;
    
    public List<EmployeeDBO> selectRecentEmps() {
        employeeDBOs = new ArrayList<>();
        try {
            String query = "SELECT * FROM "+TABLE_NAME+"  WHERE EXTRACT(YEAR_MONTH FROM added_date)"
                    + "=(SELECT MAX(EXTRACT(YEAR_MONTH FROM added_date)) FROM "+TABLE_NAME+") ORDER BY emp_id DESC";
            
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                employeeDBOs.add(setEmployeeDBO(res));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return employeeDBOs;
    }

    public List<MyAreaChart> getTotalEmployeeByYear() {
        areaCharts = new ArrayList<>();
        try {
            String query = "SELECT EXTRACT(YEAR FROM added_date) AS year, COUNT(emp_id) AS rowcount FROM "+TABLE_NAME+" GROUP BY year DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                MyAreaChart data = new MyAreaChart();
                data.setEmployees(res.getInt("rowcount")+"");
                data.setYear(res.getString("year")+"");
                areaCharts.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return areaCharts;
    }
    
    public List<MyPieChart> getTotalEmployeeByGender() {
        List<MyPieChart> gendersNum = new ArrayList<>();
        try {
            String query = "SELECT emp_gender, COUNT(emp_id) AS rowcount FROM "+TABLE_NAME+" GROUP BY emp_gender";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                MyPieChart data = new MyPieChart();
                data.setGender(res.getString("emp_gender"));
                data.setNumber(res.getInt("rowcount"));
                gendersNum.add(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return gendersNum;
    }
    
    public List<EmployeeDBO> selectAll(){
        employeeDBOs = new ArrayList<>();
        try{
            String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY emp_id DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                employeeDBOs.add(setEmployeeDBO(res));
            }
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        
        return employeeDBOs;
    }

    public List<EmployeeDBO> serchEmployee(String searchText) {
        employeeDBOs = new ArrayList<>();
        try{
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE emp_code LIKE '%"+searchText+"%'"
                    + " OR emp_firstname='"+searchText+"' OR emp_lastname LIKE '%"+searchText+"%'"
                    + " OR CONCAT(emp_firstname,' ',emp_lastname) LIKE '%"+searchText+"%'"
                    + "OR emp_national_id LIKE '%"+searchText+"%' OR emp_mobile_no LIKE '%"+searchText+"%'"
                    + " ORDER BY emp_id DESC";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                employeeDBOs.add(setEmployeeDBO(res));
            }
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return employeeDBOs;
    }
    
    private EmployeeDBO setEmployeeDBO(ResultSet res){
        EmployeeDBO dbo = new EmployeeDBO();
        try {
            dbo.setEmpid(res.getInt("emp_id"));
            dbo.setEmp_code(res.getString("emp_code"));
            dbo.setEmp_firstname(res.getString("emp_firstname"));
            dbo.setEmp_lastname(res.getString("emp_lastname"));
            dbo.setEmp_avatar(res.getString("emp_avatar"));
            Date date = res.getDate("emp_dob");
            dbo.setEmp_dob(date);
            dbo.setEmp_gender(res.getString("emp_gender"));
            dbo.setEmp_email(res.getString("emp_email"));
            dbo.setEmp_mobile_no(res.getString("emp_mobile_no"));
            dbo.setEmp_phone_no(res.getString("emp_phone_no"));
            dbo.setEmp_zone(res.getString("emp_zone"));
            dbo.setEmp_city(res.getString("emp_city"));
            dbo.setEmp_district(res.getString("emp_district"));
            dbo.setEmp_address(res.getString("emp_address"));
            dbo.setEmp_national_id(res.getString("emp_national_id"));
            dbo.setEmp_salary(res.getDouble("emp_salary"));
            dbo.setAdded_date(res.getTimestamp("added_date"));
            dbo.setLast_modified_date(res.getTimestamp("last_modified_date"));
            dbo.setEmp_status(res.getString("emp_status"));
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbo;
    }

    public int UpdateEmployee(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_firstname = ?, "
                    + "emp_lastname = ?, "
                    + "emp_dob = ?, "
                    + "emp_gender = ?, "
                    + "emp_mobile_no = ?,"
                    + "emp_salary = ?,"
                    + "emp_status = ?,"
                    + "emp_national_id = ?,"
                    + "last_modified_date = ?"
                    + " WHERE emp_code = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getEmp_firstname());
            prepStatement.setString(2, dbo.getEmp_lastname());
            prepStatement.setDate(3, dbo.getEmp_dob());
            prepStatement.setString(4, dbo.getEmp_gender());
            prepStatement.setString(5, dbo.getEmp_mobile_no());
            prepStatement.setDouble(6, dbo.getEmp_salary());
            prepStatement.setString(7, dbo.getEmp_status());
            prepStatement.setString(8, dbo.getEmp_national_id());
            prepStatement.setTimestamp(9, dbo.getLast_modified_date());
            prepStatement.setString(10, dbo.getEmp_code());
            

            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    public int addEmployee(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "INSERT INTO "+TABLE_NAME+" "
                    + "("
                    + "emp_code,"
                    + "emp_firstname,"
                    + "emp_lastname,"
                    + "emp_dob,"
                    + "emp_gender,"
                    + "emp_mobile_no,"
                    + "emp_salary,"
                    + "emp_status,"
                    + "emp_avatar,"
                    + "emp_national_id,"
                    + "added_date"
                    + ") "
                    + "VALUE(?,?,?,?,?,?,?,?,?,?,?)";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, dbo.getEmp_code());
            prepStatement.setString(2, dbo.getEmp_firstname());
            prepStatement.setString(3, dbo.getEmp_lastname());
            prepStatement.setDate(4, dbo.getEmp_dob());
            prepStatement.setString(5, dbo.getEmp_gender());
            prepStatement.setString(6, dbo.getEmp_mobile_no());
            prepStatement.setDouble(7, dbo.getEmp_salary());
            prepStatement.setString(8, dbo.getEmp_status());
            prepStatement.setString(9,dbo.getEmp_avatar());
            prepStatement.setString(10,dbo.getEmp_national_id());
            prepStatement.setTimestamp(11,dbo.getAdded_date());

            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    public int getEmployeeCount(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE emp_code='"+dbo.getEmp_code()+"'";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = statement.executeQuery(query);
            while(res.next()){
                count++;
            }
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    public int deActivate(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_status = ?, "
                    + "last_modified_date = ?"
                    + " WHERE emp_code = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, "Deactive");
            prepStatement.setTimestamp(2, dbo.getLast_modified_date());
            prepStatement.setString(3, dbo.getEmp_code());
            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }
    
    public int activate(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_status = ?, "
                    + "last_modified_date = ?"
                    + " WHERE emp_code = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, "Active");
            prepStatement.setTimestamp(2, dbo.getLast_modified_date());
            prepStatement.setString(3, dbo.getEmp_code());
            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }
    

    public int suspend(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_status = ?,"
                    + "last_modified_date = ?"
                    + " WHERE emp_code = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, "Suspend");
            prepStatement.setTimestamp(2, dbo.getLast_modified_date());
            prepStatement.setString(3, dbo.getEmp_code());
            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    public int delete(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "DELETE FROM "+TABLE_NAME+" WHERE emp_code = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            prepStatement.setString(1, dbo.getEmp_code());
            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    public EmployeeDBO selectByEmpCode(EmployeeDBO dbo) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+"  WHERE emp_code='"+dbo.getEmp_code()+"'";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return setEmployeeDBO(res);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        
        return null;
    }

    public EmployeeDBO selectByEmpID(EmployeeDBO dbo) {
        try {
            String query = "SELECT * FROM "+TABLE_NAME+"  WHERE emp_id="+dbo.getEmpid()+"";
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while(res.next()){
                return setEmployeeDBO(res);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        
        return null;
    }

    public int updateEmpDetail(EmployeeDBO dbo) {
        int count = 0;
        try{
            String query = "UPDATE "+TABLE_NAME+" SET "
                    + "emp_firstname = ?, "
                    + "emp_lastname = ?, "
                    + "emp_dob = ?, "
                    + "emp_gender = ?, "
                    + "emp_mobile_no = ?,"
                    + "emp_salary = ?,"
                    + "emp_national_id = ?,"
                    + "emp_phone_no = ?,"
                    + "emp_city = ?,"
                    + "emp_district = ?,"
                    + "emp_zone = ?,"
                    + "emp_email = ?,"
                    + "emp_address = ?,"
                    + "emp_avatar = ?,"
                    + "last_modified_date = ?"
                    + " WHERE emp_id = ?";
            connection = DatabaseManager.getConnection();
            prepStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            prepStatement.setString(1, dbo.getEmp_firstname());
            prepStatement.setString(2, dbo.getEmp_lastname());
            prepStatement.setDate(3, dbo.getEmp_dob());
            prepStatement.setString(4, dbo.getEmp_gender());
            prepStatement.setString(5, dbo.getEmp_mobile_no());
            prepStatement.setDouble(6, dbo.getEmp_salary());
            prepStatement.setString(7, dbo.getEmp_national_id());
            prepStatement.setString(8, dbo.getEmp_phone_no());
            prepStatement.setString(9, dbo.getEmp_city());
            prepStatement.setString(10, dbo.getEmp_district());
            prepStatement.setString(11, dbo.getEmp_zone());
            prepStatement.setString(12, dbo.getEmp_email());
            prepStatement.setString(13, dbo.getEmp_address());
            prepStatement.setString(14, dbo.getEmp_avatar());
            prepStatement.setTimestamp(15, dbo.getLast_modified_date());
            prepStatement.setInt(16, dbo.getEmpid());
            

            // execute the java preparedstatement
            count = prepStatement.executeUpdate();
//            System.out.println("Row Affected: "+count);
            
        }catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
                DBUtil.close(statement);
                DBUtil.close(connection);
        }
        return count;
    }

    
    
}
