package com.bw.microapp.model;

public class CreateGroupRequest {
  private String title;
  private String botUsername;
  private String username;
  private String agentUsername;
  private String context;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBotUsername() {
    return botUsername;
  }

  public void setBotUsername(String botUsername) {
    this.botUsername = botUsername;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAgentUsername() {
    return agentUsername;
  }

  public void setAgentUsername(String agentUsername) {
    this.agentUsername = agentUsername;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }
}
