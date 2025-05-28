package org.example.shared;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.features.product.ProductDetailsController;
import org.example.features.translation.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The type Scene router. */
public class SceneRouter {

  private static final Logger log = LoggerFactory.getLogger(SceneRouter.class);
  private final Stage stage;
  private final Callback<Class<?>, Object> controllerFactory;
  private final TranslationService translationService;
  private KioskPage currentPage;

  /**
   * Instantiates a new Scene router.
   *
   * @param stage the stage
   * @param orderService the order service
   */
  public SceneRouter(
      Stage stage, OrderService orderService, TranslationService translationService) {
    this.stage = stage;
    this.controllerFactory = new AppControllerFactory(orderService, this);
    this.translationService = translationService;
  }

  // Fade in animation
  private void applyFadeInTransition(Scene scene) {
    FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), scene.getRoot());
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  private void coolTransition(Scene scene) {
    Transition transition = new TranslateTransition(Duration.millis(1000), scene.getRoot());
    transition.play();
  }

  /**
   * Sets the stage for the application.
   *
   * @param page The primary stage to be set.
   */
  public void goTo(KioskPage page) {
    try {
      // Load the main layout
      URL mainLayoutUrl = getClass().getResource("/BaseLayout.fxml");
      FXMLLoader mainLoader = new FXMLLoader(mainLayoutUrl);
      mainLoader.setControllerFactory(controllerFactory);
      Scene scene = new Scene(mainLoader.load());
      scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
      scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/theme-dark.css")).toExternalForm());

      BaseLayoutController mainLayoutController = mainLoader.getController();

      // Load the page content
      URL pageUrl = getClass().getResource("/" + page.getValue());
      FXMLLoader pageLoader = new FXMLLoader(pageUrl);
      pageLoader.setControllerFactory(controllerFactory);
      Node pageContent = pageLoader.load();

      // Inject content into layout
      mainLayoutController.setContent(pageContent);
      currentPage = page;

      // Set the scene
      stage.setScene(scene);
      translationService.translate(scene.getRoot());
      coolTransition(scene);

    } catch (IOException e) {
      log.error("Failed to load scene for page {}: {}", page, e.getMessage(), e);
    }
  }

  /**
   * Gets current page.
   *
   * @return the current page
   */
  public KioskPage getCurrentPage() {
    return currentPage;
  }

  /** Go to home page. */
  public void goToHomePage() {
    goTo(KioskPage.HOME);
  }

  /** Go to menu page. */
  public void goToMenuPage() {
    goTo(KioskPage.MENU);
  }

  /** Go to coupons page. */
  public void goToCouponsPage() {
    goTo(KioskPage.COUPONS);
  }

  /** Go to dashboard page. */
  public void goToDashboardPage() {
    goTo(KioskPage.DASHBOARD);
  }

  /** Go to checkout page. */
  public void goToCheckoutPage() {
    goTo(KioskPage.CHECKOUT);
  }

  public void goToProductDetailsPage(Product product) {
    try {
      URL url = getClass().getResource("/" + KioskPage.PRODUCTDESCRIPTION.getValue());
      FXMLLoader loader = new FXMLLoader(url);
      loader.setControllerFactory(controllerFactory);

      Scene scene = new Scene(loader.load());
      ProductDetailsController controller = loader.getController();
      controller.setProduct(product);

      // Apply theme and utility styles
      scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/theme-dark.css")).toExternalForm());
      scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

      log.debug("Product: {}", product);
      controller.displaySides();
      controller.displayIngredients();

      currentPage = KioskPage.PRODUCTDESCRIPTION;
      stage.setScene(scene);
      Platform.runLater(() -> translationService.translate(scene.getRoot()));

    } catch (IOException e) {
      log.error("Failed to load scene for page {}: {}", KioskPage.PRODUCTDESCRIPTION, e.getMessage(), e);
    }
  }

  public void goToReceiptPage() {
    goTo(KioskPage.RECEIPT);
  }

  public void goToAdminLoginPage() {
    goTo(KioskPage.ADMIN_LOGIN);
  }

  public void goToMemberLoginPage() {
    goTo(KioskPage.MEMBER_LOGIN);
  }

  public void goToPaymentPage() {
    goTo(KioskPage.PAYMENT);
  }

  public void goToFeedbackPage() {
    goTo(KioskPage.FEEDBACK);
  }

  public void goToSmallReceiptPage() {
    goTo(KioskPage.SMALLRECEIPT);
  }

  public void goToLanguagesPage() {
    goTo(KioskPage.EDIT_TRANSLATION);
  }

  public void refreshPage() {
    if (currentPage != null) {
      goTo(currentPage);
    } else {
      log.warn("Current page is null, cannot refresh.");
    }
  }

  /**
   * Enum representing the different pages in the kiosk application. Each enum constant corresponds
   * to a specific FXML file.
   *
   * <p>If you add a new page, make sure to update this enum and the corresponding FXML file.
   */
  public enum KioskPage {
    /** Home kiosk page. */
    HOME("HomeView.fxml"),
    /** Menu kiosk page. */
    MENU("MenuView.fxml"),
    /** Coupons kiosk page. */
    COUPONS("CouponsView.fxml"),
    /** Checkout kiosk page. */
    CHECKOUT("CheckoutView.fxml"),
    /** Payment Page. */
    PAYMENT("PaymentView.fxml"),
    /** Receipt kiosk page. */
    RECEIPT("ReceiptView.fxml"),
    /** Dashboard kiosk page. */
    DASHBOARD("DashboardView.fxml"),
    /** Admin login kiosk page. */
    ADMIN_LOGIN("AdminLoginView.fxml"),
    // Product description page
    PRODUCTDESCRIPTION("ProductDescription.fxml"),
    // Member login page
    MEMBER_LOGIN("MemberLoginView.fxml"),
    // Feedback page
    FEEDBACK("CustomerFeedback.fxml"),
    // Edit translation page
    EDIT_TRANSLATION("EditTranslationView.fxml"),

    SMALLRECEIPT("SmallReceiptView.fxml");

    private final String value;

    KioskPage(String value) {
      this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
      return value;
    }
  }
}
