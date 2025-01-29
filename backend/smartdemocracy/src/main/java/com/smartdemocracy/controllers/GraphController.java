package com.smartdemocracy.controllers;

import com.smartdemocracy.models.Article;
import com.smartdemocracy.repositories.ArticleRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class GraphController {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    private String BASE_URL = "https://app.trustservista.com/api";
    private String API_KEY = "d4f388d353b44266aa075e2c5cd2b48b";
    private String GRAPH_URL = "/rest/v2/graph";
    
    @GetMapping("/graph")
    public ResponseEntity<?> graphApi(@RequestParam("articleUrl") String articleUrl) {
        try {
            String graphRequestBody = """
                {
                    "content": "EMPTY",
                    "contentUri": "%s",
                    "language": "eng"
                }
                """.formatted(articleUrl);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest graphRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + GRAPH_URL))
                .header("Accept", "application/json")
                .header("X-TRUS-API-Key", API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(graphRequestBody))
                .build();

            HttpResponse<String> graphResponse = client.send(graphRequest, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonGraph = objectMapper.readTree(graphResponse.body());

            JsonNode graphNodes = jsonGraph.path("graphNodes");

            if(graphNodes.isArray() && graphNodes.size() > 0) {
                for(JsonNode node : graphNodes) {
                    JsonNode articleGraphNode = node.path("articleGraphNodes");
                    if(articleGraphNode.isArray() && articleGraphNode.size() > 0) {
                        for(JsonNode articleNode : articleGraphNode) {
                            Article newArticle = new Article();
                            newArticle.setArticleId(articleNode.get("id").asText());
                            newArticle.setUrl(articleNode.get("url").asText());
                            newArticle.setTitle(articleNode.get("title").asText());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            newArticle.setPublishTime(LocalDateTime.parse(articleNode.get("publishTime").asText(), formatter));
                            newArticle.setTrustLevel(0.0f);
                            newArticle.setSource(articleNode.get("source").asText());
                            newArticle.setDistance((float)articleNode.get("distance").asDouble());
                            articleRepository.save(newArticle);
                        }
                        return ResponseEntity.ok("All articles saved!");
                    }
                }
            }

            return ResponseEntity.status(404).body("No graph nodes found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}