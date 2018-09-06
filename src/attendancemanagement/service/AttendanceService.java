package attendancemanagement.service;

import attendancemanagement.database.AttendanceDAO;
import attendancemanagement.database.AttendanceDBO;
import attendancemanagement.model.Attendance;
import attendancemanagement.model.AttendanceData;
import attendancemanagement.model.Employee;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class AttendanceService {
    
    private Calendar calendar;
    private Date now;
    private Timestamp currentTimestamp;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date;

    public AttendanceService() {
        calendar = Calendar.getInstance();
        now = calendar.getTime();
        currentTimestamp =  new Timestamp(now.getTime());
        date = LocalDate.now().toString();
    }

   
    
    public ObservableList<Attendance> getAllAttendances() {
        ObservableList<Attendance> attendanceDatas = FXCollections.observableArrayList();
        AttendanceDAO dao = new AttendanceDAO();
        int i = 0;
        for(AttendanceDBO dbo: dao.getAllAttendances()){
            i++;
            Attendance data = new Attendance();
            EmployeeService empService = new EmployeeService();
            ObservableList<Employee> emps = FXCollections.observableArrayList();
            Employee emp = empService.getEmployeeData(dbo.getEmpid());
            
            data.setSn(i);
            data.setEmpid(emp.getEmpid());
            data.setTime(dbo.getTime().toLocalDateTime());
            data.setStatus(dbo.getStatus());
            attendanceDatas.add(data);
        }
        return attendanceDatas;
    }  

    public boolean clockInAttendance(Employee emp) {
        OptionService service = new OptionService();
        String clockInStart = date+" "+service.getClockTime("Clock In Time Start")+":00";
        String clockInEnd = date+" "+service.getClockTime("Clock In Time End")+":00";
        if(currentTimestamp.after(Timestamp.valueOf(clockInStart))  && 
                currentTimestamp.before(Timestamp.valueOf(clockInEnd))){
            AttendanceDAO dao = new AttendanceDAO();
            AttendanceDBO dbo = new AttendanceDBO();
            dbo.setEmpid(emp.getEmpid());
            dbo.setTime(Timestamp.valueOf(LocalDateTime.now()));
            dbo.setStatus("Clock In");
            if(dao.insertAttendanceTime(dbo)){
                return true;
            } 
        }
        return false;
    }

    public ObservableList<AttendanceData> getTotalAttendanceOfMonth(YearMonth month) {
        AttendanceDAO dao = new AttendanceDAO();
        String yearMonth = month.toString().replace("-", "");
        return dao.getTotalAttendanceOfMonth(yearMonth);
    }

    public ObservableList<AttendanceData> getTotalAttendanceOfDay(int i) {
        AttendanceDAO dao = new AttendanceDAO();
        return dao.getTotalAttendanceOfDay(i);
    }

    public ObservableList<Attendance> getTodayAttendance(String yearMonth, String day) {
        ObservableList<Attendance> attendanceDatas = FXCollections.observableArrayList();
        AttendanceDAO dao = new AttendanceDAO();
        int i = 1;
        for(AttendanceDBO dbo: dao.getTodayAttendance(yearMonth,day)){
            Attendance data = new Attendance();
            data.setSn(i);
            data.setEmpid(dbo.getEmpid());
            data.setTime(dbo.getTime().toLocalDateTime());
            data.setStatus(dbo.getStatus());
            attendanceDatas.add(data);
            i++;
        }
        return attendanceDatas;
    }
    
    public ObservableList<Attendance> getTodayAttendance(int empid, String yearMonth, String day) {
        ObservableList<Attendance> attendanceDatas = FXCollections.observableArrayList();
        AttendanceDAO dao = new AttendanceDAO();
        int i = 1;
        for(AttendanceDBO dbo: dao.getTodayAttendance(empid, yearMonth,day)){
            Attendance data = new Attendance();
            data.setSn(i);
            data.setEmpid(dbo.getEmpid());
            data.setTime(dbo.getTime().toLocalDateTime());
            data.setStatus(dbo.getStatus());
            attendanceDatas.add(data);
            i++;
        }
        return attendanceDatas;
    }

    public boolean clockOutAttendance(Employee emp) {
        
        OptionService service = new OptionService();
        String clockOutStart = date+" "+service.getClockTime("Clock Out Time Start")+":00";
        String clockOutEnd = date+" "+service.getClockTime("Clock Out Time End")+":00";
        if(currentTimestamp.after(Timestamp.valueOf(clockOutStart))  && 
                currentTimestamp.before(Timestamp.valueOf(clockOutEnd))){
            AttendanceDAO dao = new AttendanceDAO();
            AttendanceDBO dbo = new AttendanceDBO();
            dbo.setEmpid(emp.getEmpid());
            dbo.setTime(Timestamp.valueOf(LocalDateTime.now()));
            dbo.setStatus("Clock Out");
            if(dao.insertAttendanceTime(dbo)){
                return true;
            }  
        }
        return false;
    }

    public ObservableList<Attendance> getEmployeeAttendance(int empid) {
        ObservableList<Attendance> attendanceDatas = FXCollections.observableArrayList();
        AttendanceDAO dao = new AttendanceDAO();
        int i = 0;
        for(AttendanceDBO dbo: dao.getEmpAttendances(empid)){
            i++;
            Attendance data = new Attendance();
            EmployeeService empService = new EmployeeService();
            Employee emp = empService.getEmployeeData(dbo.getEmpid());
            
            data.setSn(i);
            data.setEmpid(emp.getEmpid());
            data.setTime(dbo.getTime().toLocalDateTime());
            data.setStatus(dbo.getStatus());
            attendanceDatas.add(data);
        }
        return attendanceDatas;
    }

    public ObservableList<Attendance> searchAttendance(String newValue) {
        ObservableList<Attendance> attendanceDatas = FXCollections.observableArrayList();
        AttendanceDAO dao = new AttendanceDAO();
        int i = 0;
        for(AttendanceDBO dbo: dao.getEmpAttendances(newValue)){
            i++;
            Attendance data = new Attendance();
            EmployeeService empService = new EmployeeService();
            Employee emp = empService.getEmployeeData(dbo.getEmpid());
            
            data.setSn(i);
            data.setEmpid(emp.getEmpid());
            data.setTime(dbo.getTime().toLocalDateTime());
            data.setStatus(dbo.getStatus());
            attendanceDatas.add(data);
        }
        return attendanceDatas;
    }

    public int getMaxTotalOfMonth(YearMonth month) {
        AttendanceDAO dao = new AttendanceDAO();
        String yearMonth = month.toString().replace("-", "");
        return dao.getTotalMaxOfMonth(yearMonth);
    }

}
