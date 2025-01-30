package com.smartdemocracy.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SentimentService {
    
    @Value("${trustservista.base-url}")
    private String BASE_URL;
    
    @Value("${trustservista.api-key}")
    private String API_KEY;

    @Value("${trustservista.sentiment-url}")
    private String SENTIMENT_URL;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchSentimentData(String articleUrl) throws Exception {
        String requestBody = 
            """
            {
                "content": "EMPTY",
                "contentUri": "%s",
                "language": "eng"
            }
            """.formatted(articleUrl);

        HttpRequest sentimentRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + SENTIMENT_URL))
            .header("Accept", "application/json")
            .header("X-TRUS-API-Key", API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> sentimentResponse = client.send(sentimentRequest, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(sentimentResponse.body());
    }
}