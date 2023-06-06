package com.avance.linkshortener.linkshortener.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avance.linkshortener.linkshortener.model.Link;
import com.avance.linkshortener.linkshortener.repository.LinkRepository;

@Service
public class LinkService {
	private final LinkRepository linkRepository;
    private final Map<String, LocalDateTime> urlExpiryMap;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
        this.urlExpiryMap = new HashMap<>();
    }

    public String generateShortUrl(String originalUrl) {
        // Validate URL structure
        if (!isValidUrl(originalUrl)) {
            return "Invalid URL.";
        }

        // Check for duplicate URLs
        Link existingLink = linkRepository.findByOriginalUrl(originalUrl);
        if (existingLink != null) {
            return "Short URL already exists for this URL: " + existingLink.getShortUrl();
        }

        String shortUrl = generateUniqueShortUrl();
        Link link = new Link(originalUrl, shortUrl, LocalDateTime.now());
        linkRepository.save(link);

        // Set expiry time for URL
        urlExpiryMap.put(shortUrl, LocalDateTime.now().plusMinutes(5));

        return "http://localhost:8080/" + shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        if (!urlExpiryMap.containsKey(shortUrl)) {
            return "URL doesn't exist.";
        }

        LocalDateTime expiryTime = urlExpiryMap.get(shortUrl);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            urlExpiryMap.remove(shortUrl);
            return "URL has expired.";
        }

        Link link = linkRepository.findByShortUrl(shortUrl);
        if (link != null) {
            return link.getOriginalUrl();
        }

        return "URL doesn't exist.";
    }

    private boolean isValidUrl(String url) {
        String regex = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,3}(\\/\\S*)?$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }

    private String generateUniqueShortUrl() {
        String shortUrl = generateRandomString();
        while (linkRepository.findByShortUrl(shortUrl) != null) {
            shortUrl = generateRandomString();
        }
        return shortUrl;
    }

    private String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
