package attendancemanagement.controller;

import attendancemanagement.model.User;
import attendancemanagement.service.ScreensController;
import attendancemanagement.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginController implements Initializable{
    public static String DASHBOARD_FXML = "/attendancemanagement/view/Dashboard.fxml";
    public static String DASHBOARD_SCENE_ID = "DashboardScene";
    
    public static String EMPLOYEE_FXML = "/attendancemanagement/view/Employee.fxml";
    public static String EMPLOYEE_SCENE_ID = "EmployeeScene";
    
    private ScreensController mainContainer;
    
    private User user;
    
    private int uid;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User login(String name, String pass) {
        user = new User();
        user.setUsername(name);
        user.setPassword(pass);
        System.out.println("Username="+name);
        System.out.println("Password="+pass);
        UserService service = new UserService();
        User data = service.login(user);
        
        return data;
    }

    public void openDashboard(Stage loginStage) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/attendancemanagement/view/AdminWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            AdminWindowController.uid = uid;
            Scene scene = new Scene(root,980,680);
            stage.setScene(scene);
            AdminWindowController.adminStage = stage;
            stage.show();
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            }); 
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex.getCause());
        }
     
    }

    void logOut() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/attendancemanagement/view/LoginScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(450);
            stage.setResizable(true);
            stage.setTitle("Attendance Management System");
            AdminWindowController.adminStage.hide();
            stage.show();
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
