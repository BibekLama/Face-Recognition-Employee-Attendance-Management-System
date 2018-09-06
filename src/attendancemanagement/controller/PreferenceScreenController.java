package attendancemanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PreferenceScreenController implements Initializable {

    private TreeView<String> menuTreeView;
    
    @FXML
    private AnchorPane menuContainer;
    
    @FXML
    private AnchorPane prefContainer;
    @FXML
    private AnchorPane preferenceScreen;
    private Stage preferenceStage;
    
    final ImageView chngPassIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/changePass.png"))
        );
    
    final ImageView userIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/userIcon.png"))
        );

    final ImageView jobIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/job.png"))
        );
    
    final ImageView timeIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/time.png"))
        );
    
    final ImageView preferenceIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/preference.png"))
        );
    
    public void setPreferenceStage(Stage preferenceStage) {
        this.preferenceStage = preferenceStage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showMenuTreeView();
        
    }    

    private void showMenuTreeView() {
        
        TreeItem<String> rootItem = new TreeItem<String> ("Preferences",preferenceIcon);
        rootItem.setExpanded(true);
        TreeItem<String> changePass = new TreeItem<String> ("Change Password",chngPassIcon);  
        
        TreeItem<String> manageUsers = new TreeItem<String> ("Manage Users",userIcon); 
        manageUsers.setExpanded(true);
        TreeItem<String> userRole = new TreeItem<String> ("User's Role"); 
        TreeItem<String> users = new TreeItem<String> ("Users"); 
        manageUsers.getChildren().addAll(userRole,users);
        
        TreeItem<String> manageJobCat = new TreeItem<String> ("Manage Job Category",jobIcon); 
        TreeItem<String> jobCats = new TreeItem<String> ("Job Categories");
        manageJobCat.getChildren().addAll(jobCats);
        
        TreeItem<String> timeSetting = new TreeItem<String> ("Time Settings",timeIcon); 
        TreeItem<String> arrivalTime = new TreeItem<String> ("Clock In Time"); 
        TreeItem<String> departureTime = new TreeItem<String> ("Clock Out Time"); 
        timeSetting.getChildren().addAll(arrivalTime,departureTime);
        
        rootItem.getChildren().addAll(changePass,manageUsers,manageJobCat,timeSetting);
        
        menuTreeView = new TreeView<>();
        menuTreeView.setRoot(rootItem);
        menuContainer.getChildren().add(menuTreeView);
        menuContainer.setTopAnchor(menuTreeView,0.0);
        menuContainer.setBottomAnchor(menuTreeView,0.0);
        menuContainer.setRightAnchor(menuTreeView,0.0);
        menuContainer.setLeftAnchor(menuTreeView,0.0);
        
        menuTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayContent(newValue.getValue()));
        
       
    }

    private void displayContent(String value) {
        switch(value){
            case "Change Password": displayChangePasswordContent();
            break;
            case "User's Role": displayUsersRoleContent();
            break;
            case "Users": displayUsersContent();
            break;
            case "Job Categories": displayJobCateContent();
            break;
            case "Clock In Time": displayArrivalTimeContent();
            break;
            case "Clock Out Time":displayDepartureTimeContent();
            break;
            default:
            break;   
        }
    }

    public void displayChangePasswordContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/ChangePassword.fxml"));
                VBox root = loader.load();
                ChangePasswordController chngPassController = loader.getController();
                chngPassController.setStage(preferenceStage);
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void displayUsersRoleContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/UsersRole.fxml"));
                VBox root = loader.load();
                UsersRoleController usersRoleController = loader.getController();
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void displayUsersContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/Users.fxml"));
                VBox root = loader.load();
                UsersController usersController = loader.getController();
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void displayJobCateContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/JobCategory.fxml"));
                VBox root = loader.load();
                JobCategoryController catController = loader.getController();
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void displayArrivalTimeContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/TimeSettingForm.fxml"));
                VBox root = loader.load();
                TimeSettingFormController timeController = loader.getController();
                timeController.arrivalTimeForm();
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void displayDepartureTimeContent() {
        if(prefContainer.getChildren().size() >= 1){
            prefContainer.getChildren().remove(0);
        }
        if(prefContainer.getChildren().size() < 1){
            try {
                FXMLLoader loader = new FXMLLoader(PreferenceScreenController.class.getResource(
                        "/attendancemanagement/view/TimeSettingForm.fxml"));
                VBox root = loader.load();
                TimeSettingFormController timeController = loader.getController();
                timeController.departureTimeForm();
                prefContainer.getChildren().add(root);
                prefContainer.setTopAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setBottomAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setRightAnchor(prefContainer.getChildren().get(0),0.0);
                prefContainer.setLeftAnchor(prefContainer.getChildren().get(0),0.0);
                
            } catch (IOException ex) {
                Logger.getLogger(PreferenceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
