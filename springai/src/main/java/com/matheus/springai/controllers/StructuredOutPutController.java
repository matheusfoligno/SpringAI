package com.matheus.springai.controllers;

import com.matheus.springai.models.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StructuredOutPutController {

    private final ChatClient chatClient;

    public StructuredOutPutController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCities> chat(@RequestParam String message) {
        CountryCities countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(CountryCities.class);

        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-list")
    public ResponseEntity<List<String>> chatList(@RequestParam String message) {
        List<String> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ListOutputConverter());

        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-map")
    public ResponseEntity<Map<String, Object>> chatMap(@RequestParam String message) {
        Map<String, Object> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new MapOutputConverter());

        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-bean-list")
    public ResponseEntity<List<CountryCities>> chatBeanList(@RequestParam String message) {
        List<CountryCities> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>() {});

        return ResponseEntity.ok(countryCities);
    }
}
