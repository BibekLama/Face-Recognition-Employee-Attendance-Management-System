package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.utils.Utils;
import attendancemanagement.model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class LoginScreenController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    
    private LoginController loginController;
    @FXML
    private Label unameValidator;
    @FXML
    private Label passValidator;
    @FXML
    private AnchorPane LoginScreen;
    @FXML
    private StackPane loginStackPane;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loginController = new LoginController();
        usernameField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(!newValue && usernameField.getText().isEmpty()){
                unameValidator.setText("Username is required.");
                unameValidator.setVisible(true);
                usernameField.getStyleClass().add("textfield-error");
            }
            else{
                unameValidator.setVisible(false);
                usernameField.getStyleClass().remove("textfield-error");
            }
        });
        
        passwordField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(!newValue && passwordField.getText().isEmpty()){
                passValidator.setText("Password is required.");
                passValidator.setVisible(true);
                passwordField.getStyleClass().add("textfield-error");
            }
            else
            {
                passValidator.setVisible(false);
                passwordField.getStyleClass().remove("textfield-error");
            }
        }); 
    }    

    @FXML
    private void doLogin(ActionEvent event) {
        loginAction();
    }
    
    private void loginAction(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(!username.isEmpty() && !password.isEmpty()){
            User res = loginController.login(username,Utils.hashPass(password));
            if(res != null){
                if(username.equals(res.getUsername()) && Utils.hashPass(password).equals(res.getPassword())){
                    loginController.setUid(res.getUid());
                    loginController.openDashboard((Stage) LoginScreen.getScene().getWindow());
                }

            }else{
                UIUtils.errorNotification("Login Failed", "User doesn't exist. Username or Password Incorrect!");
            }
        }else{
            UIUtils.errorNotification("Login Failed", "Username and Password are required.");
        }
    }
   
    @FXML
    private void resetField(ActionEvent event) {
        usernameField.clear();
        passwordField.clear();
    }
    
}
