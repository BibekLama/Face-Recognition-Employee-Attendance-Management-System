package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Role;
import attendancemanagement.service.RoleService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RoleFormController implements Initializable {

    @FXML
    private TextField roleField;
    @FXML
    private TextField displayNameField;
    @FXML
    private Button submitButton;

    private boolean edit;
    
    private int rid;
    
    @FXML
    private AnchorPane roleForm;
    
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void submitForm(ActionEvent event) {
        String role = roleField.getText();
        String name = displayNameField.getText();
        if(!role.isEmpty()){
            if(!role.isEmpty()){
                if(edit){
                    editRole(role,name);
                }else{
                    addRole(role,name);
                }
            }else{
                UIUtils.warningNotification("Add Incomplete!", "Display Name field is empty.");
            }
        }else{
            UIUtils.warningNotification("Add Incomplete!", "Role field is empty.\n"
                            + "Enter unique user's role in the role field.");
        }
    }

    void addNewRole() {
        edit = false;
        submitButton.setText("ADD ROLE");
        roleField.clear();
        displayNameField.clear();
    }

    private void addRole(String role, String name) {
        RoleService service = new RoleService();
        Role data = new Role();
        data.setRole(role);
        data.setDisplay_name(name);
        int status = service.addRole(data);
        
        showMessage(status);
        
    }

    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Add Incomplete!", "New Users's Role has not been"
                            + " added successfully");
            }
            break;
            case 1:{
                UIUtils.successNotification("Add Complete!", "New Users's Role has been"
                            + " successfully added");
                stage.close();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Add Complete!", "Multiple Users's Role has been"
                            + " added \n"
                        + "Users's Role should be unique");
                stage.close();
            }
            break;
            case 4:{
                UIUtils.warningNotification("Users's Role Exist!", "Users's Role is already"
                            + " exists in table\n"
                        + "Users's Role should be unique");
            }
            break;
            case 5:{
                UIUtils.successNotification("Edit Complete!", "Users's Role has been"
                            + " successfully edited");
                stage.close();
            }
            break;
            case 6:{
                UIUtils.warningNotification("Edit Complete!", "Multiple Users's Role has"
                            + " been edited\n"
                        + "Users's Role should be unique");
                stage.close();
            }
            break;
            case 7:{
                UIUtils.errorNotification("Edit Incomplete!", "Users's Role has not been"
                            + " edited successfully");
            }
            break;
        }
    }

    void editRoleFrom() {
        edit = true;
        submitButton.setText("EDIT ROLE");
        RoleService service = new RoleService();
        Role data = service.selectByRid(rid);
        if(data != null){
            roleField.setText(data.getRole());
            displayNameField.setText(data.getDisplay_name());
        }
    }

    private void editRole(String role, String name) {
        RoleService service = new RoleService();
        Role data = new Role();
        data.setRid(rid);
        data.setRole(role);
        data.setDisplay_name(name);
        int status = service.editRole(data);
        showMessage(status);
    }
    
}
