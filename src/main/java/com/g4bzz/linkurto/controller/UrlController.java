package com.g4bzz.linkurto.controller;

import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.dto.UrlPostRequestBody;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    //TODO: add validation for the request body
    @PostMapping("shorten")
    public ResponseEntity<Url> shorten(@RequestBody UrlPostRequestBody urlPostRequestBody, @RequestParam(required = true, defaultValue = "HASHING") EngineType engineType) {
        return ResponseEntity.ok(urlService.shortenUrl(urlPostRequestBody.getUrl(), engineType));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Url> resolve(@PathVariable String shortUrl){
        Url url = urlService.resolve(shortUrl);
        if (url == null || url.getUrl().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        //301 Moved Permanently
        return ResponseEntity.status(301).location(URI.create(url.getUrl())).build();
    }
}
