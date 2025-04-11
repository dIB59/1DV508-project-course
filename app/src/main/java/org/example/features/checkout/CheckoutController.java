package org.example.features.checkout;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.Item;
import org.example.router.SceneRouter;

public class CheckoutController implements Initializable {

  @FXML private Label itemCountLabel;
  @FXML private Button homeButton;
  @FXML private ListView<String> itemListView;

  private final CheckoutModel model = CheckoutModel.getInstance();


  public void goToHomePage(ActionEvent actionEvent) {
    SceneRouter.goToHomePage();
  }


  public List<Item> getItems() {
    return model.getItems();
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    int itemCount = model.getItems().size();
    itemCountLabel.setText("Items in cart: " + itemCount);

    // Populate ListView
    for (Item item : model.getItems()) {
      itemListView.getItems().add(item.toString()); // or item.getName(), etc.
    }
  }
}
