package org.example.features.translation;


import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.example.AppContext;

public class TranslationService {
  private final TranslationRepository repo;
  private final LibreTranslateClient client;
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TranslationService.class);

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
    String targetLang = AppContext.getInstance().getLanguage().toString();
    if (targetLang == null || targetLang.isBlank() || targetLang.equals("en")) {
      return;
    }

    traverseAndTranslate(root, "en", targetLang);
  }

  private void traverseAndTranslate(Node node, String sourceLang, String targetLang) {
    switch (node) {
      case Labeled labeled -> {
        String originalText = labeled.getText();
        logger.info("Original text: {}", originalText);
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.info("Translated text: {}", translated);
            labeled.setText(translated);
          } catch (Exception e) {
            logger.error("Translation failed for: {}", originalText, e);
          }
        }
      }
      case Text textNode -> {
        String originalText = textNode.getText();
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.info("Translated text: {}", translated);
            textNode.setText(translated);
          } catch (Exception e) {
            logger.error("TxtNode failed for: {}", originalText, e);
          }
        }
      }
      case StackPane stackPane -> {
        logger.info("StackPane found");
        for (Node child : stackPane.getChildren()) {
          traverseAndTranslate(child, sourceLang, targetLang);
        }
      }
      case Region region -> {
        String originalText = region.getAccessibleText();
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.info("Translated text: {}", translated);
            region.setAccessibleText(translated);
          } catch (Exception e) {
            logger.error("Region failed for: {}", originalText, e);
          }
        }
      }
      default -> logger.info("Node type not handled: {}", node.getClass().getSimpleName());
    }

    if (node instanceof Parent parent) {
      for (Node child : parent.getChildrenUnmodifiable()) {
        traverseAndTranslate(child, sourceLang, targetLang);
      }
    }
  }
}
