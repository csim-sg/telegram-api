package com.bw.microapp.service;

import com.bw.microapp.config.TelegramConfig;
import org.springframework.stereotype.Service;
import it.tdlight.client.APIToken;
import it.tdlight.client.AuthenticationData;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.TDLibSettings;
import it.tdlight.common.Init;
import it.tdlight.common.utils.CantLoadLibrary;
import it.tdlight.jni.TdApi;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class TelegramService {

  private final TelegramConfig telegramConfig;
  private SimpleTelegramClient client;

  public TelegramService(TelegramConfig telegramConfig) {
    this.telegramConfig = telegramConfig;
    initializeClient();
  }

  private void initializeClient() {
    try {
      Init.start();
    } catch (CantLoadLibrary e) {
      throw new RuntimeException("Failed to load TDLib", e);
    }

    var settings = TDLibSettings.create(new APIToken(
        telegramConfig.getAppId(),
        telegramConfig.getApiHash()));

    // Configure session path
    var sessionPath = Paths.get("tdlib-session");
    settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
    settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

    // Initialize client
    var authenticationData = AuthenticationData.consoleLogin(); // Simplified for now
    this.client = new SimpleTelegramClient(settings);

    // Start client
    this.client.start(authenticationData);

    // Wait for authorization (simplified)
    try {
      this.client.waitForExit();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public long createGroup(String title, String[] usernames)
      throws ExecutionException, InterruptedException, TimeoutException {
    long[] userIds = new long[usernames.length];

    for (int i = 0; i < usernames.length; i++) {
      String username = usernames[i].startsWith("@") ? usernames[i].substring(1) : usernames[i];
      try {
        // Search for the user/chat to resolve ID
        var searchChat = new TdApi.SearchPublicChat(username);
        var chat = client.send(searchChat).get(10, TimeUnit.SECONDS);
        userIds[i] = chat.id;
      } catch (Exception e) {
        System.err.println("Failed to resolve username: " + username);
        // Handle error or skip
      }
    }

    // Create basic group
    var createChatReq = new TdApi.CreateNewBasicGroupChat();
    createChatReq.title = title;
    createChatReq.userIds = userIds;

    var result = client.send(createChatReq).get(10, TimeUnit.SECONDS);

    if (result instanceof TdApi.Chat) {
      return ((TdApi.Chat) result).id;
    } else {
      throw new RuntimeException("Failed to create group: " + result);
    }
  }

  public void setGroupDescription(long chatId, String description) {
    // Basic groups don't have "about" in the same way supergroups do in some
    // contexts,
    // but let's try to set it if it's a supergroup or upgrade it.
    // For basic groups, we might not be able to set a description easily without
    // upgrading.
    // For this MVP, we'll try to just log it if not supported.
    System.out.println("Setting description for chat " + chatId + ": " + description);
    // Implementation would depend on chat type (BasicGroup vs Supergroup)
  }
}
