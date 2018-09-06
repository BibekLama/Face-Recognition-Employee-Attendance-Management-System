package attendancemanagement.controller;

import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.JobCategoryService;
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


public class JobCategoryController implements Initializable {

    @FXML
    private TableView<JobCategory> jobsTableView;
    @FXML
    private TableColumn<JobCategory, Integer> jidCol;
    @FXML
    private TableColumn<JobCategory, String> titleCol;
    @FXML
    private TableColumn<JobCategory, String> nameCol;
    @FXML
    private TableColumn<JobCategory, LocalDateTime> addedCol;
    @FXML
    private TableColumn<JobCategory, LocalDateTime> modifiedCol;

    private ObservableList<JobCategory> jobCategories;
    
    private int jid;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jobCategories = FXCollections.observableArrayList();
        getJobsTableData();
        displayCategoriesTable();
    }    

    @FXML
    private void jobRegistrationForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/JobCategoryForm.fxml"));
            Parent root = loader.load();
            JobCategoryFormController jobformController = loader.getController();
            jobformController.jobRegistrationForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add Job Category");
            window.setResizable(false);
            Scene scene = new Scene(root);
            jobformController.setStage(window);
            window.setOnHidden(e -> {
                getJobsTableData();
                setDataInJobTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void jobEditForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UsersController.class.getResource(
                    "/attendancemanagement/view/JobCategoryForm.fxml"));
            Parent root = loader.load();
            JobCategoryFormController jobformController = loader.getController();
            jobformController.setJid(jid);
            jobformController.jobEditForm();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Edit Job Category");
            window.setResizable(false);
            Scene scene = new Scene(root);
            jobformController.setStage(window);
            window.setOnHidden(e -> {
                getJobsTableData();
                setDataInJobTable();
            });
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteJob(ActionEvent event) {
        boolean response = UIUtils.confirmNotification("Confirm Delete",
                "This will delete the job category from the list",
                "Are you ok with this?");
        if(response){
            JobCategoryService service = new JobCategoryService();
            int status = service.deleteJobCategory(jid);
            showMessage(status);
        }
    }

    private void displayCategoriesTable() {
        jobsTableView.getSelectionModel().selectedItemProperty().addListener((o,oldVal,newVal)->{
            if(newVal != null){
//                System.out.println(newVal.getEmpcode());
                jid = newVal.getJid();
            }
        });
        setDataInJobTable();
    }

    private void setDataInJobTable() {
        jidCol.setCellValueFactory(
                cellData -> cellData.getValue().jidProperty().asObject());
        titleCol.setCellValueFactory(
                cellData -> cellData.getValue().titleProperty());
        nameCol.setCellValueFactory(
                cellData -> cellData.getValue().display_nameProperty());
        addedCol.setCellValueFactory(
                cellData -> cellData.getValue().added_dateProperty());
        modifiedCol.setCellValueFactory(
                cellData -> cellData.getValue().last_modified_dateProperty());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a, MMM dd, yyyy");
        
        addedCol.setCellFactory(column -> {
                return new TableCell<JobCategory, LocalDateTime>() {
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
                return new TableCell<JobCategory, LocalDateTime>() {
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
        
        jobsTableView.setItems(jobCategories);
    }
    
    private void getJobsTableData() {
        JobCategoryService service = new JobCategoryService();
        jobCategories = service.getJobCategories();
    }
    
    private void showMessage(int status) {
        switch(status){
            case 0:{
                UIUtils.errorNotification("Delete Incomplete!", "Job Category has not been"
                            + " deleted successfully");
            }
            break;
            case 1:{
                UIUtils.successNotification("Delete Complete!", "Job Category has been"
                            + " successfully deleted");
                getJobsTableData();
                setDataInJobTable();
            }
            break;
            case 2:{
                UIUtils.warningNotification("Delete Complete!", "Multiple Job Category has been"
                            + "deleted.\n"
                        + "Job Category title must be unique.");
                getJobsTableData();
                setDataInJobTable();
            }
            break;
        }
    }
    
}
