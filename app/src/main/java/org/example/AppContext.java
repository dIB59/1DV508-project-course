package org.example;

import java.util.Objects;
import org.example.features.translation.Language;

public class AppContext {
  private static AppContext instance;

  private Language language;
  private final String LIGHT_THEME = Objects.requireNonNull(
      getClass().getResource("/styles/theme-light.css")).toExternalForm();
  private final String DARK_THEME = Objects.requireNonNull(
      getClass().getResource("/styles/theme-dark.css")).toExternalForm();
  public final String BASE_THEME = Objects.requireNonNull(getClass()
      .getResource("/styles/styles.css")).toExternalForm();


  // 🔹 Added: Track dark mode status
  private boolean darkMode = false;

  private AppContext() {
    this.language = Language.ENGLISH; // Default language
  }

  public static AppContext getInstance() {
    if (instance == null) {
      instance = new AppContext();
    }
    return instance;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }
  
  public boolean isDarkMode() {
    return darkMode;
  }

  public void setDarkMode(boolean darkMode) {
    this.darkMode = darkMode;
  }

  public String getCurrentTheme(){
    if (isDarkMode()){
      return DARK_THEME;
    }
    return LIGHT_THEME;
  }
}
