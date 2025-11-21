package com.bw.microapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {
  private int appId;
  private String apiHash;
  private String sessionString;

  public int getAppId() {
    return appId;
  }

  public void setAppId(int appId) {
    this.appId = appId;
  }

  public String getApiHash() {
    return apiHash;
  }

  public void setApiHash(String apiHash) {
    this.apiHash = apiHash;
  }

  public String getSessionString() {
    return sessionString;
  }

  public void setSessionString(String sessionString) {
    this.sessionString = sessionString;
  }
}
