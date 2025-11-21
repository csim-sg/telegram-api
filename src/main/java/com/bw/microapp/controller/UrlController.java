package com.bw.microapp.controller;

import com.bw.microapp.model.FormatUrlRequest;
import com.bw.microapp.model.FormatUrlResponse;
import com.bw.microapp.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  @PostMapping("/format-url")
  public ResponseEntity<?> formatUrl(@RequestBody FormatUrlRequest request) {
    if (request.getBaseURL() == null || request.getLinkString() == null) {
      return ResponseEntity.badRequest().body("baseURL and linkString are required");
    }

    try {
      List<String> results = request.getLinkString().stream()
          .map(link -> urlService.formatURL(request.getBaseURL(), link))
          .distinct()
          .collect(Collectors.toList());

      return ResponseEntity.ok(new FormatUrlResponse(results));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Failed to format URL");
    }
  }
}
