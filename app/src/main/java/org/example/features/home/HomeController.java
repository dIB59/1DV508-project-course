package org.example.features.home;

import java.util.Arrays;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.example.AppContext;
import org.example.features.order.Order;
import org.example.features.order.OrderService;
import org.example.features.translation.Language;
import org.example.features.translation.TranslationService;
import org.example.shared.SceneRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.Scene;

/** The type Home controller. */
public class HomeController {

  private final Logger log = LoggerFactory.getLogger(HomeController.class);
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private final TranslationService translationService;
  @FXML private Label welcomeLabel;
  @FXML private ComboBox<Language> languageSelector;
  @FXML private ToggleButton darkModeToggle;


  public HomeController(
      SceneRouter sceneRouter, OrderService orderService, TranslationService translationService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
    this.translationService = translationService;
  }

  @FXML
  public void initialize() {
    log.debug(Arrays.toString(Language.values()));
    languageSelector.getItems().setAll(Language.values());
    languageSelector
        .getSelectionModel()
        .select(AppContext.getInstance().getLanguage()); // Default selection
    languageSelector.getStyleClass()
        .addAll("text-primary", "border-muted", "bg-background", "round-md", "px-2");
    // Setup keyboard shortcut after scene is ready
    welcomeLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene != null) {
        addAdminShortcut(newScene);
      }
    });
  }

  private void addAdminShortcut(Scene scene) {
    KeyCombination adminShortcut = new KeyCodeCombination(
        KeyCode.A,
        KeyCombination.SHIFT_DOWN
    );

    scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
      if (adminShortcut.match(event)) {
        log.debug("Admin shortcut pressed.");
        goToAdminPage();
      }
    });
  }

  @FXML
  public void translatePage() {
    Language selectedLanguage = languageSelector.getValue();
    if (selectedLanguage == Language.ENGLISH) {
      AppContext.getInstance().setLanguage(Language.ENGLISH); // Store choice globally
      translationService.reverseTranslate(
          welcomeLabel.getScene().getRoot()); // Restore original tex
      return;
    }
    if (selectedLanguage != null) {
      AppContext.getInstance().setLanguage(selectedLanguage); // Store choice globally
      translationService.translate(welcomeLabel.getScene().getRoot()); // Translate whole scene
    }
  }

  @FXML
  public void goToMenuPageTakeout() {
    orderService.clear();
    orderService.setType(Order.Type.TAKEAWAY);
    sceneRouter.goToMenuPage();
  }

  @FXML
  public void goToMenuPageEatIn() {
    orderService.clear();
    orderService.setType(Order.Type.EAT_IN);
    sceneRouter.goToMenuPage();
  }
  @FXML
  public void toggleDarkMode() {
    Scene scene = darkModeToggle.getScene();
    AppContext.getInstance().setDarkMode(darkModeToggle.isSelected());
    scene.getStylesheets().clear();
    scene.getStylesheets().add(AppContext.getInstance().getCurrentTheme());
    scene.getStylesheets().add(AppContext.getInstance().BASE_THEME);
  }


  public void goToAdminPage() {
    log.debug("Navigating to admin login page...");
    sceneRouter.goToAdminLoginPage();
  }
}
