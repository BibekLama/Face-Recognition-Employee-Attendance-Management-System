package attendancemanagement.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class MyAreaChart {
    private StringProperty year;
    private StringProperty employees;
    private StringProperty attendances;

    public MyAreaChart(){
        this.year = new SimpleStringProperty();
        this.employees = new SimpleStringProperty();
        this.attendances = new SimpleStringProperty();
    }
  

    public String getYear() {
        return year.get();
    }

    public void setYear(String year) {
        this.year.set(year);
    }
    
    public StringProperty yearProperty(){
        return year;
    }

    public String getEmployees() {
        return employees.get();
    }

    public void setEmployees(String employees) {
        this.employees.set(employees);
    }
    
    public StringProperty employeesProperty(){
        return employees;
    }

    public String getAttendances() {
        return attendances.get();
    }

    public void setAttendances(String attendances) {
        this.attendances.set(attendances);
    }
    
    public StringProperty attendancesProperty(){
        return attendances;
    }
}
