package org.example.minishop.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ollama")
public class OllamaController {

    private ChatClient chatClient;

    @Autowired
    public OllamaController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }


    @GetMapping("/product-description/{productName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getProductDescription(@PathVariable String productName) {
         String response = chatClient.prompt()
                .user("write a 2 line description for this (only write the description and nothing more): " + productName)
                .call()
                .content();

         Map<String, String> map = new HashMap<>();
         map.put("description", response);
         return ResponseEntity.ok(map);
    }
}
