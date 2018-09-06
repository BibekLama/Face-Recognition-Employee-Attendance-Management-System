package attendancemanagement.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Role {
    private IntegerProperty rid;
    private StringProperty role;
    private StringProperty display_name;
    private ObjectProperty<LocalDateTime> added_date;
    private ObjectProperty<LocalDateTime> last_modified_date;

    public Role() {
        this.rid = new SimpleIntegerProperty();
        this.role = new SimpleStringProperty();
        this.display_name = new SimpleStringProperty();
        this.added_date = new SimpleObjectProperty<>();
        this.last_modified_date = new SimpleObjectProperty<>();
    }

    public int getRid() {
        return rid.get();
    }

    public void setRid(int rid) {
        this.rid.set(rid);
    }
    
    public IntegerProperty ridProperty(){
        return rid;
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
    
    public String getDisplay_name() {
        return display_name.get();
    }

    public void setDisplay_name(String display_name) {
        this.display_name.set(display_name);
    }
    
    public StringProperty display_nameProperty(){
        return display_name;
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
