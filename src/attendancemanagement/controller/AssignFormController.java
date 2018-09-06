package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Employee;
import attendancemanagement.model.Role;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.RoleService;
import attendancemanagement.service.UserService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class AssignFormController implements Initializable {

    @FXML
    private Label assignLabel;
    @FXML
    private Button submitButton;
    @FXML
    private HBox assignBox;

    private boolean assignEmp;
    private boolean assignRole;

    private int uid;
    private Stage stage;
    private ComboBox<Role> roleField;
    private ComboBox<Employee> empField;
    
    private ObservableList<Employee> employees;
    private ObservableList<Role> roles;
    private int rid;
    private int empid;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void assign(ActionEvent event) {
        if(assignRole){
            addRolltoUser();
        }
        if(assignEmp){
            addEmployeetoUser();
        }
    }

    void setUid(int uid) {
        this.uid = uid;
    }

    void assignRoleForm() {
        assignEmp = false;
        assignRole = true;
        assignLabel.setText("ROLE");
        submitButton.setText("ASSIGN ROLE");
        roleField = new ComboBox<>();
        setUsersRoleComboBox();
        
        UserService service = new UserService();
        int rid = service.getRidFromAssignedRole(uid);
        RoleService rService = new RoleService();
        Role role = rService.selectByRid(rid);
        roleField.getSelectionModel().select(role);
        
        if(assignBox.getChildren().size() > 0){
            assignBox.getChildren().clear();
        }
        assignBox.getChildren().add(roleField);
    }

    void setStage(Stage window) {
        stage = window;
    }
    
    private void setEmployeeComboBox(){
        employees = FXCollections.observableArrayList();
        EmployeeService service = new EmployeeService();
        for(Employee emp: service.getAllEmployees()){
            employees.add(emp);
        }
        empField.setItems(employees);
        empField.setConverter(new StringConverter<Employee>(){
            @Override
            public String toString(Employee object) {
                return object.getEmpcode();
            }

            @Override
            public Employee fromString(String string) {
                return empField.getItems().stream().filter(emp ->
                emp.getEmpcode().equals(string)).findFirst().orElse(null);
            }
        });
        
        empField.valueProperty().addListener((obs,oldval, newval) -> {
            if(newval != null){
                empid = newval.getEmpid();
                System.out.println("emp_id= "+empid);
            }
        });
        UserService uService = new UserService();
    }
    
    private void setUsersRoleComboBox(){
        roles = FXCollections.observableArrayList();
        RoleService service = new RoleService();
        for(Role role: service.getRoleData()){
            roles.add(role);
        }
        roleField.setItems(roles);
        roleField.setConverter(new StringConverter<Role>(){
            @Override
            public String toString(Role object) {
                return object.getDisplay_name();
            }

            @Override
            public Role fromString(String string) {
                return roleField.getItems().stream().filter(rol ->
                rol.getDisplay_name().equals(string)).findFirst().orElse(null);
            }
            
        });
        
        roleField.valueProperty().addListener((obs,oldval, newval) -> {
            if(newval != null){
                rid = newval.getRid();
                System.out.println("rid= "+rid);
            }
        });
        UserService uService = new UserService();
    }

    private void addRolltoUser() {
        int status = 0;
        if(uid != 0){
            UserService service = new UserService();
            status = service.assignRole(uid,rid);
        }
        showMessage(status);
    }

    private void showMessage(int status) {
        switch(status){
            case 0:{
                stage.close();
                UIUtils.warningNotification("Assign Incomplete!", "Please select user from the table"); 
            }
            break;
            case 1:{
                stage.close();
                UIUtils.successNotification("Assign Complete!", "Role has been"
                       + " assigned to the User account successfully");
            }
            break;
            case 2:{
                stage.close();
                UIUtils.warningNotification("Assign Complete!", "Role has been assigned to Multiple Users account"
                        + " \n"
                        + "Users account should be unique");
            }
            break;
            case 3:{
                UIUtils.errorNotification("Assign Incomplete!", "Role has not been"
                            + " assigned to User account successfully");
            }
            break;
            case 4:{
                stage.close();
                UIUtils.successNotification("Assign Complete!", "User account has been"
                       + " aassigned to the employee successfully");
            }
            break;
            case 5:{
                stage.close();
                UIUtils.warningNotification("Assign Complete!", "User account has been assigned to Multiple employee"
                        + " \n"
                        + "Employee should be unique");
            }
            break;
            case 6:{
                UIUtils.errorNotification("Assign Incomplete!", "User account has not been"
                            + " assigned to employee successfully");
            }
            break;
            
        }
    }

    void assignEmployeeForm() {
        assignEmp = true;
        assignRole = false;
        assignLabel.setText("EMPLOYEE");
        submitButton.setText("ASSIGN EMPLOYEE");
        empField = new ComboBox<>();
        setEmployeeComboBox();
        
        UserService service = new UserService();
        int empid = service.getEmpIdFromAssignedUser(uid);
        EmployeeService eService = new EmployeeService();
        Employee emp = eService.getEmployeeData(empid);
        empField.getSelectionModel().select(emp);
        
        if(assignBox.getChildren().size() > 0){
            assignBox.getChildren().clear();
        }
        assignBox.getChildren().add(empField);
    }

    private void addEmployeetoUser() {
        int status = 0;
        if(uid != 0){
            UserService service = new UserService();
            status = service.assignEmployee(uid,empid);
        }
        showMessage(status);
    }
    
}
