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
public class TrustlevelService {
    
    @Value("${trustservista.base-url}")
    private String BASE_URL;

    @Value("${trustservista.api-key}")
    private String API_KEY;

    @Value("${trustservista.trustlevel-url}")
    private String TRUSTLEVEL_URL;

    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchTrustlevelData(String articleUrl) throws Exception {
        String requestBody = 
            """
            {
                "content": "EMPTY",
                "contentUri": "%s",
                "language": "eng"
            }    
            """.formatted(articleUrl);

        HttpRequest trustlevelRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + TRUSTLEVEL_URL))
            .header("Accept", "application/json")
            .header("X-TRUS-API-Key", API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> trustlevelResponse = client.send(trustlevelRequest, HttpResponse.BodyHandlers.ofString());
        
        return objectMapper.readTree(trustlevelResponse.body());
    }
}