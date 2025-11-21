package com.bw.microapp.controller;

import com.bw.microapp.model.CreateGroupRequest;
import com.bw.microapp.service.TelegramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GroupController {

  private final TelegramService telegramService;

  public GroupController(TelegramService telegramService) {
    this.telegramService = telegramService;
  }

  @PostMapping("/create-group")
  public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
    try {
      String[] users = new String[] {
          request.getBotUsername(),
          request.getUsername(),
          request.getAgentUsername()
      };

      long chatId = telegramService.createGroup(request.getTitle(), users);

      if (request.getContext() != null && !request.getContext().isEmpty()) {
        telegramService.setGroupDescription(chatId, request.getContext());
      }

      return ResponseEntity.ok(Map.of("chatID", String.valueOf(chatId)));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create group: " + e.getMessage()));
    }
  }
}
