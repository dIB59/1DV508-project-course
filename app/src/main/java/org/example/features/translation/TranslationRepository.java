package org.example.features.translation;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

public class TranslationRepository {
  private final Connection conn;

  public TranslationRepository(Connection conn){
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

  public Optional<String> find(String text, String sourceLang, String targetLang) throws SQLException {
    String textHash = hashText(text);
    PreparedStatement stmt = conn.prepareStatement("""
            SELECT translated_text FROM Translations
            WHERE original_text_hash = ? AND source_lang = ? AND target_lang = ?
        """);
    stmt.setString(1, textHash);
    stmt.setString(2, sourceLang);
    stmt.setString(3, targetLang);
    ResultSet rs = stmt.executeQuery();
    return rs.next() ? Optional.of(rs.getString("translated_text")) : Optional.empty();
  }

  public void save(String text, String sourceLang, String targetLang, String translatedText) throws SQLException {
    String textHash = hashText(text);

    if (find(text, sourceLang, targetLang).isPresent()) {
      PreparedStatement stmt = conn.prepareStatement("""
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

    PreparedStatement stmt = conn.prepareStatement("""
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
}
