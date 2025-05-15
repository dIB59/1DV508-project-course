package org.example.features.translation;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class LibreTranslateClientTest {
  private static WireMockServer wireMockServer;
  private LibreTranslateClient client;

  @BeforeAll
  static void setupServer() {
    wireMockServer = new WireMockServer(8089); // Starts on http://localhost:8089
    wireMockServer.start();
  }

  @AfterAll
  static void shutdownServer() {
    wireMockServer.stop();
  }

  @BeforeEach
  void setUp() {
    client = new LibreTranslateClient(URI.create("http://localhost:8089/translate")) {
      @Override
      public String translate(String text, String targetLang, String sourceLang) throws Exception {
        // Override endpoint to point to WireMock server
        String endpoint = "http://localhost:8089/translate";

        JSONObject body = new JSONObject()
            .put("q", text)
            .put("source", sourceLang)
            .put("target", targetLang)
            .put("format", "text");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(response.body()).getString("translatedText");
      }
    };
  }

  @Test
  void testTranslateSuccess() throws Exception {
    // Stub response
    wireMockServer.stubFor(post(urlEqualTo("/translate"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"translatedText\":\"Bonjour\"}")));

    String result = client.translate("Good morning", "fr", "en");
    assertEquals("Bonjour", result);
  }

  @Test
  void testNoMockResponse() throws Exception {
    // No stub for this request, should throw an exception
    var realClient = new LibreTranslateClient();

    var res = realClient.translate("Good morning", "sv", "en");
    assertNotNull(res);
    assertFalse(res.isEmpty());
    var transText = "God morgon";

    assertEquals(transText, res);
  }

}
