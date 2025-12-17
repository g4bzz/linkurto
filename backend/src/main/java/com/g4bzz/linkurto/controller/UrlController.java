package com.g4bzz.linkurto.controller;

import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.dto.UrlPostRequestBody;
import com.g4bzz.linkurto.dto.UrlPostResponseBody;
import com.g4bzz.linkurto.mapper.UrlMapper;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.net.URI;
import java.text.MessageFormat;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/")
public class UrlController implements IUrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("shorten")
    public ResponseEntity<UrlPostResponseBody> shorten(@RequestBody @Valid UrlPostRequestBody urlPostRequestBody,
            @RequestParam(defaultValue = "HASHING") EngineType engineType) {
        Url shortenedUrl = urlService.shortenUrl(urlPostRequestBody.getUrl(), urlPostRequestBody.getRecaptchaToken(),
                engineType);
        UrlPostResponseBody urlPostResponseBody = UrlMapper.INSTANCE.toUrlPostResponseBody(shortenedUrl);
        return new ResponseEntity<>(urlPostResponseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> resolve(@PathVariable String shortUrl) throws NoResourceFoundException {
        Url url = urlService.resolve(shortUrl);
        if (url == null || url.getId() == 0) {
            throw new NoResourceFoundException(HttpMethod.GET, MessageFormat.format("/{0}", shortUrl));
        } else {
            // 301 Moved Permanently
            return ResponseEntity.status(301).location(URI.create(url.getUrl())).build();
        }
    }

    @GetMapping("/exists/{shortUrl}")
    public ResponseEntity<UrlPostResponseBody> shortUrlExists(@PathVariable String shortUrl)
            throws NoResourceFoundException {
        Url url = urlService.resolve(shortUrl);
        if (url == null || url.getId() == 0) {
            throw new NoResourceFoundException(HttpMethod.GET, MessageFormat.format("/{0}", shortUrl));
        } else {
            UrlPostResponseBody urlPostResponseBody = UrlMapper.INSTANCE.toUrlPostResponseBody(url);
            return new ResponseEntity<>(urlPostResponseBody, HttpStatus.OK);
        }
    }
}
