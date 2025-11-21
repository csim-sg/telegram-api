package com.bw.microapp.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.CreateInvalidationRequest;
import software.amazon.awssdk.services.cloudfront.model.CreateInvalidationResponse;
import software.amazon.awssdk.services.cloudfront.model.InvalidationBatch;
import software.amazon.awssdk.services.cloudfront.model.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class CloudFrontService {

  private static final int MAX_PATHS_PER_INVALIDATION = 30;

  private final CloudFrontClient cloudFrontClient;

  public CloudFrontService() {
    // Region defaults to us-east-1 if not specified, similar to original code
    String region = System.getenv("AWS_REGION");
    if (region == null || region.isEmpty()) {
      region = "us-east-1";
    }
    this.cloudFrontClient = CloudFrontClient.builder()
        .region(Region.of(region))
        .build();
  }

  public List<Map<String, Object>> invalidatePaths(String distributionId, List<String> paths, String callerReference) {
    List<String> invalidationPaths = paths.stream()
        .map(path -> path.startsWith("/") ? path : "/" + path)
        .toList();

    List<List<String>> batches = new ArrayList<>();
    for (int i = 0; i < invalidationPaths.size(); i += MAX_PATHS_PER_INVALIDATION) {
      batches.add(invalidationPaths.subList(i, Math.min(i + MAX_PATHS_PER_INVALIDATION, invalidationPaths.size())));
    }

    String baseReference = callerReference != null ? callerReference : String.valueOf(System.currentTimeMillis());
    List<Map<String, Object>> invalidations = new ArrayList<>();

    for (int i = 0; i < batches.size(); i++) {
      List<String> batchPaths = batches.get(i);
      String batchReference = batches.size() == 1 ? baseReference : baseReference + "-" + i;

      try {
        CreateInvalidationRequest request = CreateInvalidationRequest.builder()
            .distributionId(distributionId)
            .invalidationBatch(InvalidationBatch.builder()
                .callerReference(batchReference)
                .paths(Paths.builder()
                    .quantity(batchPaths.size())
                    .items(batchPaths)
                    .build())
                .build())
            .build();

        CreateInvalidationResponse response = cloudFrontClient.createInvalidation(request);

        Map<String, Object> result = new HashMap<>();
        result.put("invalidationId", response.invalidation().id());
        result.put("status", response.invalidation().status());
        result.put("submittedAt", response.invalidation().createTime().toString());
        invalidations.add(result);

      } catch (Exception e) {
        throw new RuntimeException("Failed to create invalidation: " + e.getMessage(), e);
      }
    }

    return invalidations;
  }
}
