package org.example.features.home;

import java.util.Arrays;
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

/** The type Home controller. */
public class HomeController {

  private final Logger log = LoggerFactory.getLogger(HomeController.class);
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private final TranslationService translationService;

  @FXML private Label welcomeLabel;
  @FXML private ComboBox<Language> languageSelector;

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
    languageSelector.getStyleClass().add("language-combo");

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
      AppContext.getInstance().setLanguage(Language.ENGLISH);
      translationService.reverseTranslate(welcomeLabel.getScene().getRoot());
      return;
    }
    if (selectedLanguage != null) {
      AppContext.getInstance().setLanguage(selectedLanguage);
      translationService.translate(welcomeLabel.getScene().getRoot());
    }
  }

  @FXML
  public void goToMenuPageTakeout() {
    orderService.clear();
    orderService.setType(Order.Type.TAKEOUT);
    sceneRouter.goToMenuPage();
  }

  @FXML
  public void goToMenuPageEatIn() {
    orderService.clear();
    orderService.setType(Order.Type.EAT_IN);
    sceneRouter.goToMenuPage();
  }

  public void goToAdminPage() {
    log.debug("Navigating to admin login page...");
    sceneRouter.goToAdminLoginPage();
  }
}
