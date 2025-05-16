package com.example.chatbot_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAIResponse(String userMessage) {
        
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(
                Map.of("role", "user", "content", userMessage)
            )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> message = (Map<String, Object>) ((Map<String, Object>) ((List<?>) response.getBody().get("choices")).get(0)).get("message");
            return message.get("content").toString().trim();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra la pila de error en consola
            return "Se ha excedido la cuota de uso de OpenAI. Por favor, revisa tu plan.";
        }
    }
}
