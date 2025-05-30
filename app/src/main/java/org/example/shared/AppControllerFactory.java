package org.example.shared;

import java.sql.Connection;
import javafx.util.Callback;
import org.example.database.Database;
import org.example.features.admin.AdminController;
import org.example.features.admin.AdminMapper;
import org.example.features.admin.AdminRepository;
import org.example.features.campaign.CampaignMapper;
import org.example.features.campaign.CampaignRepository;
import org.example.features.checkout.CheckoutController;
import org.example.features.coupons.CouponsController;
import org.example.features.coupons.CouponsRepository;
import org.example.features.dashboard.DashboardController;
import org.example.features.dashboard.RestaurantSettingsRepository;
import org.example.features.dashboard.SettingsController;
import org.example.features.feedback.FeedbackController;
import org.example.features.help.HelpController;
import org.example.features.home.HomeController;
import org.example.features.ingredients.IngredientMapper;
import org.example.features.ingredients.IngredientsRepository;
import org.example.features.menu.MenuController;
import org.example.features.order.OrderService;
import org.example.features.payments.FreePay;
import org.example.features.payments.PaymentController;
import org.example.features.product.ProductDetailsController;
import org.example.features.product.ProductMapper;
import org.example.features.product.ProductRepository;
import org.example.features.receipt.ReceiptController;
import org.example.features.smallreceipt.SmallReceiptController;
import org.example.features.translation.EditTranslationController;
import org.example.features.translation.LibreTranslateClient;
import org.example.features.translation.TranslationRepository;
import org.example.features.translation.TranslationService;
import org.example.members.MemberController;
import org.example.members.MemberMapper;
import org.example.members.MemberRepository;

/**
 * AppControllerFactory is a factory class that creates instances of controllers based on the
 * controller class name. It implements the Callback interface to provide a way to create controller
 * instances dynamically.
 */
public class AppControllerFactory implements Callback<Class<?>, Object> {

  private final OrderService orderService;
  private final SceneRouter sceneRouter;
  private final LibreTranslateClient translateClient;

  private final Connection connection = Database.getInstance().getConnection();

  /**
   * Instantiates a new App controller factory.
   *
   * @param orderService the order service
   * @param sceneRouter the scene router
   */
  public AppControllerFactory(OrderService orderService, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
    this.translateClient = new LibreTranslateClient();
  }

  /**
   * Creates a controller instance based on the provided class. If you add a new controller, you
   * need to add a case for it in this method.
   *
   * @param controllerClass The class of the controller to be created.
   * @return An instance of the specified controller class.
   */
  @Override
  public Object call(Class<?> controllerClass) {
    return switch (controllerClass.getSimpleName()) {
      case "HomeController" ->
          new HomeController(sceneRouter, orderService, getTranslationService());
      case "MenuController" ->
          new MenuController(getProductRepository(), sceneRouter, getTranslationService());
      case "CheckoutController" ->
          new CheckoutController(
              orderService, getCouponsRepository(), sceneRouter, getCampaignRepository());
      case "ProductDetailsController" ->
          new ProductDetailsController(orderService, sceneRouter, getProductRepository());
      case "ReceiptController" ->
          new ReceiptController(
              orderService.saveOrderAndClear(), sceneRouter, getMemberRepository(), getRestaurantSettingsRepository() );
      case "SmallReceiptController" -> new SmallReceiptController(sceneRouter, orderService);
      case "AdminController" -> new AdminController(sceneRouter, getAdminRepository());
      case "MemberController" ->
          new MemberController(sceneRouter, getMemberRepository(), orderService);
      case "FeedbackController" -> new FeedbackController(sceneRouter, orderService);
      case "DashboardController" ->
          new DashboardController(sceneRouter, getProductRepository(), getIngredientsRepository());
      case "CouponsController" -> new CouponsController(getCouponsRepository(), sceneRouter);
      case "HelpController" -> new HelpController(sceneRouter);
        case "PaymentController" -> new PaymentController(sceneRouter, orderService, new FreePay());
      case "EditTranslationController" ->
          new EditTranslationController(
              sceneRouter, getTranslationRepository(), getTranslationService());
      case "SettingsController" -> new SettingsController(connection, sceneRouter);
      case "BaseLayoutController" -> new BaseLayoutController(getRestaurantSettingsRepository(), sceneRouter);
      default ->
          throw new IllegalArgumentException(
              "No constructor found for class: " + controllerClass.getSimpleName());
    };
  }

  private RestaurantSettingsRepository getRestaurantSettingsRepository() {
    return new RestaurantSettingsRepository(connection);
  }

  private ProductRepository getProductRepository() {
    IngredientsRepository ingredientsRepository =
        new IngredientsRepository(connection, new IngredientMapper());
    ProductMapper productMapper = new ProductMapper(ingredientsRepository);
    return new ProductRepository(connection, productMapper);
  }

  private AdminRepository getAdminRepository() {
    return new AdminRepository(connection, new AdminMapper());
  }

  private MemberRepository getMemberRepository() {
    return new MemberRepository(connection, new MemberMapper());
  }

  private CouponsRepository getCouponsRepository() {
    return new CouponsRepository(connection);
  }

  private CampaignRepository getCampaignRepository() {
    return new CampaignRepository(connection, new CampaignMapper());
  }

  private TranslationService getTranslationService() {
    return new TranslationService(getTranslationRepository(), translateClient);
  }

  private TranslationRepository getTranslationRepository() {
    return new TranslationRepository(connection);
  }

  private IngredientsRepository getIngredientsRepository() {
    return new IngredientsRepository(connection, new IngredientMapper());
  }
}
