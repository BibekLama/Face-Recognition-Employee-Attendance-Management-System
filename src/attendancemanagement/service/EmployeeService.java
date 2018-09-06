package attendancemanagement.service;

import attendancemanagement.database.AssignedJobCatDAO;
import attendancemanagement.database.AssignedUserDAO;
import attendancemanagement.utils.UIUtils;
import attendancemanagement.database.EmployeeDAO;
import attendancemanagement.database.EmployeeDBO;
import attendancemanagement.model.MyAreaChart;
import attendancemanagement.model.Employee;
import attendancemanagement.model.JobCategory;
import attendancemanagement.model.MyPieChart;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class EmployeeService {
    
    private ObservableList<Employee> recentEmployee;
    
    public ObservableList<Employee> getRecentEmployee() {
        EmployeeDAO dao = new EmployeeDAO();
        recentEmployee = FXCollections.observableArrayList();
        for(EmployeeDBO dbo: dao.selectRecentEmps()){
            Employee emp = new Employee();
            emp.setEmpid(dbo.getEmpid());
            emp.setFname(dbo.getEmp_firstname());
            emp.setLname(dbo.getEmp_lastname());
            emp.setAvatar(dbo.getEmp_avatar());
            recentEmployee.add(emp);  
        }
        return recentEmployee;
    }
    
    public ObservableList<Employee> getAllEmployees() {
        EmployeeDAO dao = new EmployeeDAO();
        recentEmployee = FXCollections.observableArrayList();
        for(EmployeeDBO dbo: dao.selectAll()){
            recentEmployee.add(setEmployeeData(dbo));  
        }
        return recentEmployee;
    }
    
    private Long daysDiff(Timestamp t1, Timestamp t2){
        Long diff = t1.getTime() - t2.getTime();
        return (diff / (24 * 60 * 60 * 1000));
    }

    public List<MyAreaChart> getTotalEmployeeByYear() {
        EmployeeDAO dao = new EmployeeDAO();
        return dao.getTotalEmployeeByYear();
    }
    
    public List<MyPieChart> getTotalEmployeeByGender() {
        EmployeeDAO dao = new EmployeeDAO();
        return dao.getTotalEmployeeByGender();
    }

    public ObservableList<Employee> searchEmployee(String searchText) {
        EmployeeDAO dao = new EmployeeDAO();
        recentEmployee = FXCollections.observableArrayList();
        for(EmployeeDBO dbo: dao.serchEmployee(searchText)){
            recentEmployee.add(setEmployeeData(dbo));  
        }
        return recentEmployee;
    }
    
    Employee setEmployeeData(EmployeeDBO dbo){
        Employee emp = new Employee();
        emp.setEmpid(dbo.getEmpid());
        emp.setEmpcode(dbo.getEmp_code());
        emp.setFname(dbo.getEmp_firstname());
        emp.setLname(dbo.getEmp_lastname());
        emp.setAvatar(dbo.getEmp_avatar());
        emp.setGender(dbo.getEmp_gender());
        emp.setMobileNumber(dbo.getEmp_mobile_no());
        
        JobCategoryService catService = new JobCategoryService();
        AssignedJobCatDAO assign = new AssignedJobCatDAO();
        int jid = assign.getJobCategory(dbo.getEmpid());
        
        emp.setJobTitle(jid);
        
        emp.setPhone(dbo.getEmp_phone_no());
        emp.setCity(dbo.getEmp_city());
        emp.setDistrict(dbo.getEmp_district());
        emp.setZone(dbo.getEmp_zone());
        emp.setAddress(dbo.getEmp_address());
        emp.setSalary(dbo.getEmp_salary());
        emp.setEmail(dbo.getEmp_email());

        emp.setAddedDate(dbo.getAdded_date().toLocalDateTime());

        emp.setUpdateDate(dbo.getLast_modified_date().toLocalDateTime());

        emp.setStatus(dbo.getEmp_status());
        Date dob = new Date(dbo.getEmp_dob().getTime());
        emp.setDob(dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        emp.setNationalid(dbo.getEmp_national_id());
        
        return emp;
    }

    public int UpdateEmployee(Employee model) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(model.getEmpcode());
        dbo.setEmp_firstname(model.getFname());
        dbo.setEmp_lastname(model.getLname());
        dbo.setEmp_national_id(model.getNationalid());
        java.sql.Date dob = java.sql.Date.valueOf(model.getDob());
        dbo.setEmp_dob(dob);
        dbo.setEmp_gender(model.getGender());
        dbo.setEmp_mobile_no(model.getMobileNumber());
        dbo.setEmp_salary(model.getSalary());
        dbo.setEmp_status(model.getStatus());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        EmployeeDAO dao = new EmployeeDAO();
        return dao.UpdateEmployee(dbo);
    }
    
    public int assignJob(String empCode, int jid){
        EmployeeDAO dao = new EmployeeDAO();
        int empid = 0;
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(empCode);
        EmployeeDBO emp = dao.selectByEmpCode(dbo);
        empid = emp.getEmpid();
        
        AssignedJobCatDAO jdao = new AssignedJobCatDAO();
      
        int Jid = jdao.getJobCategory(empid);
        
        if(Jid > 0){
            if(Jid != jid){
                return jdao.updateAssignJobCat(empid, jid, Timestamp.valueOf(LocalDateTime.now()));
            }
        } 
        else{
            return jdao.insertAssignJobCat(empid, jid, Timestamp.valueOf(LocalDateTime.now()));
        }
        return 0;
    }

    public int addEmployee(Employee model) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(model.getEmpcode());
        dbo.setEmp_firstname(model.getFname());
        dbo.setEmp_lastname(model.getLname());
        dbo.setEmp_national_id(model.getNationalid());
        java.sql.Date dob = java.sql.Date.valueOf(model.getDob());
        dbo.setEmp_dob(dob);
        dbo.setEmp_gender(model.getGender());
        dbo.setEmp_mobile_no(model.getMobileNumber());
        JobCategoryService catService = new JobCategoryService();
        String jobTitle = "n/a";
        JobCategory cat = new JobCategory();
        cat = catService.getJobCategory(dbo.getEmpid());
        if(cat != null){
            jobTitle = cat.getTitle();
        }
        dbo.setEmp_salary(model.getSalary());
        dbo.setEmp_status(model.getStatus());
        dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
        File file = new File(".\\src\\frapplication\\res\\images\\avatar_other.png");
        try {
            dbo.setEmp_avatar(file.getCanonicalFile().toURI().toString());
        } catch (IOException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        EmployeeDAO dao = new EmployeeDAO();
        int empNum = dao.getEmployeeCount(dbo);
        if( empNum == 1){
            UIUtils.errorNotification("Add Incomplete!", "The employee with given Employee ID is"
                            + "already exists in the database.");
        }
        else if(empNum > 1){
            UIUtils.errorNotification("Add Incomplete!", "There are multiple employee with same"
                            + "same Employee ID.");
        }
        else{
            return dao.addEmployee(dbo);
        }
        return 0;
    }

    public int activateEmployee(Employee model) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(model.getEmpcode());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        EmployeeDAO dao = new EmployeeDAO();
        if(model.getStatus().equals("Active")){
            return dao.deActivate(dbo);
        }
        if(model.getStatus().equals("Deactive")){
            return dao.activate(dbo);
        }
        
        if(model.getStatus().equals("Suspend")){
            return dao.activate(dbo);
        }
        return 0;
    }

    public int suspendEmployee(Employee model) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(model.getEmpcode());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        EmployeeDAO dao = new EmployeeDAO();
        return dao.suspend(dbo);
    }

    public int deleteEmployee(Employee model) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(model.getEmpcode());
        EmployeeDAO dao = new EmployeeDAO();
        
        AssignedUserDAO assignUsr = new AssignedUserDAO();
        assignUsr.deleteUserAssignedByEmpid(model.getEmpid());
        AssignedJobCatDAO assignJob = new AssignedJobCatDAO();
        assignJob.deleteJobCatAssignedByEmpid(model.getEmpid());
        if(dao.delete(dbo) > 0){
            return 1;
        }
        
        return 0;
    }

    public Employee getEmployeeData(String code) {
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmp_code(code); 
        EmployeeDBO data = dao.selectByEmpCode(dbo);
        if(data != null){
            return setEmployeeData(data);
        }else{
            return null;
        }
        
    }
    
    public Employee getEmployeeData(int id) {
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmpid(id); 
        
        EmployeeDBO data = dao.selectByEmpID(dbo);
        if(data != null){
            return setEmployeeData(data);
        }
        else{
            return null;
        }
    }

    public int updateEmpDetail(Employee emp) {
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setEmpid(emp.getEmpid());
        dbo.setEmp_national_id(emp.getNationalid());
        dbo.setEmp_firstname(emp.getFname());
        dbo.setEmp_lastname(emp.getLname());
        dbo.setEmp_dob(java.sql.Date.valueOf(emp.getDob()));
        dbo.setEmp_gender(emp.getGender());
        dbo.setEmp_mobile_no(emp.getMobileNumber());
        dbo.setEmp_phone_no(emp.getPhone());
        dbo.setEmp_email(emp.getEmail());
        dbo.setEmp_city(emp.getCity());
        dbo.setEmp_district(emp.getDistrict());
        dbo.setEmp_zone(emp.getZone());
        dbo.setEmp_address(emp.getAddress());
        dbo.setEmp_salary(emp.getSalary());
        dbo.setEmp_avatar(emp.getAvatar());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        
       if(dao.updateEmpDetail(dbo) > 0){
           
            AssignedJobCatDAO jdao = new AssignedJobCatDAO();

            int Jid = jdao.getJobCategory(emp.getEmpid());

            if(Jid > 0){
                if(Jid != emp.getJobTitle()){
                    if(jdao.updateAssignJobCat(emp.getEmpid(), emp.getJobTitle(), Timestamp.valueOf(LocalDateTime.now())) > 0){
                        return 1;
                    }else{
                        return 2;
                    }
                }
                return 1;
            } 
            else{
                if(jdao.insertAssignJobCat(emp.getEmpid(), emp.getJobTitle(), Timestamp.valueOf(LocalDateTime.now())) > 0){
                    return 1;
                }else{
                    return 3;
                }
            }
       }else{
           return 0;
       }
    }

}
