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

            String iptc = iptcJson.get("iptc").asText();
            String trustLevel = trustLevelJson.get("trustLevel").asText();

            Map<String, String> articleInfo = new HashMap<>();
            articleInfo.put("trustLevel", trustLevel);

            return ResponseEntity.ok(articleInfo);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error fetching article information: " + e.getMessage());
        }
    }
}