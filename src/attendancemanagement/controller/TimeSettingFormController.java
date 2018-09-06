package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Option;
import attendancemanagement.service.OptionService;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class TimeSettingFormController implements Initializable {

    @FXML
    private Label formTitle;
    @FXML
    private Text formSubTitle;
    @FXML
    private JFXTimePicker timeStartField;
    @FXML
    private JFXTimePicker timeEndField;
    @FXML
    private Button submitButton;

    private boolean arrivalMode;
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void arrivalTimeForm() {
        arrivalMode = true;
        formTitle.setText("CLOCK IN TIME");
        formSubTitle.setText("Manage clock in time shift for employee.");
        OptionService service = new OptionService();
        Option startTime = service.getOption("Clock In Time Start");
        if(startTime != null){
            if(!startTime.getValue().isEmpty()){
                timeStartField.setValue(LocalTime.parse(startTime.getValue()));
            }
        }
        Option endTime = service.getOption("Clock In Time End");
        if(endTime != null){
            if(!endTime.getValue().isEmpty()){
                timeEndField.setValue(LocalTime.parse(endTime.getValue()));
            }
        }
    }
    
    public void departureTimeForm() {
        arrivalMode = false;
        formTitle.setText("CLOCK OUT TIME");
        formSubTitle.setText("Manage clock out time shift for employee.");
        OptionService service = new OptionService();
        Option startTime = service.getOption("Clock Out Time Start");
        if(startTime != null){
            if(!startTime.getValue().isEmpty()){
                timeStartField.setValue(LocalTime.parse(startTime.getValue()));
            }
        }
        Option endTime = service.getOption("Clock Out Time End");
        if(endTime != null){
            if(!endTime.getValue().isEmpty()){
                timeEndField.setValue(LocalTime.parse(endTime.getValue()));
            }
        }
    }

    @FXML
    private void addTime(ActionEvent event) {
        if(timeStartField.getValue() != null){
            if(timeEndField.getValue() != null){
                if(arrivalMode){
                    addArrivalTime();
                }else{
                    addDepartureTime();
                }
            }else{
                UIUtils.warningNotification("Add Incomplete!", "End At field is empty.");
            }
        }else{
            UIUtils.warningNotification("Add Incomplete!", "Start At field is empty.");
        }
    }

    private void addArrivalTime() {
        Option start = new Option();
        start.setName("Clock In Time Start");
        start.setValue(timeStartField.getValue().format(formatter));
        Option end = new Option();
        end.setName("Clock In Time End");
        end.setValue(timeEndField.getValue().format(formatter));
        OptionService service = new OptionService();
        int status = 0;
        int status1 = service.addStartTime(start);
        int status2 = service.addEndTime(end);
        
        if(status1 == 0 && status2 == 0){
            status = 0;
        }
        if(status1 == 1 && status2 == 1){
            status = 1;
        }
        if(status1 == 2 && status2 == 2){
            status = 2;
        }
        
        if(status1 == 0 && status2 == 1){
            status = 3;
        }
        if(status1 == 0 && status2 == 2){
            status = 4;
        }
        
        if(status1 == 1 && status2 == 0){
            status = 5;
        }
        if(status1 == 1 && status2 == 2){
            status = 6;
        }
        
        if(status1 == 2 && status2 == 0){
            status = 7;
        }
        if(status1 == 2 && status2 == 1){
            status = 8;
        }
       
        showMessage(status);
    }

    private void addDepartureTime() {
        Option start = new Option();
        start.setName("Clock Out Time Start");
        start.setValue(timeStartField.getValue().format(formatter));
        Option end = new Option();
        end.setName("Clock Out Time End");
        end.setValue(timeEndField.getValue().format(formatter));
        OptionService service = new OptionService();
        int status = 0;
        int status1 = service.addStartTime(start);
        int status2 = service.addEndTime(end);
        if(status1 == 0 && status2 == 0){
            status = 0;
        }
        if(status1 == 1 && status2 == 1){
            status = 1;
        }
        if(status1 == 2 && status2 == 2){
            status = 2;
        }
        
        if(status1 == 0 && status2 == 1){
            status = 3;
        }
        if(status1 == 0 && status2 == 2){
            status = 4;
        }
        
        if(status1 == 1 && status2 == 0){
            status = 5;
        }
        if(status1 == 1 && status2 == 2){
            status = 6;
        }
        
        if(status1 == 2 && status2 == 0){
            status = 7;
        }
        if(status1 == 2 && status2 == 1){
            status = 8;
        }
       
        showMessage(status);
        
    }

    private void showMessage(int status) {
       switch(status){
            case 0:{
                UIUtils.errorNotification("Add Incomplete!", "Time has not been added successfully.");
            }
            break;
            case 1:{
                UIUtils.successNotification("Add Complete!", "Start and end time has been added successfully.");
            }
            break;
            case 2:{
                UIUtils.warningNotification("Add Complete!", "Multiple start and end time has been added.\n"
                            + " Start and end time should be unique");
            }
            break;
            case 3:{
                UIUtils.warningNotification("Add Complete!", "Start time has not been added.\n"
                            + " End time has been added successfully.");
            }
            break;
            case 4:{
                UIUtils.warningNotification("Add Complete!", "Start time has not been added.\n"
                            + " Multiple End time has been added");
            }
            break;
            case 5:{
                UIUtils.warningNotification("Add Complete!", "Start time has been added successfully.\n"
                            + " End time has not been added");
            }
            break;
            case 6:{
                UIUtils.warningNotification("Add Complete!", "Start time has been added successfully.\n"
                            + " Multiple End time has been added");
            }
            break;
            case 7:{
                UIUtils.warningNotification("Add Complete!", "Multiple Start time has not been added.\n"
                            + " End time has not been added");
            }
            break;
            case 8:{
                UIUtils.warningNotification("Add Complete!", "Multiple Start time has been added.\n"
                            + " End time has been added");
            }
            break;
       }
    }
    
}
