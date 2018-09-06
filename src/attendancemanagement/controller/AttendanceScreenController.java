package attendancemanagement.controller;

import attendancemanagement.model.Attendance;
import attendancemanagement.model.Employee;
import attendancemanagement.service.AttendanceService;
import attendancemanagement.service.ControlledScene;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.ScreensController;
import attendancemanagement.utils.UIUtils;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AttendanceScreenController implements Initializable, ControlledScene {

    private ScreensController screenController;
    private AttendanceService service;
    
    private ObservableList<Attendance> attendanceTableData;
    @FXML
    private HBox AttendanceScreen;
    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TableColumn<Attendance, String> statusCol;
    @FXML
    private TableColumn<Attendance, Integer> empNameCol;
    @FXML
    private TableColumn<Attendance, Integer> snCol;
    @FXML
    private TableColumn<Attendance, Integer> empCodeCol;
    @FXML
    private TableColumn<Attendance, LocalDateTime> yearCol;
    @FXML
    private TableColumn<Attendance, LocalDateTime> monthCol;
    @FXML
    private TableColumn<Attendance, LocalDateTime> timeCol;
    @FXML
    private JFXListView<HBox> employeeList;
    
    private int empid;
    
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("dd MMM");
    @FXML
    private TextField searchField;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        loadAttendanceTable();
        showEmployeeList();
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            attendanceTableData = service.searchAttendance(newValue);
            setAttendanceTableData();
        });
        
    }    

    @Override
    public void setScreenParent(ScreensController parent) {
        screenController = parent;
    }

 
    
    private void setAttendanceTableData(){
        snCol.setCellValueFactory(
            cellData -> cellData.getValue().snProperty().asObject());
        
        empCodeCol.setCellValueFactory(
            cellData -> cellData.getValue().empidProperty().asObject());
        
        empNameCol.setCellValueFactory(
            cellData -> cellData.getValue().empidProperty().asObject());
        
        statusCol.setCellValueFactory(
                cellData -> cellData.getValue().statusProperty());
        
        yearCol.setCellValueFactory(
                cellData -> cellData.getValue().timeProperty());
        
        monthCol.setCellValueFactory(
                cellData -> cellData.getValue().timeProperty());
        
        timeCol.setCellValueFactory(
                cellData -> cellData.getValue().timeProperty());
        
        empCodeCol.setCellFactory(column -> {
                return new TableCell<Attendance, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    EmployeeService service = new EmployeeService();
                                    Employee emp = service.getEmployeeData(item);
                                    setText(emp.getEmpcode());
                                }
                        }
                };
        });
        
        empNameCol.setCellFactory(column -> {
                return new TableCell<Attendance, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    EmployeeService service = new EmployeeService();
                                    Employee emp = service.getEmployeeData(item);
                                    setText(emp.getFname()+" "+emp.getLname());
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
        
        
        
        yearCol.setCellFactory(column -> {
                return new TableCell<Attendance, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(yearFormatter));
                                }
                        }
                };
        });
        
        
        
        monthCol.setCellFactory(column -> {
                return new TableCell<Attendance, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(monthFormatter));
                                }
                        }
                };
        });
        
        attendanceTable.setItems(attendanceTableData);
    }
    
    private void loadAttendanceTable(){
        service = new AttendanceService();
        attendanceTableData = service.getAllAttendances();
        setAttendanceTableData();
    }
    
    private void loadAttendanceTable(int empid){
        service = new AttendanceService();
        attendanceTableData = service.getEmployeeAttendance(empid);
        setAttendanceTableData();
    }

    private void showEmployeeList() {
        employeeList.getItems().clear();
        EmployeeService service = new EmployeeService();
        for(Employee emp : service.getAllEmployees()){
            Label lbl = new Label(emp.getFname()+" "+emp.getLname());
            Image avatar = null;
            if(emp.getAvatar() != null){
                File file = new File(URI.create(emp.getAvatar()).getPath());
                if(file.isFile() && file.exists()){
                    avatar = new Image(emp.getAvatar());
                }
                else{
                    File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
                    try {
                        avatar = new Image(f.getCanonicalFile().toURI().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
                try {
                    avatar = new Image(f.getCanonicalFile().toURI().toString());
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            Circle clip = new Circle(50, 25, 25);
            clip.setFill(new ImagePattern(avatar));
            lbl.setGraphic(clip);
            lbl.setGraphicTextGap(10);
            lbl.setContentDisplay(ContentDisplay.LEFT);
            lbl.setFont(Font.font(14));
            HBox hbox = new HBox(lbl);
            hbox.setAccessibleText(emp.getEmpid()+"");
            hbox.setAlignment(Pos.CENTER_LEFT);
            employeeList.getItems().add(hbox); 
        }
        
        employeeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>(){
            @Override
            public void changed(ObservableValue<? extends HBox> observable, HBox oldValue, HBox newValue) {
                int id = Integer.parseInt(newValue.getAccessibleText());
//                System.out.println(id);
                loadAttendanceTable(id);
            }
        });
    }

    @FXML
    private void showAllAttendances(ActionEvent event) {
        loadAttendanceTable();
    }

    @FXML
    private void exportToExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = 
            new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
            File file = fileChooser.showSaveDialog(AttendanceScreen.getScene().getWindow());
             
        if(file != null){
            exportTableToExcel(file);
        }
    }
    
    // EXPORT TO EXCEL FILE
    private void exportTableToExcel(File file) {
        
        FileOutputStream fos = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet empSheet = wb.createSheet("Attendances");
            XSSFRow header = empSheet.createRow(0);
            header.createCell(0).setCellValue("SN");
            header.createCell(1).setCellValue("EMPLOYEE CODE");
            header.createCell(2).setCellValue("FULL NAME");
            header.createCell(3).setCellValue("YEAR");
            header.createCell(4).setCellValue("DAY OF MONTH");
            header.createCell(5).setCellValue("TIME");
            header.createCell(6).setCellValue("STATUS");
            int index = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            for(Attendance data: attendanceTableData){
                XSSFRow row = empSheet.createRow(index);
                row.createCell(0).setCellValue(data.getSn());
                
                EmployeeService service = new EmployeeService();
                Employee emp = service.getEmployeeData(data.getEmpid());
                
                row.createCell(1).setCellValue(emp.getEmpcode());
                row.createCell(2).setCellValue(emp.getFname()+" "+emp.getLname());
                row.createCell(3).setCellValue(data.getTime().format(yearFormatter));
                row.createCell(4).setCellValue(data.getTime().format(monthFormatter));
                row.createCell(5).setCellValue(data.getTime().format(timeFormatter));
                row.createCell(6).setCellValue(data.getStatus());
                index++;
            }   
            fos = new FileOutputStream(file);
            wb.write(fos);
//            System.out.println("Show Exported Message Notification.");
            UIUtils.successNotification("Export Complete!", "Attendance Table has been exported successfully.\n"
                    + "File Location: "+file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            UIUtils.errorNotification("Export Incomplete!", "Attendane Table has not been exported successfully.\n"
                    + "File Not Found.");
        } catch (IOException ex) {
            Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            UIUtils.errorNotification("Export Incomplete!", "Attendance Table has not been exported successfully.");
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(AttendanceScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }


}
