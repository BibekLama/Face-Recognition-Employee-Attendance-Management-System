package attendancemanagement.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class ListViewPopUpController {
    private int empid;
    
    private JFXPopup popup;
    
    public void setEmpid(int empid) {
        this.empid = empid;
    }
    
    public void initPopup(){
        JFXListView<Label> list = new JFXListView<>();
        list.getStylesheets().add(getClass().getResource("/attendancemanagement/res/style.css").toExternalForm());
        list.getStyleClass().add("popupList");
        list.setPrefSize(100, 103);
        
        Label view = new Label("VIEW");
        view.setStyle("-fx-text-fill:#ffffff");
        ImageView viewIcon = new ImageView(new Image(getClass().getResource("/attendancemanagement/res/images/viewicon_sm.png").toExternalForm()));
        view.setGraphic(viewIcon);
        
        Label edit = new Label("EDIT");
        edit.setStyle("-fx-text-fill:#ffffff");
        ImageView editIcon = new ImageView(new Image(getClass().getResource("/attendancemanagement/res/images/editicon_sm.png").toExternalForm()));
        edit.setGraphic(editIcon);
        
        Label del = new Label("DELETE");
        del.setStyle("-fx-text-fill:#ffffff");
        ImageView delIcon = new ImageView(new Image(getClass().getResource("/attendancemanagement/res/images/deleteicon_sm.png").toExternalForm()));
        del.setGraphic(delIcon);
        
        list.getItems().add(view);
        list.getItems().add(edit);
        list.getItems().add(del);
        
        popup = new JFXPopup(list);
        
        list.setOnMouseClicked((MouseEvent event) -> {
            String menu = list.getSelectionModel().getSelectedItem().getText();
            selectMenu(menu,empid);
        }); 
    }
    
    private void selectMenu(String menu, int empid) {
        switch(menu){
            case "VIEW": System.out.println("View Employee Info");
            break;
            case "EDIT": System.out.println("Edit Employee Info");
            break;
            case "DELETE": System.out.println("Delete Employee Info");
            break;
        }
        popup.hide();
    }
    
    public void showPopupList(JFXListView<Label> list,String id,MouseEvent event){
        empid = Integer.parseInt(id);
        if(event.getButton() == MouseButton.SECONDARY) {
            popup.show(list,JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,event.getX(),event.getY());
        }
        if(event.getButton() == MouseButton.PRIMARY) {
            System.out.println("View Employee Info");
        }
    }
}
