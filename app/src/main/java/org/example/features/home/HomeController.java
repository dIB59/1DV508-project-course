package org.example.features.home;

import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.AppContext;
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
  private final HomeModel homeModel;
  private final TranslationService translationService;

  public boolean takeout = false;

  @FXML private Label welcomeLabel;
  @FXML private Button eatInButton;
  @FXML private Button takeOutButton;
  @FXML private ComboBox<Language> languageSelector;

  public HomeController(HomeModel homeModel, SceneRouter sceneRouter, OrderService orderService, TranslationService translationService) {
    this.homeModel = homeModel;
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
    this.translationService = translationService;
  }


  @FXML
  public void initialize() {
    log.info(Arrays.toString(Language.values()));
    languageSelector.getItems().setAll(Language.values());
    languageSelector
        .getSelectionModel()
        .select(AppContext.getInstance().getLanguage()); // Default selection
    languageSelector.getStyleClass().add("language-combo");
  }

  @FXML
  public void translatePage() {
    Language selectedLanguage = languageSelector.getValue();
    if (selectedLanguage == Language.ENGLISH) {
      translationService.reverseTranslate(welcomeLabel.getScene().getRoot()); // Restore original tex
      return;
    }
    if (selectedLanguage != null) {
      AppContext.getInstance().setLanguage(selectedLanguage); // Store choice globally
      translationService.translate(welcomeLabel.getScene().getRoot()); // Translate whole scene
    }
  }

  @FXML
  public void goToMenuPageTakeout() {
    sceneRouter.goToMenuPage();
    orderService.settype("Your order is for Take out");
    takeout = true;
  }

  @FXML
  public void goToMenuPageEatIn() {
    sceneRouter.goToMenuPage();
    orderService.settype("Your order is for Eat in ");
    takeout = false;
  }

  public void goToAdminPage() {
    sceneRouter.goToAdminLoginPage();
  }
}

