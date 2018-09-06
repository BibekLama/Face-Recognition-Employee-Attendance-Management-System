package attendancemanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SplashScreenController implements Initializable {

    @FXML
    private AnchorPane SplashScreen;
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        new Splash().start();
    } 
    
    private class Splash extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Parent root = null;
                            root = FXMLLoader.load(getClass().getResource("/attendancemanagement/view/LoginScreen.fxml"));
                            
                            Stage stage = new Stage();
                            Scene scene = new Scene( root );
                            stage.setScene(scene);
                            stage.setMinWidth(600);
                            stage.setMinHeight(450);
                            stage.setResizable(true);
                            stage.setTitle("Attendance Management System");
                            SplashScreen.getScene().getWindow().hide();
                            stage.show();
                            
                        } catch (IOException ex) {
                            System.out.println("Unable to load Login.fxml");
                            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
}
