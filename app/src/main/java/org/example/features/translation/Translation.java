package org.example.features.translation;

import org.example.database.Identifiable;

public class Translation implements Identifiable<Integer> {
  private Integer id;
  private String orignalText;
  private String sourceLang;
  private String targetLang;
  private String translatedText;

  public Translation(
      String orignalText, String sourceLang, String targetLang, String translatedText) {
    this.id = 0;
    this.orignalText = orignalText;
    this.sourceLang = sourceLang;
    this.targetLang = targetLang;
    this.translatedText = translatedText;
  }

  public Translation(
      Integer id, String orignalText, String sourceLang, String targetLang, String translatedText) {
    this.id = id;
    this.orignalText = orignalText;
    this.sourceLang = sourceLang;
    this.targetLang = targetLang;
    this.translatedText = translatedText;
  }

  @Override
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getOrignalText() {
    return orignalText;
  }

  public void setOrignalText(String orignalText) {
    this.orignalText = orignalText;
  }

  public String getSourceLang() {
    return sourceLang;
  }

  public void setSourceLang(String sourceLang) {
    this.sourceLang = sourceLang;
  }

  public String getTargetLang() {
    return targetLang;
  }

  public void setTargetLang(String targetLang) {
    this.targetLang = targetLang;
  }

  public String getTranslatedText() {
    return translatedText;
  }

  public void setTranslatedText(String translatedText) {
    this.translatedText = translatedText;
  }
}
