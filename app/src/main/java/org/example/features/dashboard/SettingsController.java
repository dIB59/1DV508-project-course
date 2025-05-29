package org.example.features.dashboard;

import java.sql.Connection;

import org.example.shared.SceneRouter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.nio.file.Files;
import java.io.*;

public class SettingsController {
        
    @FXML private TextField nameField;
    @FXML private TextArea addressField;
    @FXML private TextArea contactField;
    @FXML private ImageView logoPreview;

    private byte[] newLogo;

    private final RestaurantSettingsRepository repo;
    private final SceneRouter sceneRouter;

    public SettingsController(Connection connection, SceneRouter sceneRouter) {
        this.repo = new RestaurantSettingsRepository(connection);
        this.sceneRouter = sceneRouter;
    }

    private Image byteArrayToImage(byte[] bytes) {
        return new Image(new ByteArrayInputStream(bytes));
    }
     @FXML
    public void initialize() {
        RestaurantSettings s = repo.loadSettings();
        nameField.setText(s.getName());
        addressField.setText(s.getAddress());
        contactField.setText(s.getContact());

        if (s.getLogoBlob() != null) {
            logoPreview.setImage(byteArrayToImage(s.getLogoBlob()));
        }
    }

    public void onChangeLogo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Logo");
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg","*.gif"));
        File file = fc.showOpenDialog(nameField.getScene().getWindow());
        if (file != null) {
            try {
                newLogo = Files.readAllBytes(file.toPath());
                logoPreview.setImage(byteArrayToImage(newLogo));
            } catch (IOException ex) {
                System.out.println(ex + " Unable to load file");
            }
        }
    }

    @FXML
    public void onSaveChanges() {
        if (nameField.getText().trim().isEmpty()) {
            System.out.println("Name cannot be empty."); return;
        }

        RestaurantSettings s = new RestaurantSettings();
        s.setName(nameField.getText().trim());
        s.setaddress(addressField.getText().trim());
        s.setContact(contactField.getText().trim());


        if (newLogo != null) {                 
            s.setLogoBlob(newLogo);
        } else {                               
            s.setLogoBlob(repo.loadSettings().getLogoBlob());
        }

        repo.saveSettings(s);
        System.out.println("Settings saved");

        sceneRouter.goToDashboardPage();
    }


    
}   
