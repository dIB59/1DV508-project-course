package org.example.features.translation;


import java.util.Optional;

public class TranslationService {
  private final TranslationRepository repo;
  private final LibreTranslateClient client;

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
}
