package attendancemanagement.controller;


import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Role;
import attendancemanagement.service.RoleService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class UsersRoleController implements Initializable {

    @FXML
    private TableColumn<Role, Integer> ridCol;
    @FXML
    private TableColumn<Role, String> roleCol;
    @FXML
    private TableColumn<Role, String> displayNameCol;
    @FXML
    private TableColumn<Role, LocalDateTime> addedCol;
    @FXML
    private TableColumn<Role, LocalDateTime> modifiedCol;
    @FXML
    private TableView<Role> roleTableView;
    
    private int rid;

    private ObservableList<Role> roleTableData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        roleTableData = FXCollections.observableArrayList();
        getRoleTableData();
        displayRoleTable();
    } 

    private void displayRoleTable() {
      
        roleTableView.getSelectionModel().selectedItemProperty().addListener((o,oldVal,newVal)->{
            if(newVal != null){
//                System.out.println(newVal.getEmpcode());
                rid = newVal.getRid();
            }
        });
        setDataInTable();
    }

    private void setDataInTable() {
        ridCol.setCellValueFactory(
                cellData -> cellData.getValue().ridProperty().asObject());
        roleCol.setCellValueFactory(
                cellData -> cellData.getValue().roleProperty());
        displayNameCol.setCellValueFactory(
                cellData -> cellData.getValue().display_nameProperty());
        addedCol.setCellValueFactory(
                cellData -> cellData.getValue().added_dateProperty());
        modifiedCol.setCellValueFactory(
                cellData -> cellData.getValue().last_modified_dateProperty());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a, MMM dd, yyyy");
        
        addedCol.setCellFactory(column -> {
                return new TableCell<Role, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(formatter));
                                }
                        }
                };
        });
        
        modifiedCol.setCellFactory(column -> {
                return new TableCell<Role, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(formatter));
                                }
                        }
                };
        });
        
        roleTableView.setItems(roleTableData);
    }

    private void getRoleTableData() {
        RoleService service = new RoleService();
        roleTableData = service.getRoleData();
    }

    @FXML
    private void openRoleForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersRoleController.class.getResource(
                    "/attendancemanagement/view/RoleForm.fxml"));
            Parent root = loader.load();
            RoleFormController roleformController = loader.getController();
            roleformController.addNewRole();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add Role");
            window.setResizable(false);
            Scene scene = new Scene(root);
            roleformController.setStage(window);
            window.setOnHidden(e -> {
                getRoleTableData();
                setDataInTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersRoleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openRoleEditForm(ActionEvent event) {
        System.out.println("Edit Role");
        try {
            FXMLLoader loader = new FXMLLoader(UsersRoleController.class.getResource(
                    "/attendancemanagement/view/RoleForm.fxml"));
            Parent root = loader.load();
            RoleFormController roleformController = loader.getController();
            roleformController.setRid(rid);
            roleformController.editRoleFrom();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Edit Role");
            window.setResizable(false);
            Scene scene = new Scene(root);
            roleformController.setStage(window);
            window.setOnHidden(e -> {
                getRoleTableData();
                setDataInTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersRoleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteRole(ActionEvent event) {
        boolean response = UIUtils.confirmNotification("Confirm Delete",
                "This will delete the user's role from the list",
                "Are you ok with this?");
        if(response){
            RoleService service = new RoleService();
            int status = service.deleteByRid(rid);
            showMessage(status);
        }
    
    }
    
    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Delete Incomplete!", "Users's Role has not been"
                            + " deleted successfully");
            }
            break;
            case 1:{
                UIUtils.successNotification("Delete Complete!", "Users's Role has been"
                       + " deleted successfully");
                getRoleTableData();
                setDataInTable();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Delete Complete!", "Multiple Users's Role has been"
                        + " deleted \n"
                        + "Users's Role should be unique");
                getRoleTableData();
                setDataInTable();
            }
            break;
        }
    }
    
}
