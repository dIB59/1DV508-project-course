package org.example.shared;

import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.features.product.ProductDetailsController;
import org.example.features.translation.TranslationService;

/** The type Scene router. */
public class SceneRouter {

  private final Stage stage;
  private final Callback<Class<?>, Object> controllerFactory;
  private KioskPage currentPage;
  private final TranslationService translationService;

  /**
   * Instantiates a new Scene router.
   *
   * @param stage the stage
   * @param orderService the order service
   */
  public SceneRouter(Stage stage, OrderService orderService, TranslationService translationService) {
    this.stage = stage;
    this.controllerFactory = new AppControllerFactory(orderService, this);
    this.translationService = translationService;
  }

  /**
   * Sets the stage for the application.
   *
   * @param page The primary stage to be set.
   */
  public void goTo(KioskPage page) {
    try {
      URL url = getClass().getResource("/" + page.getValue());
      FXMLLoader loader = new FXMLLoader(url);
      loader.setControllerFactory(controllerFactory);
      currentPage = page;
      Scene scene = new Scene(loader.load());

      stage.setScene(scene);
      translationService.translate(scene.getRoot());
    } catch (IOException e) {
      System.err.println("Failed to load scene: " + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }


  /** Refresh page. */
  public void refreshPage() {
    goTo(currentPage);
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

      System.out.println();
      Scene scene = new Scene(loader.load());
      ProductDetailsController controller = loader.getController();
      System.out.println(product);
      controller.setProduct(product); // Set the product after loading the FXML
      controller.displaySides();
      controller.displayIngredients();
      currentPage = KioskPage.PRODUCTDESCRIPTION;
      stage.setScene(scene);
      Platform.runLater(() -> {
        translationService.translate(scene.getRoot());
      });
    } catch (IOException e) {
      System.err.println("Failed to load Product Details page: " + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  public void goToReceiptPage() {
    goTo(KioskPage.RECEIPT);
  }

  public void goToAdminLoginPage() {
    goTo(KioskPage.ADMIN_LOGIN);
  }

  public void goToMemberLoginPage(){goTo(KioskPage.MEMBER_LOGIN);}

  public void goToPaymentPage() {
    goTo(KioskPage.PAYMENT);
  }

  public void goToFeedbackPage() {
    goTo(KioskPage.FEEDBACK);
  }

  public void goToLanguagesPage() {
    goTo(KioskPage.EDIT_TRANSLATION);
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
    EDIT_TRANSLATION("EditTranslationView.fxml");
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
