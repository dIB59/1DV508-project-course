package org.example.features.translation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.shared.SceneRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditTranslationController {

  private static final Logger logger = LoggerFactory.getLogger(EditTranslationController.class);
  private final TranslationRepository translationRepository;
  private final TranslationService translationService;
  private final SceneRouter sceneRouter;
  private final ObservableList<Translation> allTranslations = FXCollections.observableArrayList();
  private final ObservableList<Translation> filteredTranslations =
      FXCollections.observableArrayList();
  private Translation currentTranslation;
  @FXML private TextField searchField;
  @FXML private ComboBox<Language> languageComboBox;
  @FXML private TableView<Translation> translationTable;
  @FXML private TableColumn<Translation, String> originalColumn;
  @FXML private TableColumn<Translation, String> translatedColumn;
  @FXML private Button cacheButton;
  @FXML private TextField originalTextField;
  @FXML private TextField translatedTextField;
  @FXML private ProgressBar cacheProgressBar;

  public EditTranslationController(
      SceneRouter sceneRouter,
      TranslationRepository translationRepository,
      TranslationService translationService) {
    this.sceneRouter = sceneRouter;
    this.translationRepository = translationRepository;
    this.translationService = translationService;
  }

  @FXML
  private void initialize() {
    // Populate available languages
    languageComboBox.setItems(FXCollections.observableArrayList(Language.values()));
    languageComboBox.setValue(Language.ENGLISH); // default

    // Set up table and listeners (same as before)
    originalColumn.setCellValueFactory(
        cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrignalText()));
    translatedColumn.setCellValueFactory(
        cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTranslatedText()));

    translationTable.setItems(filteredTranslations);

    translationTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSel, newSel) -> {
              if (newSel != null) {
                currentTranslation = newSel;
                originalTextField.setText(newSel.getOrignalText());
                translatedTextField.setText(newSel.getTranslatedText());
              }
            });

    // Initial load
    loadTranslations();
  }

  private void loadTranslations() {
    Language selected = languageComboBox.getValue();
    if (selected != null) {
      allTranslations.setAll(translationRepository.findAll(selected));
      filterTranslations(); // Apply current search
    }
  }

  private void filterTranslations() {
    String query = searchField.getText();
    if (query == null || query.isBlank()) {
      filteredTranslations.setAll(allTranslations);
      return;
    }

    String lowerCaseQuery = query.toLowerCase();
    filteredTranslations.setAll(
        allTranslations.stream()
            .filter(
                t ->
                    t.getOrignalText().toLowerCase().contains(lowerCaseQuery)
                        || t.getTranslatedText().toLowerCase().contains(lowerCaseQuery))
            .toList());
  }

  @FXML
  private void handleSave() {
    if (currentTranslation != null) {
      Translation updated =
          new Translation(
              currentTranslation.getId(),
              originalTextField.getText(),
              currentTranslation.getSourceLang(),
              currentTranslation.getTargetLang(),
              translatedTextField.getText());
      translationRepository.update(updated);

      // Refresh table
      loadTranslations();
    }
  }

  @FXML
  private void handleCacheTranslations() {
    Language targetLanguage = languageComboBox.getValue();

    if (targetLanguage == null) {
      showAlert("Please select a target language first.");
      return;
    }

    String originalText = cacheButton.getText();
    cacheButton.setDisable(true);
    cacheButton.setText("Caching...");
    cacheProgressBar.setProgress(0);
    cacheProgressBar.setVisible(true);

    translationService
        .cacheTranslationsAsync(
            Language.ENGLISH.toString(),
            targetLanguage.toString(),
            progress -> Platform.runLater(() -> cacheProgressBar.setProgress(progress)))
        .thenAccept(
            successCount ->
                Platform.runLater(
                    () -> {
                      cacheButton.setDisable(false);
                      cacheButton.setText(originalText);
                      cacheProgressBar.setVisible(false);
                      showAlert(
                          "Successfully cached "
                              + successCount
                              + " translations for "
                              + targetLanguage);
                      loadTranslations();
                    }))
        .exceptionally(
            ex -> {
              Platform.runLater(
                  () -> {
                    cacheButton.setDisable(false);
                    cacheButton.setText(originalText);
                    cacheProgressBar.setVisible(false);
                    logger.error("Error caching translations", ex);
                    showAlert("Failed to cache translations: " + ex.getMessage());
                  });
              return null;
            });
  }

  private void showAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Translation Cache");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void handleLanguageChange(ActionEvent actionEvent) {
    loadTranslations();
  }

  @FXML
  private void handleSearch() {
    filterTranslations();
  }

  public void handleBackToDashboard(ActionEvent actionEvent) {
    sceneRouter.goToDashboardPage();
  }

  public void goToProductsPage(ActionEvent actionEvent) {
    sceneRouter.goToDashboardPage();
  }

  public void goToLanguagesPage(ActionEvent actionEvent) {
    sceneRouter.goToLanguagesPage();
  }

  public void goToHomePage(ActionEvent actionEvent) {
    sceneRouter.goToHomePage();
  }

  public void goToCouponsPage(ActionEvent actionEvent) {
    sceneRouter.goToCouponsPage();
  }

  public void goToSettingsPage(ActionEvent actionEvent) {
    sceneRouter.goToSettingsPage();
  }
}
