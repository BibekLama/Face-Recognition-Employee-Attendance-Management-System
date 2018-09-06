package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Employee;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.ControlledScene;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.JobCategoryService;
import attendancemanagement.service.ScreensController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
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
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class EmployeeScreenController implements Initializable, ControlledScene {
    private ScreensController screenController;
    private EmployeeService service;
    
    @FXML
    private TextField nationalIdField;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private ComboBox<String> genderField;
    @FXML
    private TextField mobileField;
    @FXML
    private ComboBox<JobCategory> jobTitleField;
    @FXML
    private TextField salaryField;
    @FXML
    private RadioButton activeRadioBtn;
    @FXML
    private TableView<Employee> employeeTable = new TableView<Employee>();
    private TableColumn<Employee, String> empidCol;
    private TableColumn<Employee, String> nationalidCol;
    private TableColumn<Employee, String> fnameCol;
    private TableColumn<Employee, String> lnameCol;
    private TableColumn<Employee, LocalDate> dobCol;
    private TableColumn<Employee, String> genderCol;
    private TableColumn<Employee, String> mobileCol;
    private TableColumn<Employee, Integer> jobTitleCol;
    private TableColumn<Employee, Double> salaryCol;
    private TableColumn<Employee, String> statusCol;
    private ObservableList<Employee> employeeTableData;
    private Employee employeeData;
    @FXML
    private TextField searchField;
    private volatile boolean editMode = false;
    private volatile boolean saveMode = true;
    @FXML
    private Button editButton;
    @FXML
    private Label moreInfoLabel;
    @FXML
    private Button saveButton;
    @FXML
    private HBox EmployeeScreen;
    
    @FXML
    private TextField empCodeField;
    @FXML
    private ComboBox<Object> dobField1;
    @FXML
    private ComboBox<Object> dobField2;
    @FXML
    private ComboBox<Object> dobField3;
    @FXML
    private Button activateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button suspendButton;
    
    private int empid;
    
    private ObservableList<JobCategory> jobTitles;
    
    private int jid;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        service = new EmployeeService();
        employeeTableData = FXCollections.observableArrayList();
        employeeTableData = service.getAllEmployees();
        genderField.getItems().addAll(UIUtils.showGender());
        genderField.getSelectionModel().selectFirst();
        setJobCategoryComboBox();
        dobField1.getItems().addAll(UIUtils.showDay());
        dobField2.getItems().addAll(UIUtils.showMonth());
        dobField3.getItems().addAll(UIUtils.showYear("1900"));
        dobField1.getSelectionModel().selectFirst();
        dobField2.getSelectionModel().selectFirst();
        dobField3.getSelectionModel().selectFirst();
        
        employeeTableDisplay(); // DISPLAY EMPLOYEE TABLE
        setEmployeeTableData(); // SET DATA IN EMPLOYEE TABLE
        if(editMode){
            setEditMode();
        }
        if(saveMode){
            setSaveMode();
        }
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeeTableData = service.searchEmployee(newValue);
            setEmployeeTableData();
        });
    } 
    
    private void employeeTableDisplay(){
        empidCol = new TableColumn<>("Employee ID");
        empidCol.setMinWidth(100);
        nationalidCol = new TableColumn<>("National ID");
        nationalidCol.setMinWidth(100);
        fnameCol = new TableColumn<>("First Name");
        fnameCol.setMinWidth(100);
        lnameCol = new TableColumn<>("Last Name");
        lnameCol.setMinWidth(100);
        dobCol = new TableColumn<>("Date Of Birth");
        dobCol.setMinWidth(100);
        genderCol = new TableColumn<>("Gender");
        genderCol.setMinWidth(100);
        mobileCol = new TableColumn<>("Mobile Number");
        mobileCol.setMinWidth(100);
        jobTitleCol = new TableColumn<>("Job Title");
        jobTitleCol.setMinWidth(100);
        salaryCol = new TableColumn<>("Salary");
        salaryCol.setMinWidth(100);
        statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(100);
        employeeTable.getColumns().addAll(empidCol, nationalidCol, fnameCol,
                lnameCol,dobCol,genderCol,mobileCol,jobTitleCol,salaryCol,statusCol);
        
        employeeTable.getSelectionModel().selectedItemProperty().addListener((o,oldVal,newVal)->{
            if(newVal != null){
//                System.out.println(newVal.getEmpcode());
                insertValueInFields(newVal.getEmpcode());
                empid = newVal.getEmpid();
            }
        });
    }
    
    private void setEmployeeTableData(){
        empidCol.setCellValueFactory(
            cellData -> cellData.getValue().empcodeProperty());
        nationalidCol.setCellValueFactory(
        cellData -> cellData.getValue().nationalidProperty());
        fnameCol.setCellValueFactory(
                cellData -> cellData.getValue().fnameProperty());
        lnameCol.setCellValueFactory(
                cellData -> cellData.getValue().lnameProperty());
        dobCol.setCellValueFactory(
                cellData -> cellData.getValue().dobProperty());
        genderCol.setCellValueFactory(
                cellData -> cellData.getValue().genderProperty());
        mobileCol.setCellValueFactory(
                cellData -> cellData.getValue().mobileNumberProperty());
        jobTitleCol.setCellValueFactory(
                cellData -> cellData.getValue().jobTitleProperty().asObject());
        salaryCol.setCellValueFactory(
                cellData -> cellData.getValue().salaryProperty().asObject());
        statusCol.setCellValueFactory(
                cellData -> cellData.getValue().statusProperty());
        
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        dobCol.setCellFactory(column -> {
                return new TableCell<Employee, LocalDate>() {
                        @Override
                        protected void updateItem(LocalDate item, boolean empty) {
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
        
        statusCol.setCellFactory(column -> {
                return new TableCell<Employee, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item);
                                    if(item.equals("Deactive")) {
                                        setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
                                    }
                                    if(item.equals("Active")) {
                                        setStyle("-fx-text-fill: #1D8348; -fx-font-weight: bold;");
                                    }
                                    if(item.equals("Suspend")) {
                                        setStyle("-fx-text-fill: #9B59B6; -fx-font-weight: bold;");
                                    }
                                }
                        }
                };
        });
        
        jobTitleCol.setCellFactory(column -> {
                return new TableCell<Employee, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    JobCategoryService catService = new JobCategoryService();
                                    JobCategory cat = catService.getJobCatById(item);
                                    if(cat != null){
                                        setText(cat.getDisplay_name());
                                    }else{
                                        setText("Not Assigned");
                                    }
                                }
                        }
                };
        });

        employeeTable.setItems(employeeTableData);
    }
    
    // New Employee Registration Form
    @FXML
    private void newEmployeeRegistration(ActionEvent event) {
        setSaveMode();
        clearFields();
        
    }
    
    // Edit Employee Information
    @FXML
    private void editEmpInformation(ActionEvent event) {
        if(getInputFieldData().getEmpcode().isEmpty() ||
                getInputFieldData().getFname().isEmpty() ||
                getInputFieldData().getLname().isEmpty()){
            UIUtils.showError("Incomplete Information!", "Employee ID must not be empty. \n"
                    + "Firstname must not be empty. \n"
                    + "Lastname must not be empty. \n"
                    + "Mobile number ust not be empty.");
        }
        else
        {
            if(UIUtils.isNumeric(getInputFieldData().getMobileNumber())){
                int status = service.UpdateEmployee(getInputFieldData());
                if( status == 1)
                {
                    if(service.assignJob(getInputFieldData().getEmpcode(),getInputFieldData().getJobTitle()) > 0){
                        UIUtils.successNotification("Edit Complete!", "The information of employee has been"
                            + " successfully edited");
                    }else{
                        UIUtils.successNotification("Edit Complete!", "The information of employee has been"
                            + " successfully edited.\n"
                                + "But user account has not been edited successfully.");
                    }
                    
                }else if(status > 1){
                    UIUtils.warningNotification("Edit Complete!", "Duplicate Employee ID exist,\n"
                            + "Employee ID must be unique.");
                }else{
                    UIUtils.errorNotification("Edit Incomplete!", "The information of employee has not been"
                            + " edited successfully.");
                }
            }
            else
            {
                UIUtils.showError("Non-Numeric Value!", "Mobile number must be numeric value.");
            }
        }
        
        reloadEmpTable();
        insertValueInFields(getInputFieldData().getEmpcode());
    }
    
    // Save Employee Information
    @FXML
    private void saveEmpInformation(ActionEvent event) {
        System.out.println(getInputFieldData().getEmpcode());
        System.out.println(getInputFieldData().getFname());
        System.out.println(getInputFieldData().getLname());
        if(getInputFieldData().getEmpcode().isEmpty() ||
                getInputFieldData().getFname().isEmpty() ||
                getInputFieldData().getLname().isEmpty() ){
            UIUtils.showError("Incomplete Information!", "Employee ID must not be empty. \n"
                    + "First name must not be empty. \n"
                    + "Last name must not be empty. \n"
                    + "Mobile number must not be empty.");    
        }
        else
        {
            if(UIUtils.isNumeric(getInputFieldData().getMobileNumber())){
                int status = service.addEmployee(getInputFieldData());
                if(status == 1){
                    if(service.assignJob(getInputFieldData().getEmpcode(),getInputFieldData().getJobTitle()) > 0){
                        UIUtils.successNotification("Add Complete!", "The information of employee has been"
                            + " successfully added");
                    }else{
                        UIUtils.successNotification("Add Complete!", "The information of employee has been"
                            + " successfully added.\n"
                                + "But user account has not been assigned successfully.");
                    }
                    employeeTableData = service.getAllEmployees();
                    setEmployeeTableData();
                    editMode = true;
                    saveMode = false;
                    saveButton.setDisable(true);
                    editButton.setDisable(false);
                    moreInfoLabel.setDisable(false);
                    activateButton.setDisable(false);
                }else if(status > 1){
                   UIUtils.warningNotification("Add Complete!", "Multiple employee added"
                            + "with same Employee ID. \n"
                           + "Employee ID must be unique.");
                    employeeTableData = service.getAllEmployees();
                    setEmployeeTableData();
                    editMode = true;
                    saveMode = false;
                    saveButton.setDisable(true);
                    editButton.setDisable(false);
                    moreInfoLabel.setDisable(false);
                    activateButton.setDisable(false);
                }else{
                   UIUtils.errorNotification("Add Incomplete!", "The information of employee has not been"
                            + " added successfully.");
                }
            }
            else
            {
                UIUtils.showError("Non-Numeric Value!", "Mobile number must be numeric value.");
            }   
        }
    }
    
    // Activate Employee Status
    @FXML
    private void activateEmployee(ActionEvent event) {
        int status = service.activateEmployee(employeeData);
        String task = "";
//        System.out.println(model.getStatus());
        if(employeeData.getStatus().equals("Active")){
            task = "Deactivate";
        }
        if(employeeData.getStatus().equals("Deactive")){
            task = "Activate";
        }
        
        if(employeeData.getStatus().equals("Suspend")){
            task = "Activate";
        }
        
        if(status == 1)
        {
            UIUtils.successNotification(task+" Complete!", "The employee has been"
                    + " successfully "+task+"d");
        }else if(status > 1){
            UIUtils.warningNotification(task+" Complete!", "Duplicate Employee ID exist,\n"
                    + "Employee ID must be unique.");
        }else{
            UIUtils.errorNotification(task+" Incomplete!", "The employee has not been"
                    + " edit"+task+"ed successfully.");
        }
        
        reloadEmpTable();
        insertValueInFields(employeeData.getEmpcode());
        
    }
    // Clear Input Field
    @FXML
    private void resetInpuField(ActionEvent event) {
        clearFields();
    }
    
    // Delete Employee
    @FXML
    private void deleteEmpInformation(ActionEvent event) {
        boolean response = UIUtils.confirmNotification("Confirm Delete",
                "This will delete the employee from the list",
                "Are you ok with this?");
        if(response){
            int status = service.deleteEmployee(employeeData);
            if(status == 1){
                UIUtils.successNotification("Delete Complete!", "The employee has been deleted successfully.");
            } else if(status > 1){
                UIUtils.warningNotification("Delete Complete!",  "Multiple employee has been deleted.,\n"
                    + "Employee ID must be unique.");
            }else{
                UIUtils.errorNotification("Delete Incomplete!", "The employee has not been deleted successfully.");
            }

            employeeTableData = service.getAllEmployees();
            setEmployeeTableData();
            setSaveMode();
            clearFields();
        }
        
    }
    
    // Suspend Employee Status
    @FXML
    private void suspendEmployee(ActionEvent event) {
        
        if(employeeData.getStatus().equals("Suspend")){
            UIUtils.warningNotification("Suspend Incomplete!", "The employee is already suspended");
        }
        else
        {
            int status = service.suspendEmployee(employeeData);
            if(status == 1){
                UIUtils.successNotification("Suspend Complete!", "The employee is suspended successfully.");
            } else if(status > 1){
                UIUtils.warningNotification("Suspend Complete!",  "Duplicate Employee ID exist,\n"
                    + "Employee ID must be unique.");
            }else{
                UIUtils.errorNotification("Suspend Incomplete!", "The employee is already suspended");
            }
        }
        reloadEmpTable();
        insertValueInFields(employeeData.getEmpcode());
    }
    
    // Refresh Employee table
    @FXML
    private void reloadEmpTable(ActionEvent event) {
        reloadEmpTable();
    }
    
    public void reloadEmpTable(){
        employeeTableData = service.getAllEmployees();
        setEmployeeTableData();
    }
    
    // Print Employee Table
    @FXML
    private void printEmpTable(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        Stage stage = (Stage) EmployeeScreen.getScene().getWindow();
        if (printerJob.showPrintDialog(stage) && printerJob.printPage(employeeTable))
        {
            employeeTable.setScaleX(0.60);
            employeeTable.setScaleY(0.60);
            employeeTable.setTranslateX(-220);
            employeeTable.setTranslateY(-70);
            boolean success = printerJob.printPage(employeeTable);
            if (success) {
                 printerJob.endJob(); 
            } 
            employeeTable.setTranslateX(0);
            employeeTable.setTranslateY(0);               
            employeeTable.setScaleX(1.0);
            employeeTable.setScaleY(1.0);  
            System.out.println("printed");
        }
    }
    
    // Export Employee Table To Excel
    @FXML
    private void exportEmpTable(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = 
            new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
            File file = fileChooser.showSaveDialog(EmployeeScreen.getScene().getWindow());
             
        if(file != null){
            exportTableToExcel(file);
        }
        
    }
    
    // Show employee detail informations
    @FXML
    private void showEmployeeDetails(ActionEvent event) {
        if(empid > 0){
            try {
                FXMLLoader loader = new FXMLLoader(EmployeeScreenController.class.getResource(
                        "/attendancemanagement/view/EmployeeDetailScreen.fxml"));
                Parent root = loader.load();
                EmployeeDetailScreenController empDetailController = loader.getController();
                empDetailController.setEmpId(empid);
                empDetailController.showEmpDetail();
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Employee Detail View");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private void insertValueInFields(String empcode) {
        setEditMode();
        for(Employee data: employeeTableData){
            if(data.getEmpcode().equals(empcode)){
                empCodeField.setText(data.getEmpcode());
                nationalIdField.setText(data.getNationalid());
                fnameField.setText(data.getFname());
                lnameField.setText(data.getLname());
                dobField1.setValue(data.getDob().getDayOfMonth());
                dobField1.getSelectionModel().select((Object) data.getDob().getDayOfMonth());
                dobField2.setValue(data.getDob().getMonth());
                dobField2.getSelectionModel().select(data.getDob().getMonth());
                dobField3.setValue(data.getDob().getYear());
                dobField3.getSelectionModel().select(data.getDob().getYear());
                genderField.getSelectionModel().select(data.getGender());
                
                JobCategoryService catService = new JobCategoryService();
                JobCategory cat = new JobCategory();
                cat = catService.getJobCatById(data.getJobTitle());
                jobTitleField.getSelectionModel().select(cat);
                
                mobileField.setText(data.getMobileNumber()+"");
                salaryField.setText(data.getSalary()+"");
                if(data.getStatus().equals("Active"))
                {
                    activeRadioBtn.setSelected(true);
                    activateButton.setText("Deactivate");
                }else{
                    activeRadioBtn.setSelected(false);
                    activateButton.setText("Activate");
                }
                employeeData = data;
            }
        }
    }
    
    private void setJobCategoryComboBox(){
        jobTitles = FXCollections.observableArrayList();
        JobCategoryService service = new JobCategoryService();
        for(JobCategory job: service.getJobCategories()){
            jobTitles.add(job);
        }
        jobTitleField.setItems(jobTitles);
        jobTitleField.setConverter(new StringConverter<JobCategory>(){
            @Override
            public String toString(JobCategory object) {
                return object.getDisplay_name();
            }

            @Override
            public JobCategory fromString(String string) {
                return jobTitleField.getItems().stream().filter(job ->
                job.getDisplay_name().equals(string)).findFirst().orElse(null);
            }
        });
        
        jobTitleField.valueProperty().addListener((obs,oldval, newval) -> {
            if(newval != null){
                jid = newval.getJid();
                System.out.println("jid= "+jid);
            }
        });
       
    }

    private void clearFields() {
        if(saveMode)
        {
            empCodeField.setText(null);
            activeRadioBtn.setSelected(true);
        }
        
        nationalIdField.setText(null);
        fnameField.setText(null);
        lnameField.setText(null);
        dobField1.getSelectionModel().selectFirst();
        dobField2.getSelectionModel().selectFirst();
        dobField3.getSelectionModel().selectFirst();
        genderField.getSelectionModel().selectFirst();
        mobileField.setText(null);
        salaryField.setText("0.0");
        jobTitleField.getSelectionModel().selectFirst();
        
    }

    @Override
    public void setScreenParent(ScreensController parent) {
        screenController = parent;
    }
    
    private Employee getInputFieldData(){
        Employee inputData = new Employee();
        inputData.setEmpcode(empCodeField.getText());
        inputData.setNationalid(nationalIdField.getText());
        inputData.setFname(fnameField.getText());
        inputData.setLname(lnameField.getText());
        
        int day = (Integer) dobField1.getSelectionModel().getSelectedItem();
        Month month = (Month) dobField2.getSelectionModel().getSelectedItem();
        int year = (Integer) dobField3.getSelectionModel().getSelectedItem();
        inputData.setDob(LocalDate.of(year, month, day));
        inputData.setGender(genderField.getSelectionModel().getSelectedItem());
        inputData.setMobileNumber(mobileField.getText());
        inputData.setJobTitle(jobTitleField.getSelectionModel().getSelectedItem().getJid());
        Double salary = 0.0;
        if(!salaryField.getText().isEmpty()) {
            salary = Double.parseDouble(salaryField.getText());
        }
        
        inputData.setSalary(salary);
        String status = "Deactive";
        if(activeRadioBtn.isSelected()){
            status = "Active";
        }
        inputData.setStatus(status);
        
        return inputData;
    } 
    
    private void setSaveMode(){
        editMode = false;
        saveMode = true;
        saveButton.setDisable(false);
        editButton.setDisable(true);
        moreInfoLabel.setDisable(true);
        activateButton.setDisable(true);
        deleteButton.setDisable(true);
        suspendButton.setDisable(true);
        empCodeField.setDisable(false);
    }
    
    private void setEditMode(){
        saveMode = false;
        editMode = true;
        saveButton.setDisable(true);
        editButton.setDisable(false);
        moreInfoLabel.setDisable(false);
        activateButton.setDisable(false);
        empCodeField.setDisable(true);
        deleteButton.setDisable(false);
        suspendButton.setDisable(false);
    }



    // EXPORT TO EXCEL FILE
    private void exportTableToExcel(File file) {
        
        FileOutputStream fos = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet empSheet = wb.createSheet("Employee Details");
            XSSFRow header = empSheet.createRow(0);
            header.createCell(0).setCellValue("Employee ID");
            header.createCell(1).setCellValue("National ID");
            header.createCell(2).setCellValue("First Name");
            header.createCell(3).setCellValue("Last Name");
            header.createCell(4).setCellValue("Date Of Birth");
            header.createCell(5).setCellValue("Gender");
            header.createCell(6).setCellValue("Mobile Number");
            header.createCell(7).setCellValue("Job Title");
            header.createCell(8).setCellValue("Salary (per hr)");
            header.createCell(9).setCellValue("Status");
            int index = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            for(Employee data: employeeTableData){
                XSSFRow row = empSheet.createRow(index);
                row.createCell(0).setCellValue(data.getEmpcode());
                row.createCell(1).setCellValue(data.getNationalid());
                row.createCell(2).setCellValue(data.getFname());
                row.createCell(3).setCellValue(data.getLname());
                row.createCell(4).setCellValue(data.getDob().format(formatter));
                row.createCell(5).setCellValue(data.getGender());
                row.createCell(6).setCellValue(data.getMobileNumber());
                row.createCell(7).setCellValue(data.getJobTitle());
                row.createCell(8).setCellValue(data.getSalary());
                row.createCell(9).setCellValue(data.getStatus());
                index++;
            }   
            fos = new FileOutputStream(file);
            wb.write(fos);
//            System.out.println("Show Exported Message Notification.");
            UIUtils.successNotification("Export Complete!", "Employee Table has been exported successfully.\n"
                    + "File Location: "+file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            UIUtils.errorNotification("Export Incomplete!", "Employee Table has not been exported successfully.\n"
                    + "File Not Found.");
        } catch (IOException ex) {
            Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            UIUtils.errorNotification("Export Incomplete!", "Employee Table has not been exported successfully.");
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    @FXML
    private void openRegForm(MouseEvent event) {
        if(empid > 0){
            try {
                FXMLLoader loader = new FXMLLoader(EmployeeScreenController.class.getResource(
                        "/attendancemanagement/view/EmployeeRegistrationForm.fxml"));
                Parent root = loader.load();
                EmployeeRegistrationFormController formController = loader.getController();
                formController.setEmpid(empid);
                formController.showEmpForm();
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Registration Form");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.showAndWait();
                reloadEmpTable();
                insertValueInFields(employeeData.getEmpcode());
            } catch (IOException ex) {
                Logger.getLogger(EmployeeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   
}
