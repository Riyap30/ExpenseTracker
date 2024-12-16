package com.example.demo.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public String getSuggestedCategory(String description) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        RestTemplate restTemplate = new RestTemplate();

        // Prepare the request payload
        Map<String, Object> payload = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant for categorizing expenses."),
                        Map.of("role", "user", "content", "Suggest a category for this expense: " + description + "The user will provide an expense description and amount. The user has provided a list of existing categories. Your task is to determine the most appropriate category from the provided list for the given expense. If no suitable category exists, you will suggest a clear, concise, and logical new category name that best fits the expense description. Trim response down to only the answer")
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);
            Map<String, Object> choices = (Map<String, Object>) ((List<Object>) response.getBody().get("choices")).get(0);
            Map<String, String> message = (Map<String, String>) choices.get("message");
            return message.get("content").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Uncategorized"; // Fallback category
        }
    }
}
