package com.bw.microapp.model;

import java.util.List;

public class FormatUrlResponse {
  private List<String> urls;

  public FormatUrlResponse(List<String> urls) {
    this.urls = urls;
  }

  public List<String> getUrls() {
    return urls;
  }

  public void setUrls(List<String> urls) {
    this.urls = urls;
  }
}
