package org.example.features.translation;


import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class LibreTranslateClient {
  private static final URI ENDPOINT = URI.create("http://localhost:8081/translate");
  private final HttpClient client = HttpClient.newHttpClient();
  private final URI endpoint;

  public LibreTranslateClient(URI endpoint) {
    this.endpoint = endpoint;
  }

  public LibreTranslateClient() {
    this(ENDPOINT);
  }


  public String translate(String text, String targetLang, String sourceLang) throws Exception {

    JSONObject body = new JSONObject()
        .put("q", text)
        .put("source", sourceLang)
        .put("target", targetLang)
        .put("format", "text");

    HttpRequest request = HttpRequest.newBuilder()
        .uri(endpoint)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response);

    return new JSONObject(response.body()).getString("translatedText");
  }
}
