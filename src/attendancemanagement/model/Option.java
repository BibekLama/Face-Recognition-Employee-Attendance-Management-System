package attendancemanagement.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Option {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty value;
    private ObjectProperty<LocalDateTime> added_date;
    private ObjectProperty<LocalDateTime> last_modified_date;

    public Option() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.value = new SimpleStringProperty();
        this.added_date = new SimpleObjectProperty<>();
        this.last_modified_date = new SimpleObjectProperty<>();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }
    
    public IntegerProperty idProperty(){
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getName(){
        return name.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
    
    public String getValue() {
        return value.get();
    }

    public ObjectProperty<LocalDateTime> added_dateProperty() {
        return added_date;
    }

    public void setAdded_date(LocalDateTime added_date) {
        this.added_date.set(added_date);
    }
    
    public LocalDateTime getAdded_date() {
        return added_date.get();
    }

    public ObjectProperty<LocalDateTime> last_modified_dateProperty() {
        return last_modified_date;
    }

    public LocalDateTime getLast_modified_date() {
        return last_modified_date.get();
    }
    
    public void setLast_modified_date(LocalDateTime last_modified_date) {
        this.last_modified_date.set(last_modified_date);
    }
    
    
}
