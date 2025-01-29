package com.smartdemocracy.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Article")
public class Article {

    @Id
    @Column(name = "ArticleId", nullable = false, length = 50)
    private String articleId;

    @Column(name = "Url", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String url;

    @Column(name = "Title", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String title;

    @Column(name = "PublishTime", nullable = false)
    private LocalDateTime publishTime;

    @Column(name = "TrustLevel")
    private Float trustLevel;

    @Column(name = "Source", nullable = false, length = 50)
    private String source;

    @Column(name = "Distance")
    private Float distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentArticleId")
    private Article parentArticle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChildArticleId")
    private Article childArticle;

    // Getters and Setters

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Float getTrustLevel() {
        return trustLevel;
    }

    public void setTrustLevel(Float trustLevel) {
        this.trustLevel = trustLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Article getParentArticle() {
        return parentArticle;
    }

    public void setParentArticle(Article parentArticle) {
        this.parentArticle = parentArticle;
    }

    public Article getChildArticle() {
        return childArticle;
    }

    public void setChildArticle(Article childArticle) {
        this.childArticle = childArticle;
    }
}