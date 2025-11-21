package com.bw.microapp.model;

import java.util.List;

public class FormatUrlRequest {
  private String baseURL;
  private List<String> linkString;

  public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public List<String> getLinkString() {
    return linkString;
  }

  public void setLinkString(List<String> linkString) {
    this.linkString = linkString;
  }
}
