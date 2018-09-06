package attendancemanagement.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class MyPieChart {
    private StringProperty gender;
    private IntegerProperty number;

    public MyPieChart() {
        this.gender = new SimpleStringProperty();
        this.number = new SimpleIntegerProperty();
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
    
    public StringProperty genderProperty() {
        return gender;
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int number) {
        this.number.set(number);
    }
    
    public IntegerProperty numberProperty() {
        return number;
    }
    
}
