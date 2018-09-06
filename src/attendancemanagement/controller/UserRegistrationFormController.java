package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Employee;
import attendancemanagement.model.User;
import attendancemanagement.model.Role;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.UserService;
import attendancemanagement.service.RoleService;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UserRegistrationFormController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPassField;
    private int empid;
    private int rid;
    private Stage stage;
    private boolean edit;
    
    private int uid;
    
    @FXML
    private Button submitButton;

    public void setUid(int uid) {
        this.uid = uid;
    }

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addUser(ActionEvent event) {
        if(!usernameField.getText().isEmpty()){
            if(edit){
                editUser();
            }else{
                addNewUser();
            }    
        }else{
            UIUtils.warningNotification("Add Incomplete!", "Username field is empty.\n"
                            + "Enter unique username in the username field.");
        } 
    }

    public void userRegistrationForm() {
        edit = false;
        submitButton.setText("ADD USER");
    }

    public void setStage(Stage window) {
        stage = window;
    }

    private void addNewUser() {
        if(!passwordField.getText().isEmpty()){
            if(passwordField.getText().equals(confirmPassField.getText())){
                User user = new User();
                user.setUsername(usernameField.getText());
                user.setPassword(hashPass(passwordField.getText()));
                UserService service = new UserService();
                int status = service.addNewUser(user);
                showMessage(status);
            }else{
                UIUtils.warningNotification("Add Incomplete!", "Confirm password does not match with password.\n"
                                + "Enter correct password in Confirm password field.");
            }
        }else{
            UIUtils.warningNotification("Add Incomplete!", "Password field is empty.\n"
                            + "Enter password in the password field.");
        }  
    }
    
    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.warningNotification("User Exists!", "User account already"
                       + " exists");
            }
            break;
            case 1:{
                UIUtils.successNotification("Add Complete!", "New User account has been"
                       + " added successfully");
                stage.close();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Add Complete!", "Multiple Users account has been"
                        + " added \n"
                        + "Users account should be unique");
                stage.close();
            }
            break;
            case 3:{
                UIUtils.errorNotification("Add Incomplete!", "Users account has not been"
                            + " added successfully");
            }
            break;
            case 4:{
                UIUtils.errorNotification("Already Assigned!", "Users account was already assigned"
                            + " to the employee");
            }
            break;
            case 5:{
                stage.close();
                UIUtils.successNotification("Edit Complete!", "Users account has been edited"
                            + " successfully.");
            }
            break;
            case 6:{
                UIUtils.warningNotification("Edit Complete!", "Multiple Users accounts are edited.\n"
                            + " User account should be unique");
            }
            break;
            case 7:{
                UIUtils.errorNotification("Edit Incomplete!", "Users account has not been"
                            + " edited successfully");
            }
            break;
        }
    }
    
    private String hashPass(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            StringBuffer hPass = new StringBuffer();
            for(byte byt: digest){
                hPass.append(String.format("%02x",byt & 0xff));
            }
            return hPass.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    void userEditForm() {
        edit = true;
        submitButton.setText("EDIT USER");
        UserService service = new UserService();
        User user = service.getUserById(uid);
        if(user != null){
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            passwordField.setDisable(true);
            confirmPassField.setText(user.getPassword());
            confirmPassField.setDisable(true);
        }
    }

    private void editUser() {
        User user = new User();
        user.setUid(uid);
        user.setUsername(usernameField.getText());
        UserService service = new UserService();
        int status = service.updateUser(user);
        showMessage(status);
    }
}
