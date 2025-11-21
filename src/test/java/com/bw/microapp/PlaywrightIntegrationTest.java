package com.bw.microapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaywrightIntegrationTest {

  @LocalServerPort
  int port; // Random port assigned by Spring Boot

  private static Playwright playwright;
  private static APIRequestContext api;

  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeAll
  static void globalSetUp() {
    // Initialise Playwright once for the whole test suite
    playwright = Playwright.create();
    APIRequest request = playwright.request();
    api = request.newContext(); // default context (no auth needed for our API)
  }

  @AfterAll
  static void globalTearDown() {
    api.dispose();
    playwright.close();
  }

  private String baseUrl() {
    return "http://localhost:" + port;
  }

  @Test
  void formatUrlEndpointWorks() throws Exception {
    String json = """
        {
          \"url\": \"example.com\",
          \"baseUrl\": \"https://example.com\"
        }
        """;

    APIResponse response = api.post(
        baseUrl() + "/format-url",
        new APIRequest.Options()
            .setData(json)
            .setHeader("Content-Type", "application/json"));

    assertEquals(200, response.status(), "HTTP status should be 200");

    JsonNode body = mapper.readTree(response.text());
    assertEquals("https://example.com", body.get("url").asText(),
        "The formatted URL should be returned");
  }

  @Test
  void invalidateCloudFrontEndpointWorks() throws Exception {
    String json = """
        {
          \"paths\": [\"/index.html\", \"/static/*\"]
        }
        """;

    APIResponse response = api.post(
        baseUrl() + "/invalidate-cloudfront",
        new APIRequest.Options()
            .setData(json)
            .setHeader("Content-Type", "application/json"));

    assertEquals(200, response.status(), "HTTP status should be 200");

    JsonNode body = mapper.readTree(response.text());
    assertTrue(body.get("status").asText().contains("Invalidation"),
        "Response should contain a status message");
  }

  @Test
  void createGroupEndpointWorks() throws Exception {
    String json = """
        {
          \"title\": \"Test Group\",
          \"usernames\": [\"alice\", \"bob\"]
        }
        """;

    APIResponse response = api.post(
        baseUrl() + "/create-group",
        new APIRequest.Options()
            .setData(json)
            .setHeader("Content-Type", "application/json"));

    assertEquals(200, response.status(), "HTTP status should be 200");

    JsonNode body = mapper.readTree(response.text());
    assertEquals("Test Group", body.get("title").asText(),
        "Created group title should be echoed back");
  }
}
