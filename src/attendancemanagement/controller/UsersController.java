package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.User;
import attendancemanagement.service.UserService;
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


public class UsersController implements Initializable {

    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, Integer> uidCol;
    @FXML
    private TableColumn<User, String> usernameCol;
    @FXML
    private TableColumn<User, String> empidCol;
    @FXML
    private TableColumn<User, String> ridCol;
    @FXML
    private TableColumn<User, LocalDateTime> addedCol;
    @FXML
    private TableColumn<User, LocalDateTime> modifiedCol;
    
    private int uid;
    private ObservableList<User> userTableData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userTableData = FXCollections.observableArrayList();
        getUserTableData();
        displayUserTable();
    }    

    @FXML
    private void userRegistrationForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/UserRegistrationForm.fxml"));
            Parent root = loader.load();
            UserRegistrationFormController userformController = loader.getController();
            userformController.userRegistrationForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add User");
            window.setResizable(false);
            Scene scene = new Scene(root);
            userformController.setStage(window);
            window.setOnHidden(e -> {
                getUserTableData();
                setDataInUserTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void userEditForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/UserRegistrationForm.fxml"));
            Parent root = loader.load();
            UserRegistrationFormController userformController = loader.getController();
            userformController.setUid(uid);
            userformController.userEditForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Edit User");
            window.setResizable(false);
            Scene scene = new Scene(root);
            userformController.setStage(window);
            window.setOnHidden(e -> {
                getUserTableData();
                setDataInUserTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        boolean response = UIUtils.confirmNotification("Confirm Delete",
                "This will delete the user account from the list",
                "Are you ok with this?");
        if(response){
            UserService service = new UserService();
            int status = service.deleteByRid(uid);
            showMessage(status);
        }
    }
    
    private void displayUserTable() {
      
        usersTableView.getSelectionModel().selectedItemProperty().addListener((o,oldVal,newVal)->{
            if(newVal != null){
//                System.out.println(newVal.getEmpcode());
                uid = newVal.getUid();
            }
        });
        setDataInUserTable();
    }

    private void setDataInUserTable() {
        uidCol.setCellValueFactory(
                cellData -> cellData.getValue().uidProperty().asObject());
        usernameCol.setCellValueFactory(
                cellData -> cellData.getValue().usernameProperty());
        empidCol.setCellValueFactory(
                cellData -> cellData.getValue().employeeProperty());
        ridCol.setCellValueFactory(
                cellData -> cellData.getValue().roleProperty());
        addedCol.setCellValueFactory(
                cellData -> cellData.getValue().added_dateProperty());
        modifiedCol.setCellValueFactory(
                cellData -> cellData.getValue().last_modified_dateProperty());
        
//        empidCol.setCellFactory(column -> {
//                return new TableCell<User, Integer>() {
//                        @Override
//                        protected void updateItem(Integer item, boolean empty) {
//                                super.updateItem(item, empty);
//                                System.out.println("empid col: "+item);
//                                if (item == 0 || item == null) {
//                                    setText(null);
//                                } else {
//                                    EmployeeService service = new EmployeeService();
//                                    Employee emp = service.getEmployeeData(item);
//                                    if(emp != null){
//                                        if(emp.getFname().isEmpty() && emp.getLname().isEmpty()){
//                                            setText("null");
//                                        }else{
//                                            setText(emp.getFname()+" "+emp.getLname());
//                                        }
//                                    }else{
//                                        setText("null");
//                                    }
//                                }
//                        }
//                };
//        });
//        
//        ridCol.setCellFactory(column -> {
//                return new TableCell<User, Integer>() {
//                        @Override
//                        protected void updateItem(Integer item, boolean empty) {
//                                super.updateItem(item, empty);
//                                System.out.println("rid col: "+item);
//                                if (item == null || item == 0) {
//                                    setText(null);
//                                } else {
//                                    RoleService service = new RoleService();
//                                    Role role = service.selectByRid(item);
//                                    if(role != null){
//                                        if(role.getDisplay_name() == null){
//                                            setText("null");
//                                        }else{
//                                            setText(role.getDisplay_name());
//                                        }
//                                    }else{
//                                        setText("null");
//                                    }
//                                }
//                        }
//                };
//        });
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a, MMM dd, yyyy");
        
        addedCol.setCellFactory(column -> {
                return new TableCell<User, LocalDateTime>() {
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
                return new TableCell<User, LocalDateTime>() {
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
        
        usersTableView.setItems(userTableData);
    }
    
    private void getUserTableData() {
        UserService service = new UserService();
        userTableData = service.getUsersData();
    }
    
    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Delete Incomplete!", "User has not been"
                            + " deleted successfully");
            }
            break;
            case 1:{
                UIUtils.successNotification("Delete Complete!", "User has been"
                       + " deleted successfully");
                getUserTableData();
                setDataInUserTable();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Delete Complete!", "Multiple Users has been"
                        + " deleted \n"
                        + "Username should be unique");
                getUserTableData();
                setDataInUserTable();
            }
            break;
        }
    }

    @FXML
    private void assignRole(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/AssignForm.fxml"));
            Parent root = loader.load();
            AssignFormController assignFormController = loader.getController();
            assignFormController.setUid(uid);
            assignFormController.assignRoleForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Assign Role");
            window.setResizable(false);
            Scene scene = new Scene(root);
            assignFormController.setStage(window);
            window.setOnHidden(e -> {
                getUserTableData();
                setDataInUserTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void assignEmployee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/AssignForm.fxml"));
            Parent root = loader.load();
            AssignFormController assignFormController = loader.getController();
            assignFormController.setUid(uid);
            assignFormController.assignEmployeeForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Assign Employee");
            window.setResizable(false);
            Scene scene = new Scene(root);
            assignFormController.setStage(window);
            window.setOnHidden(e -> {
                getUserTableData();
                setDataInUserTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
}
