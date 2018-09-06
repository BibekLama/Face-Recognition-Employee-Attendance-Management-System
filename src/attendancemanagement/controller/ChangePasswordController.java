package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.service.UserService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;


public class ChangePasswordController implements Initializable {

    @FXML
    private PasswordField oldPassField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField reTypePassField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
   

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void changePassword(ActionEvent event) {
        UserService service = new UserService();
        if(!oldPassField.getText().isEmpty()){
            if(!newPassField.getText().isEmpty()){
                if(!reTypePassField.getText().isEmpty()){
                    if(newPassField.getText().equals(reTypePassField.getText())){
                        int status = service.updatePass(AdminWindowController.uid, oldPassField.getText(),newPassField.getText());
                        showMessage(status);
                    }else{
                        UIUtils.warningNotification("Login Failed", "Confirmation Password does not matched with new password.");
                    }
                }else{
                    UIUtils.warningNotification("Login Failed", "Confirmation Password is empty.");
                }
            }else{
                UIUtils.warningNotification("Login Failed", "New Password is empty.");
            }
        }else{
            UIUtils.warningNotification("Login Failed", "Old Password is empty.");
        }
    }

    @FXML
    private void clearField(ActionEvent event) {
        oldPassField.clear();
        newPassField.clear();
        reTypePassField.clear();
    }

    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.warningNotification("Failed", "Old password is incorrect.");
            }
            break;
            case 1:{
                UIUtils.errorNotification("Failed", "Password has not been changed.");
                
            }
            break;
            case 2:{
                UIUtils.successNotification("Success", "Password has been changed");
                LoginController loginController = new LoginController();
                loginController.logOut();
                stage.hide();
            }
            break;
            case 3:{
                UIUtils.warningNotification("Error", "Multiple user password has been changed.\n"
                        + "check database");
            }
            break;
        }
    }
    
}
