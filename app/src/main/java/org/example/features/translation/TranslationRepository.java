package org.example.features.translation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationRepository {
  private final Connection conn;
  private static final Logger logger = LoggerFactory.getLogger(TranslationRepository.class);

  public TranslationRepository(Connection conn) {
    this.conn = conn;
  }

  private String hashText(String text) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(text.getBytes());
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 not available", e);
    }
  }

  public Optional<String> find(String text, String sourceLang, String targetLang)
      throws SQLException {
    String textHash = hashText(text);
    PreparedStatement stmt =
        conn.prepareStatement(
            """
            SELECT translated_text FROM Translations
            WHERE original_text_hash = ? AND source_lang = ? AND target_lang = ?
        """);
    stmt.setString(1, textHash);
    stmt.setString(2, sourceLang);
    stmt.setString(3, targetLang);
    ResultSet rs = stmt.executeQuery();
    return rs.next() ? Optional.of(rs.getString("translated_text")) : Optional.empty();
  }

  public void save(String text, String sourceLang, String targetLang, String translatedText)
      throws SQLException {
    String textHash = hashText(text);

    if (find(text, sourceLang, targetLang).isPresent()) {
      PreparedStatement stmt =
          conn.prepareStatement(
              """
              UPDATE Translations
              SET translated_text = ?
              WHERE original_text_hash = ? AND source_lang = ? AND target_lang = ?
          """);
      stmt.setString(1, translatedText);
      stmt.setString(2, textHash);
      stmt.setString(3, sourceLang);
      stmt.setString(4, targetLang);
      stmt.executeUpdate();
      return;
    }

    PreparedStatement stmt =
        conn.prepareStatement(
            """
            INSERT INTO Translations
            (original_text_hash, original_text, source_lang, target_lang, translated_text)
            VALUES (?, ?, ?, ?, ?)
        """);
    stmt.setString(1, textHash);
    stmt.setString(2, text);
    stmt.setString(3, sourceLang);
    stmt.setString(4, targetLang);
    stmt.setString(5, translatedText);
    stmt.executeUpdate();
  }

  public void update(Translation translation) {
    try {
      PreparedStatement stmt =
          conn.prepareStatement(
              """
              UPDATE Translations
              SET translated_text = ?
              WHERE id = ?
          """);
      stmt.setString(1, translation.getTranslatedText());
      stmt.setInt(2, translation.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error updating translation with ID: {}", translation.getId(), e);
      throw new RuntimeException("Error updating translation", e);
    }
  }

  public List<Translation> findAll(Language language) {
    List<Translation> translations = new ArrayList<>();
    String tarLang = language.toString();

    String query = """
      SELECT id, original_text, source_lang, target_lang, translated_text
      FROM Translations
      WHERE target_lang = ?
      """;

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, tarLang);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Translation translation = new Translation(
              rs.getInt("id"),
              rs.getString("original_text"),
              rs.getString("source_lang"),
              rs.getString("target_lang"),
              rs.getString("translated_text")
          );
          translations.add(translation);
        }
      }
    } catch (SQLException e) {
      logger.error("Error fetching translations", e);
    }

    return translations;
  }
}