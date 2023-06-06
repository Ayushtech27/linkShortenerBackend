package com.avance.linkshortener.linkshortener.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.avance.linkshortener.linkshortener.service.LinkService;

import jakarta.servlet.http.HttpServletResponse;


@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class LinkController {
	private final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        if (url == null || url.isEmpty()) {
            return ResponseEntity.badRequest().body("URL parameter is missing or empty");
        }

        String shortUrl = linkService.generateShortUrl(url);
        return ResponseEntity.ok("Short URL: " + shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectUrl(@PathVariable("shortUrl") String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = linkService.getOriginalUrl(shortUrl);

        if (originalUrl.equals("URL doesn't exist.")) {
            // Handle case where the URL doesn't exist
            return ResponseEntity.notFound().build();
        } else if (originalUrl.equals("URL has expired.")) {
            // Handle case where the URL has expired
            return ResponseEntity.status(HttpStatus.GONE).body("URL has expired.");
        } else {
            // Redirect to the original URL
            String encodedUrl = response.encodeRedirectURL(originalUrl);
            response.addHeader("Location", encodedUrl);
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).build();
        }
    }
  }