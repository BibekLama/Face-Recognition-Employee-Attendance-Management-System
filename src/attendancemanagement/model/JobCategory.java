package attendancemanagement.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class JobCategory {
    private IntegerProperty jid;
    private StringProperty title;
    private StringProperty display_name;
    private ObjectProperty<LocalDateTime> added_date;
    private ObjectProperty<LocalDateTime> last_modified_date;

    public JobCategory() {
        this.jid = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.display_name = new SimpleStringProperty();
        this.added_date = new SimpleObjectProperty<>();
        this.last_modified_date = new SimpleObjectProperty<>();
    }

    public int getJid() {
        return jid.get();
    }

    public void setJid(int jid) {
        this.jid.set(jid);
    }
    
    public IntegerProperty jidProperty(){
        return jid;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public StringProperty titleProperty(){
        return title;
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
