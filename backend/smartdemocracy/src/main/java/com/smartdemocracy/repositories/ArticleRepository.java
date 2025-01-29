package com.smartdemocracy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartdemocracy.models.Article;

public interface ArticleRepository extends JpaRepository<Article, String> {
    
}