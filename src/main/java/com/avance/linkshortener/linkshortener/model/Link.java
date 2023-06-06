package com.avance.linkshortener.linkshortener.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "links")
public class Link {
	@Id
	private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationTime;

    public Link(String originalUrl, String shortUrl, LocalDateTime creationTime) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.creationTime = creationTime;
    }

    // Getters and setters
    private String id;
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

    
}
