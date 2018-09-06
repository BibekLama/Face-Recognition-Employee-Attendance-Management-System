package attendancemanagement.controller;

import attendancemanagement.service.ScreensController;
import attendancemanagement.utils.UIUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


public class AdminWindowController implements Initializable {

    public static int uid;
    
    public static Stage adminStage;
    
    private ScreensController screenController;
    
    public static String screenID;
    
    public static String DASHBOARDFXML = "/attendancemanagement/view/DashboardScreen.fxml";
    public static String DASHBOARDSCREENID = "DashboardScreen";
    
    public static String EMPLOYEEFXML = "/attendancemanagement/view/EmployeeScreen.fxml";
    public static String EMPLOYEESCREENID = "EmployeeScreen";

    public static String ATTENDANCEFXML = "/attendancemanagement/view/AttendanceScreen.fxml";
    public static String ATTENDANCESCREENID = "AttendanceScreen";
    
    
    public static String CAMERASCREENFXML = "/attendancemanagement/view/CameraScreen.fxml";
    public static String CAMERASCREENID = "TrainFaceScreen";

    @FXML
    private MenuBar myMenuBar;
    @FXML
    private StackPane MainContainer;
    @FXML
    private AnchorPane AdminWindow;
    @FXML
    private MenuItem reloadAll;
    
    
    private String currentScreen = "DashboardScreen";

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        screenController = new ScreensController();
        
        screenController.LoadScreen(DASHBOARDSCREENID, DASHBOARDFXML);
        screenController.LoadScreen(EMPLOYEESCREENID, EMPLOYEEFXML);
        screenController.LoadScreen(ATTENDANCESCREENID, ATTENDANCEFXML);
        screenController.LoadScreen(CAMERASCREENID, CAMERASCREENFXML);
        
        screenController.setScreen(DASHBOARDSCREENID, MainContainer);
     
    }    

    @FXML
    private void openDashboardScreen(ActionEvent event) {
        screenController.setScreen(DASHBOARDSCREENID, MainContainer);
        currentScreen = DASHBOARDSCREENID;
    }

    @FXML
    private void openEmployeeScreen(ActionEvent event) {
        screenController.setScreen(EMPLOYEESCREENID, MainContainer);
        currentScreen = EMPLOYEESCREENID;
    }

    @FXML
    private void reloadAll(ActionEvent event) {
        
        screenController.unloadScene(EMPLOYEESCREENID);
        screenController.unloadScene(ATTENDANCESCREENID);
        screenController.unloadScene(CAMERASCREENID);
        screenController.unloadScene(DASHBOARDSCREENID);
        
        screenController = new ScreensController();
        screenController.LoadScreen(DASHBOARDSCREENID, DASHBOARDFXML);
        screenController.LoadScreen(EMPLOYEESCREENID, EMPLOYEEFXML);
        screenController.LoadScreen(ATTENDANCESCREENID, ATTENDANCEFXML);
        screenController.LoadScreen(CAMERASCREENID, CAMERASCREENFXML);
        
        screenController.setScreen(currentScreen, MainContainer);

    }

    @FXML
    private void openAttendanceScreen(ActionEvent event) {
        screenController.setScreen(ATTENDANCESCREENID, MainContainer);
        currentScreen = ATTENDANCESCREENID;
    }

    @FXML
    private void openTrainFaces(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminWindowController.class.getResource(
                    "/attendancemanagement/view/TrainFaceScreen.fxml"));
            Parent root = loader.load();
            TrainFaceScreenController trainController = loader.getController();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("TRAIN FACES");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            trainController.setTrainFaceStage(window);
           
            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(final WindowEvent arg0) {
                    trainController.setClosed();
                }
            });
            
            window.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openPreferences(ActionEvent event) {
//        System.out.println("Preferences...");
        try {
            FXMLLoader loader = new FXMLLoader(AdminWindowController.class.getResource(
                    "/attendancemanagement/view/PreferenceScreen.fxml"));
            Parent root = loader.load();
            PreferenceScreenController prefController = loader.getController();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("PREFERENCES");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            prefController.setPreferenceStage(window);
            prefController.displayChangePasswordContent();
            window.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openCameraScreen(ActionEvent event) {
        screenController.setScreen(CAMERASCREENID, MainContainer);
        currentScreen = CAMERASCREENID;
    }

    @FXML
    private void setIntegratedCamera(ActionEvent event) {
        CameraScreenController.setDeviceId(0);
    }
    
    private int numberOfCameras() {
        int number = 0;
        while (true){
            VideoCapture cap = new VideoCapture();
            cap.open(number);
            if (cap.isOpened()){
                Mat frame = new Mat();
                cap.read(frame); 
                if (!frame.empty()) {
                    number++;  
                }
                cap.release();
            }
            else{ break; }
        }
        return number;
    }
    
    private void showDeviceList() {
        int cameraCount = numberOfCameras();
        
        for(int i = 0; i<=cameraCount; i++){
           
        }
    }

    @FXML
    private void openLogoutDialog(ActionEvent event) {
        boolean response = UIUtils.confirmNotification("Confirm Logout",
                "This will logout you from the Administrator Panel.",
                "Are you ok with this?");
        if(response){
            LoginController loginController = new LoginController();
            loginController.logOut();
            AdminWindow.getScene().getWindow().hide();
        }
    }
    
  
}
