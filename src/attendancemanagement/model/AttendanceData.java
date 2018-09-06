package attendancemanagement.model;

import java.time.MonthDay;
import java.time.YearMonth;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class AttendanceData {
    private IntegerProperty day;
    private StringProperty month;
    private IntegerProperty total;

    public AttendanceData() {
        this.day = new SimpleIntegerProperty();
        this.month = new SimpleStringProperty();
        this.total = new SimpleIntegerProperty();
    }

    public int getDay() {
        return day.get();
    }
    
    public IntegerProperty dayProperty() {
        return day;
    }

    public void setDay(int day) {
        this.day.set(day);
    }
    
    public String getMonth() {
        return month.get();
    }

    public StringProperty monthOneValProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }
    
    
    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }
    
    
    
}
