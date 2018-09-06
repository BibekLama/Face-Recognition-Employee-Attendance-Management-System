package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.JobCategoryService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class JobCategoryFormController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField displayNameField;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane jobCategoryForm;
    private Stage stage;
    private boolean edit;
    private int jid;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setJid(int jid) {
        this.jid = jid;
    }

    @FXML
    private void submitForm(ActionEvent event) {
        if(!titleField.getText().isEmpty()){
            if(!displayNameField.getText().isEmpty()){
                if(edit){
                    editJobCategory();
                }else{
                    addJobCategory();
                }
            }else{
                UIUtils.warningNotification("Add Incomplete!", "Display Name field is empty.");
            }
        }else{
            UIUtils.warningNotification("Add Incomplete!", "Title field is empty.\n"
                            + "Enter unique job title in the title field.");
        }
    }

    public void setStage(Stage window) {
        stage = window;
    }

    public void jobRegistrationForm() {
       edit = false;
       submitButton.setText("ADD CATEGORY");
    }

    private void addJobCategory() {
        JobCategory cat = new JobCategory();
        cat.setTitle(titleField.getText());
        cat.setDisplay_name(displayNameField.getText());
        JobCategoryService service = new JobCategoryService();
        int status = service.addJobCategory(cat);
        showMessage(status);
    }
    
    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Add Incomplete!", "New Job Category has not been"
                            + " added successfully");
            }
            break;
            case 1:{
                UIUtils.successNotification("Add Complete!", "New Job Category has been"
                            + " successfully added");
                stage.close();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Add Complete!", "Multiple Job Category has been"
                            + " added \n"
                        + "Job Category title should be unique");
                stage.close();
            }
            break;
            case 4:{
                UIUtils.warningNotification("Job Category Exist!", "Job Category is already exist."
                            + "\n"
                        + "Job Category title should be unique");
            }
            break;
            case 5:{
                UIUtils.errorNotification("Edit Incomplete!", "Job Category has not been"
                            + " edited successfully");
            }
            break;
            case 6:{
                UIUtils.successNotification("Edit Complete!", "Job Category has been"
                            + " successfully edited");
                stage.close();
            }
            break;
            case 7:{
                UIUtils.warningNotification("Edit Complete!", "Multiple Job Category has been"
                            + " edited \n"
                        + "Job Category title should be unique");
                stage.close();
            }
            break;
        }
    }

    void jobEditForm() {
       edit = true;
       submitButton.setText("EDIT CATEGORY");
       JobCategoryService service = new JobCategoryService();
       JobCategory cat = service.getJobCatById(jid);
       if(cat != null){
           titleField.setText(cat.getTitle());
           displayNameField.setText(cat.getDisplay_name());
       }
    }

    private void editJobCategory() {
        JobCategory cat = new JobCategory();
        cat.setJid(jid);
        cat.setTitle(titleField.getText());
        cat.setDisplay_name(displayNameField.getText());
        JobCategoryService service = new JobCategoryService();
        int status = service.editJobCategory(cat);
        showMessage(status);
    }
    
}
