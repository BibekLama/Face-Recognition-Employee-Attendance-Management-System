package attendancemanagement.controller;

import attendancemanagement.model.Attendance;
import attendancemanagement.model.Employee;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.AttendanceService;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.JobCategoryService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class EmployeeDetailScreenController implements Initializable {

    private EmployeeService empService;
    
    @FXML
    private AnchorPane EmployeeDetailScreen;
    @FXML
    private Label fullnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label mobileNoLabel;
    @FXML
    private Label employeeCodeLabel;
    @FXML
    private Label nationalIDLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label phoneNoLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label districtLabel;
    @FXML
    private Label zoneLabel;
    @FXML
    private Text addressLabel;
    @FXML
    private Label jobTitleLabel;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label activateDateLabel;
    private Label updateDateLabel;
    
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
    private DateTimeFormatter dobFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    @FXML
    private TableColumn<Attendance, LocalDateTime> dateCol;
    @FXML
    private TableColumn<Attendance, LocalDateTime> timeCol;
    @FXML
    private TableColumn<Attendance, String> statusCol;
    @FXML
    private TableView<Attendance> attendanceTable;
    
    private ObservableList<Attendance> attendances;
  
    private int empid;
    @FXML
    private HBox avatarBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        empService = new EmployeeService();
        loadTableData();
        
    }    

    void setEmpId(int empid) {
        this.empid = empid;
        
//        System.out.println(empcode);
    }

    public void showEmpDetail() {
        Employee employee = empService.getEmployeeData(empid);
        fullnameLabel.setText(employee.getFname()+" "+employee.getLname());
        emailLabel.setText(employee.getEmail());
        mobileNoLabel.setText(employee.getMobileNumber());
        employeeCodeLabel.setText(employee.getEmpcode());
        nationalIDLabel.setText(employee.getNationalid());
        dobLabel.setText(employee.getDob().format(dobFormatter));
        genderLabel.setText(employee.getGender());
        phoneNoLabel.setText(employee.getPhone());
        cityLabel.setText(employee.getCity());
        districtLabel.setText(employee.getDistrict());
        zoneLabel.setText(employee.getZone());
        addressLabel.setText(employee.getAddress());
        JobCategoryService catService = new JobCategoryService();
        String jobTitle = "n/a";
        JobCategory cat = catService.getJobCategory(employee.getEmpid());
        if(cat != null){
            jobTitle = cat.getTitle();
        }
        jobTitleLabel.setText(jobTitle);
        salaryLabel.setText(Double.toString(employee.getSalary()));
        statusLabel.setText(employee.getStatus());
        activateDateLabel.setText(employee.getAddedDate().format(dateFormatter));
//            updateDateLabel.setText(employee.getUpdateDate().format(formatter));

        File file = new File(URI.create(employee.getAvatar()).getPath());
        Image avatar = null;
        if(file.isFile() && file.exists()){
            avatar = new Image(employee.getAvatar());

        }
        else
        {
            File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
            try {
                avatar = new Image(f.getCanonicalFile().toURI().toString());
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ImageView imageView = new ImageView(avatar);
        imageView.setFitHeight(98);
        imageView.setFitWidth(98);
        avatarBox.setPadding(new Insets(1,1,1,1));
        avatarBox.getChildren().add(imageView);
        
        loadTableData();
        
    }

    private void loadTableData() {
        AttendanceService service = new AttendanceService();
        attendances = service.getEmployeeAttendance(empid);
        setTableData();
    }
    
    private void setTableData(){
        dateCol.setCellValueFactory(
            cellData -> cellData.getValue().timeProperty());
        timeCol.setCellValueFactory(
            cellData -> cellData.getValue().timeProperty());
        statusCol.setCellValueFactory(
            cellData -> cellData.getValue().statusProperty());
        
        dateCol.setCellFactory(column -> {
                return new TableCell<Attendance, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(dateFormatter));
                                }
                        }
                };
        });
        
        
        timeCol.setCellFactory(column -> {
                return new TableCell<Attendance, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(timeFormatter));
                                }
                        }
                };
        });
        
        attendanceTable.setItems(attendances);
        
    }
    
}
