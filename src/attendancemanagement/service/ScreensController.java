package attendancemanagement.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class ScreensController extends StackPane {
    private HashMap<String,Node> scenes = new HashMap<>();
    private HashMap<String,ControlledScene> controlledScene = new HashMap<>();
    public  String controlledScreen;
   
    public ScreensController() {
        super();
    }
    
    // Add Scene in scenes collection
    public void addScreen(String name, Node scene){
        scenes.put(name,scene);
    }
    
    public void addControlledScene(String name, ControlledScene scene){
        controlledScene.put(name,scene);
    }
    
    
    // Get Scene node with appropriate name
    public Node getScreen(String name){
        return scenes.get(name);
    }
    
    
    
    //Load Scene in scenes collection
    public boolean LoadScreen(String name, String resource){
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            HBox parent = (HBox) myLoader.load();
            
            ControlledScene mySceneController = ((ControlledScene) myLoader.getController());
            mySceneController.setScreenParent(this);
            addScreen(name,parent);
            addControlledScene(name, mySceneController);
            return true;
            
        } catch (IOException ex) {
            Logger.getLogger(ScreensController.class.getName()).log(Level.SEVERE, null, "Hello World "+ex.getCause());
            return false;
        }
    }
    
    // Set Screen in Window
    
    public boolean setScreen(String name,StackPane mainContainer){
        
        if(scenes.get(name) != null){
            
            final DoubleProperty opacity = opacityProperty();
            
            if(!mainContainer.getChildren().isEmpty()){
                FadeTransition fadeOut = new FadeTransition(Duration.millis(2000));
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
                
                mainContainer.getChildren().remove(0);
                
                        
                FadeTransition fadeIn = new FadeTransition(Duration.millis(2000));
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
                
                mainContainer.getChildren().add(0,scenes.get(name));
            }
            else
            {
                setOpacity(0.0);
                mainContainer.getChildren().add(scenes.get(name));
                FadeTransition fadeIn = new FadeTransition(Duration.millis(2000));
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
            return true;
        }
        else
        {
            System.out.println("Scene has not been loaded");
            return false;
        }  
    }
    
   
    // Unload Scene from scenes collection
    public boolean unloadScene(String name){
        if(scenes.remove(name) == null){
            System.out.println("Scene does't Exist.");
            return false;
        }
        else
        {
            return true;
        }
    }
}
