package com.matheus.springairag.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final ChatClient webSearchChatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource hrSystemTemplate;

    public RAGController(@Qualifier("chatMemoryClient") ChatClient chatClient,
                         @Qualifier("webSearchRAGChatClient") ChatClient webSearchChatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.webSearchChatClient = webSearchChatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webSearchChat(@RequestHeader("username")
                                                String username, @RequestParam("message") String message) {
        String answer = webSearchChatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }
}
