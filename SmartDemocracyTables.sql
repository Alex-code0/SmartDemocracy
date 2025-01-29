CREATE DATABASE SmartDemocracy
GO
USE SmartDemocracy
GO

CREATE TABLE Article (
    ArticleId VARCHAR(50) PRIMARY KEY,
    Url NVARCHAR(MAX) NOT NULL,
    Title NVARCHAR(MAX) NOT NULL,
    PublishTime DATETIME NOT NULL,
    TrustLevel FLOAT,
    Source VARCHAR(50) NOT NULL,
	Distance FLOAT,
    ParentArticleId VARCHAR(50),
	ChildArticleId VARCHAR(50),
    FOREIGN KEY (ParentArticleId) REFERENCES Article(ArticleId),
	FOREIGN KEY (ChildArticleId) REFERENCES Article(ArticleId)
)