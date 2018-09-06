package attendancemanagement.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class DashboardTable {
    private IntegerProperty day;
    private IntegerProperty monthOneVal;
    private IntegerProperty monthTwoVal;
    private IntegerProperty monthThreeVal;

    public DashboardTable() {
        this.day = new SimpleIntegerProperty();
        this.monthOneVal = new SimpleIntegerProperty();
        this.monthTwoVal = new SimpleIntegerProperty();
        this.monthThreeVal = new SimpleIntegerProperty();
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

    public int getMonthOneVal() {
        return monthOneVal.get();
    }

    public IntegerProperty monthOneValProperty() {
        return monthOneVal;
    }

    public void setMonthOneVal(int monthOneVal) {
        this.monthOneVal.set(monthOneVal);
    }

    public int getMonthTwoVal() {
        return monthTwoVal.get();
    }

    public IntegerProperty monthTwoValProperty() {
        return monthTwoVal;
    }

    public void setMonthTwoVal(int monthTwoVal) {
        this.monthTwoVal.set(monthTwoVal);
    }

    public int getMonthThreeVal() {
        return monthThreeVal.get();
    }

    public IntegerProperty monthThreeValProperty() {
        return monthThreeVal;
    }

    public void setMonthThreeVal(int monthThreeVal) {
        this.monthThreeVal.set(monthThreeVal);
    }
}
