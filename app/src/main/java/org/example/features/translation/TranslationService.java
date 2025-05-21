package org.example.features.translation;


import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.text.Text;
import org.example.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationService {
  private final TranslationRepository repo;
  private final LibreTranslateClient client;
  private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);

  public TranslationService(TranslationRepository repo, LibreTranslateClient client) {
    this.repo = repo;
    this.client = client;
  }

  public String get(String text, String sourceLang, String targetLang) throws Exception {
    Optional<String> cached = repo.find(text, sourceLang, targetLang);
    if (cached.isPresent()) {
      return cached.get();
    }

    String translated = client.translate(text, targetLang, sourceLang);
    repo.save(text, sourceLang, targetLang, translated);
    return translated;
  }

  public void translate(Node root) {
    Language targetLang = AppContext.getInstance().getLanguage();
    if (targetLang == null) {
      return;
    }

    traverseAndTranslate(root, Language.ENGLISH.toString(), targetLang.toString());
  }

  public void reverseTranslate(Node root) {
    traverseAndRestore(root);
  }

  private void traverseAndRestore(Node node) {
    switch (node) {
      case Labeled labeled -> {
        Object original = labeled.getProperties().get("originalText");
        if (original instanceof String originalText) {
          labeled.setText(originalText);
        }
      }
      case Text textNode -> {
        Object original = textNode.getProperties().get("originalText");
        if (original instanceof String originalText) {
          textNode.setText(originalText);
        }
      }
      default -> logger.info("Node type not handled: {}", node.getClass().getSimpleName());
    }

    if (node instanceof Parent parent) {
      for (Node child : parent.getChildrenUnmodifiable()) {
        traverseAndRestore(child);
      }
    }
  }

  private void traverseAndTranslate(Node node, String sourceLang, String targetLang) {
    switch (node) {
      case Labeled labeled -> {
        if (labeled.textProperty().isBound()) {
          logger.debug("Label is bound, skipping translation: {}", labeled);
          return;
        }
        String originalText = labeled.getText();
        if (labeled.getProperties().get("originalText") != null) {
          originalText = (String) labeled.getProperties().get("originalText");
        }
        logger.debug("Original text: {}", originalText);
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.debug("Translated label: {}", translated);
            labeled.getProperties().put("originalText", originalText);
            labeled.setText(translated);
          } catch (Exception e) {
            logger.error("Translation failed for: {}", originalText, e);
          }
        }
      }
      case Text textNode -> {
        if (textNode.textProperty().isBound()) {
          logger.debug("Text is bound, skipping translation: {}", textNode);
          return;
        }
        String originalText = textNode.getText();
        if (textNode.getProperties().get("originalText") != null) {
          originalText = (String) textNode.getProperties().get("originalText");
        }
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.debug("Translated TxtNode: {}", translated);
            textNode.getProperties().put("originalText", originalText);
            textNode.setText(translated);
          } catch (Exception e) {
            logger.error("TxtNode failed for: {}", originalText, e);
          }
        }
      }
      default -> logger.debug("Node type not handled: {}", node.getClass().getSimpleName());
    }

    if (node instanceof Parent parent) {
      for (Node child : parent.getChildrenUnmodifiable()) {
        traverseAndTranslate(child, sourceLang, targetLang);
      }
    }
  }
}
