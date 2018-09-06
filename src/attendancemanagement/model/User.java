package attendancemanagement.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class User {
    private IntegerProperty uid;
    private StringProperty username;
    private StringProperty password;
    private StringProperty employee;
    private StringProperty role;
    private ObjectProperty<LocalDateTime> added_date;
    private ObjectProperty<LocalDateTime> last_modified_date;

    public User() {
        this.uid = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.employee = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.added_date = new SimpleObjectProperty<>();
        this.last_modified_date = new SimpleObjectProperty<>();
    }

    public int getUid() {
        return uid.get();
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }
    
    public IntegerProperty uidProperty(){
        return uid;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
    
    public StringProperty usernameProperty(){
        return username;
    }

    public String getPassword() {
        return password.get();
    }
    
    public void setPassword(String password) {
        this.password.set(password);
    }
    
    public StringProperty passwordProperty(){
        return password;
    }
    
    public String getEmployee() {
        return employee.get();
    }
    
    public void setEmployee(String employee) {
        this.employee.set(employee);
    }
    
    public StringProperty employeeProperty(){
        return employee;
    }
    
    public String getRole() {
        return role.get();
    }
    
    public void setRole(String role) {
        this.role.set(role);
    }
    
    public StringProperty roleProperty(){
        return role;
    }

    public LocalDateTime getAdded_date() {
        return added_date.get();
    }

    public void setAdded_date(LocalDateTime added_date) {
        this.added_date.set(added_date);
    }
    
    
    public ObjectProperty<LocalDateTime> added_dateProperty(){
        return added_date;
    }

    public LocalDateTime getLast_modified_date() {
        return last_modified_date.get();
    }

    public void setLast_modified_date(LocalDateTime last_modified_date) {
        this.last_modified_date.set(last_modified_date);
    }
    
    public ObjectProperty<LocalDateTime> last_modified_dateProperty(){
        return last_modified_date;
    }

}
