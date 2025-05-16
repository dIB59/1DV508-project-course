package org.example.features.translation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.example.shared.SceneRouter;


public class EditTranslationController {

  private final TranslationRepository translationRepository;
  private final SceneRouter sceneRouter;

  private Translation currentTranslation;

  @FXML private TextField searchField;
  @FXML private ComboBox<Language> languageComboBox;
  @FXML private TableView<Translation> translationTable;
  @FXML private TableColumn<Translation, String> originalColumn;
  @FXML private TableColumn<Translation, String> translatedColumn;

  @FXML private TextField originalTextField;
  @FXML private TextField translatedTextField;

  private final ObservableList<Translation> allTranslations = FXCollections.observableArrayList();
  private final ObservableList<Translation> filteredTranslations = FXCollections.observableArrayList();

  public EditTranslationController(SceneRouter sceneRouter, TranslationRepository translationRepository) {
    this.sceneRouter = sceneRouter;
    this.translationRepository = translationRepository;
  }

  @FXML
  private void initialize() {
    // Populate available languages
    languageComboBox.setItems(FXCollections.observableArrayList(Language.values()));
    languageComboBox.setValue(Language.ENGLISH); // default

    // Set up table and listeners (same as before)
    originalColumn.setCellValueFactory(cellData ->
        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrignalText())
    );
    translatedColumn.setCellValueFactory(cellData ->
        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTranslatedText())
    );

    translationTable.setItems(filteredTranslations);

    translationTable.getSelectionModel().selectedItemProperty().addListener(
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
            .filter(t -> t.getOrignalText().toLowerCase().contains(lowerCaseQuery)
                || t.getTranslatedText().toLowerCase().contains(lowerCaseQuery))
            .toList()
    );
  }


  @FXML
  private void handleSave() {
    if (currentTranslation != null) {
      Translation updated = new Translation(
          currentTranslation.getId(),
          originalTextField.getText(),
          currentTranslation.getSourceLang(),
          currentTranslation.getTargetLang(),
          translatedTextField.getText()
      );
      translationRepository.update(updated);

      // Refresh table
      loadTranslations();
    }
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
}