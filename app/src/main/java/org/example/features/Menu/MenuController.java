package org.example.features.Menu;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.router.SceneRouter;

public class MenuController {
    
    @FXML private Label menuLable;
    
    private final MenuModel model = new MenuModel();

    @FXML 
    public void initialize(URL url, ResourceBundle resourceBundle){
        
    }

}
