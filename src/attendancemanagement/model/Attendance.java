package attendancemanagement.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Attendance {
    private IntegerProperty sn;
    private IntegerProperty empid;
    private ObjectProperty<LocalDateTime> time;
    private StringProperty status;

    public Attendance() {
        this.sn = new SimpleIntegerProperty();
        this.empid = new SimpleIntegerProperty();
        this.time = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
    }

    public int getSn() {
        return sn.get();
    }

    public void setSn(int sn) {
        this.sn.set(sn);
    }
    
    public IntegerProperty snProperty() {
        return sn;
    }
    
    public int getEmpid() {
        return empid.get();
    }

    public void setEmpid(int empid) {
        this.empid.set(empid);
    }
    
    public IntegerProperty empidProperty() {
        return empid;
    }

  
    public LocalDateTime getTime() {
        return time.get();
    }

    public void setTime(LocalDateTime time) {
        this.time.set(time);
    }
    
    public ObjectProperty<LocalDateTime> timeProperty() {
        return time;
    }
 
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    
    public StringProperty statusProperty() {
        return status;
    }
    
}
