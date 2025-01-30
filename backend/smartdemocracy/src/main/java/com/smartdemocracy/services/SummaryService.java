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
public class SummaryService {
    
    @Value("${trustservista.base-url}")
    private String BASE_URL;

    @Value("${trustservista.api-key}")
    private String API_KEY;

    @Value("${trustservista.summary-url}")
    private String SUMMARY_URL;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchSummaryData(String articleUrl) throws Exception {
        String requestBody = 
            """
            {
                "content": "EMPTY",
                "contentUri": "%s",
                "language": "eng",
                "size": 10
            }
            """.formatted(articleUrl);

        HttpRequest summaryRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + SUMMARY_URL))
            .header("Accept", "application/json")
            .header("X-TRUS-API-Key", API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> summaryResponse = client.send(summaryRequest, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(summaryResponse.body());
    }
}