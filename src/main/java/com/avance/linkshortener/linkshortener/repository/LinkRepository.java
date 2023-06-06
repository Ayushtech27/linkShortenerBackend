package com.avance.linkshortener.linkshortener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avance.linkshortener.linkshortener.model.Link;

public interface LinkRepository extends MongoRepository<Link, String> {
	Link findByShortUrl(String shortUrl);
	Link findByOriginalUrl(String originalUrl);
}
