package com.bw.microapp.controller;

import com.bw.microapp.model.InvalidateCloudFrontRequest;
import com.bw.microapp.service.CloudFrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CloudFrontController {

  private final CloudFrontService cloudFrontService;

  public CloudFrontController(CloudFrontService cloudFrontService) {
    this.cloudFrontService = cloudFrontService;
  }

  @PostMapping("/invalidate-cloudfront")
  public ResponseEntity<?> invalidateCloudFront(@RequestBody InvalidateCloudFrontRequest request) {
    if (request.getDistributionId() == null) {
      return ResponseEntity.badRequest().body(Map.of("error", "distributionId is required"));
    }

    if (request.getPaths() == null || request.getPaths().isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "paths must be a non-empty array"));
    }

    try {
      List<Map<String, Object>> invalidations = cloudFrontService.invalidatePaths(
          request.getDistributionId(),
          request.getPaths(),
          request.getCallerReference());
      return ResponseEntity.status(202).body(Map.of("invalidations", invalidations));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of(
          "error", e.getMessage(),
          "partialInvalidations", List.of() // Simplified for now, could be improved to track partial success
      ));
    }
  }
}
