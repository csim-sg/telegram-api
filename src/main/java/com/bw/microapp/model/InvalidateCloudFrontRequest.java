package com.bw.microapp.model;

import java.util.List;

public class InvalidateCloudFrontRequest {
  private String distributionId;
  private List<String> paths;
  private String callerReference;

  public String getDistributionId() {
    return distributionId;
  }

  public void setDistributionId(String distributionId) {
    this.distributionId = distributionId;
  }

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }

  public String getCallerReference() {
    return callerReference;
  }

  public void setCallerReference(String callerReference) {
    this.callerReference = callerReference;
  }
}
