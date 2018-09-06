package attendancemanagement.controller;

import attendancemanagement.model.Employee;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.JobCategoryService;
import attendancemanagement.utils.UIUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;


public class EmployeeRegistrationFormController implements Initializable {

    @FXML
    private Label empCodeLabel;
    @FXML
    private Button fleButton;
    @FXML
    private TextField nationalIdField;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private DatePicker dobField;
    @FXML
    private ComboBox<String> genderField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField districtField;
    @FXML
    private ComboBox<String> zoneField;
    @FXML
    private TextArea addressField;
    @FXML
    private ComboBox<JobCategory> jobField;
    @FXML
    private TextField salaryField;
    
    private FileChooser fileChooser;
    
    
    private String avatar_url;

    private int empid;
    
    private int jid;
    
    private ObservableList<JobCategory> jobTitles;
    @FXML
    private AnchorPane empRegFormScreen;
    @FXML
    private Label avatarLabel;
    @FXML
    private TextField emailField;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        genderField.getItems().addAll(UIUtils.showGender());
        zoneField.getItems().addAll(UIUtils.showZone());
        setJobCategoryComboBox();
    }  
    
    public void setEmpid(int empid){
        this.empid = empid;
    }

    @FXML
    private void chooseImageFile(ActionEvent event) {
        fileChooser = new FileChooser();
        //Show save file dialog
        File file = fileChooser.showOpenDialog(empRegFormScreen.getScene().getWindow());
        if (file != null) {
            setEmpAvatar(file);
        }
    }

    @FXML
    private void submitForm(ActionEvent event) {
        EmployeeService service = new EmployeeService();
        int status = 0;
        if(inputData() != null){
            status = service.updateEmpDetail(inputData());
        }
        showNotifications(status);
    }

    @FXML
    private void resetFields(ActionEvent event) {
    }
    
    public void showEmpForm(){
        setEmployeeFields();
    }
    
    private void setEmployeeFields(){
        if(empid > 0){
            EmployeeService service = new EmployeeService();
            Employee emp = service.getEmployeeData(empid);
            empCodeLabel.setText(emp.getEmpcode());
            setEmpAvatar(emp.getAvatar());
            nationalIdField.setText(emp.getNationalid());
            fnameField.setText(emp.getFname());
            lnameField.setText(emp.getLname());
            dobField.setValue(emp.getDob());
            
            if(!emp.getGender().equals("")){
                genderField.getSelectionModel().select(emp.getGender());
            }else{
                genderField.getSelectionModel().selectFirst();
            }
            mobileField.setText(emp.getMobileNumber());
            phoneField.setText(emp.getPhone());
            emailField.setText(emp.getEmail());
            cityField.setText(emp.getCity());
            districtField.setText(emp.getDistrict());
            if(emp.getZone()!= null){
                zoneField.getSelectionModel().select(emp.getZone());
            }else{
                zoneField.getSelectionModel().selectFirst();
            }
            
            addressField.setText(emp.getAddress());
            JobCategoryService jService = new JobCategoryService();
            JobCategory job = jService.getJobCatById(emp.getJobTitle());
            if(job.getJid() > 0){
                jobField.getSelectionModel().select(job);
            }else{
                jobField.getSelectionModel().selectFirst();
            }
            salaryField.setText(emp.getSalary()+"");
        }
    }
    
    private void setEmpAvatar(String url) {
        File file = new File(URI.create(url).getPath());
        Image avatar = null;
        if(file.isFile() && file.exists() && !url.equals("")){
            avatar = new Image(url);
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
        Circle clip = new Circle(80, 40, 40);
        clip.setFill(new ImagePattern(avatar));
        avatarLabel.setGraphic(clip);
        avatar_url = url;
    }
    
    private void setEmpAvatar(File file) {
        Image avatar = new Image(file.toURI().toString());
        Circle clip = new Circle(80, 40, 40);
        clip.setFill(new ImagePattern(avatar));
        avatarLabel.setGraphic(clip);
        avatar_url = file.toURI().toString();
    }
    
    private void setJobCategoryComboBox(){
        jobTitles = FXCollections.observableArrayList();
        JobCategoryService service = new JobCategoryService();
        for(JobCategory job: service.getJobCategories()){
            jobTitles.add(job);
        }
        jobField.setItems(jobTitles);
        jobField.setConverter(new StringConverter<JobCategory>(){
            @Override
            public String toString(JobCategory object) {
                return object.getDisplay_name();
            }

            @Override
            public JobCategory fromString(String string) {
                return jobField.getItems().stream().filter(job ->
                job.getDisplay_name().equals(string)).findFirst().orElse(null);
            }
        });
        
        jobField.valueProperty().addListener((obs,oldval, newval) -> {
            if(newval != null){
                jid = newval.getJid();
            }
        });
       
    }
    
    private Employee inputData(){
        Employee emp = new Employee();
        emp.setEmpid(empid);
        emp.setNationalid(nationalIdField.getText());
        emp.setFname(fnameField.getText());
        emp.setLname(lnameField.getText());
        emp.setDob(dobField.getValue());
        emp.setGender(genderField.getSelectionModel().getSelectedItem());
        if(mobileField.getText().isEmpty()){
            UIUtils.showError("Empty Field!", "Mobile number must not be empty.");
            return null;
        }
        if(UIUtils.isNumeric(mobileField.getText())){
            emp.setMobileNumber(mobileField.getText());
        }else{
            UIUtils.showError("Non-Numeric Value!", "Mobile number must be numeric value.");
            return null;
        }
        
        if(!phoneField.getText().isEmpty()){
            if(UIUtils.isNumeric(phoneField.getText())){
                emp.setPhone(phoneField.getText());
            }else{
                UIUtils.showError("Non-Numeric Value!", "Phone number must be numeric value.");
                return null;
            }
        }
        
        emp.setEmail(emailField.getText());
        emp.setCity(cityField.getText());
        emp.setDistrict(districtField.getText());
        emp.setZone(zoneField.getSelectionModel().getSelectedItem());
        emp.setAddress(addressField.getText());
        
        JobCategoryService jService = new JobCategoryService();
        JobCategory job = jService.getJobCatById(emp.getJobTitle());
        emp.setJobTitle(jobField.getSelectionModel().getSelectedItem().getJid());
        
        Double salary = 0.0;
        if(!salaryField.getText().isEmpty()) {
            salary = Double.parseDouble(salaryField.getText());
        }
        emp.setSalary(salary);
        if(avatar_url.equals("")){
            File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
            try {
                avatar_url = f.getCanonicalFile().toURI().toString();
            } catch (IOException ex) {
                Logger.getLogger(EmployeeRegistrationFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        emp.setAvatar(avatar_url);
        return emp;
    }

    private void showNotifications(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Update Incomplete!", "The information of employee has not been"
                            + " updated successfully");
            }
            break;
            case 1:{
                 UIUtils.successNotification("Update Complete!", "The information of employee has been"
                            + " updated successfully");
            }
            break;
            case 2:{
                UIUtils.warningNotification("Update Complete!", "The information of employee has been"
                            + " updated successfully\n"
                        + "But the job category assigned to the employee has not updated successfully.");
            }
            break;
            case 3:{
                UIUtils.warningNotification("Update Complete!", "The information of employee has been"
                            + " updated successfully\n"
                        + "But job category is not assigned to the employee.");
            }
            break;
                
        }
    }
    
}
