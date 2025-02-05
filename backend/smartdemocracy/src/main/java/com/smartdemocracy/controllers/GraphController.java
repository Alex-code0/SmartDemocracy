package com.smartdemocracy.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartdemocracy.models.Article;
import com.smartdemocracy.repositories.ArticleRepository;

import com.smartdemocracy.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class GraphController {
    
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private IptcService iptcService;

    @Autowired
    private ClickbaitService clickbaitService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private P0Service p0Service;

    @Autowired
    private TrustlevelService trustlevelService;

    @Autowired
    private SentimentService sentimentService;
    
    @GetMapping("/articleInfo")
public ResponseEntity<?> fetchArticleInfo(@RequestParam("articleUrl") String articleUrl) {
    try {
        JsonNode iptcJson = iptcService.fetchIptcData(articleUrl);
        JsonNode clickbaitJson = clickbaitService.fetchClickbaitData(articleUrl);
        JsonNode metadataJson = metadataService.fetchMetadata(articleUrl);
        JsonNode p0Json = p0Service.fetchP0Data(articleUrl);
        JsonNode sentimentJson = sentimentService.fetchSentimentData(articleUrl);
        JsonNode trustLevelJson = trustlevelService.fetchTrustlevelData(articleUrl);

        double highestScore = 0.0;
        JsonNode classifyNode = iptcJson.path("classify");
        if (!classifyNode.isMissingNode() && classifyNode.has("classes")) {
            JsonNode classes = classifyNode.get("classes");
            for (JsonNode child : classes) {
                double score = child.get("score").asDouble();
                if (score > highestScore) {
                    highestScore = score;
                }
            }
        }

        String clickbait = clickbaitJson.path("clickbaitScore").asText();
        String trustLevel = trustLevelJson.path("trustLevel").asText();
        String sentiment = sentimentJson.path("confidence").asText();

        Map<String, String> articleInfo = new HashMap<>();
        articleInfo.put("iptc", String.valueOf(highestScore));
        articleInfo.put("clickbait", clickbait);
        articleInfo.put("sentiment", sentiment);
        articleInfo.put("trustLevel", trustLevel);

        return ResponseEntity.ok(articleInfo);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error fetching article information: " + e.getMessage());
        }
    }
}