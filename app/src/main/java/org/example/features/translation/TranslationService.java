package org.example.features.translation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
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

  /**
   * Asynchronously caches translations for all texts from source language to target language
   * @param sourceLang Source language code
   * @param targetLang Target language code
   * @return CompletableFuture with the count of successfully cached translations
   */

  public CompletableFuture<Integer> cacheTranslationsAsync(
      String sourceLang,
      String targetLang,
      Consumer<Double> progressCallback
  ) {
    return CompletableFuture.supplyAsync(() -> {
      List<String> textsToTranslate = repo.findUntranslatedTexts(sourceLang, targetLang);
      int total = textsToTranslate.size();
      int successCount = 0;

      logger.info("Found {} texts to translate from {} to {}", total, sourceLang, targetLang);

      for (int i = 0; i < total; i++) {
        String text = textsToTranslate.get(i);
        try {
          String translated = client.translate(text, targetLang, sourceLang);
          repo.save(text, sourceLang, targetLang, translated);
          successCount++;
        } catch (Exception e) {
          logger.error("Failed to cache translation for: {}", text, e);
        }

        // Update progress after each item
        double progress = (i + 1) / (double) total;
        progressCallback.accept(progress);
      }

      logger.info("Successfully cached {} translations from {} to {}", successCount, sourceLang, targetLang);
      return successCount;
    });
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
        String originalText = labeled.getText();
        if (labeled.getProperties().get("originalText") != null) {
          originalText = (String) labeled.getProperties().get("originalText");
        }
        logger.info("Original text: {}", originalText);
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.info("Translated label: {}", translated);
            labeled.getProperties().put("originalText", originalText);
            labeled.setText(translated);
          } catch (Exception e) {
            logger.error("Translation failed for: {}", originalText, e);
          }
        }
      }
      case Text textNode -> {
        String originalText = textNode.getText();
        if (textNode.getProperties().get("originalText") != null) {
          originalText = (String) textNode.getProperties().get("originalText");
        }
        if (originalText != null && !originalText.isBlank()) {
          try {
            String translated = get(originalText, sourceLang, targetLang);
            logger.info("Translated TxtNode: {}", translated);
            textNode.getProperties().put("originalText", originalText);
            textNode.setText(translated);
          } catch (Exception e) {
            logger.error("TxtNode failed for: {}", originalText, e);
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