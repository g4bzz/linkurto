package com.g4bzz.linkurto.service;

import com.g4bzz.linkurto.client.RecaptchaClient;
import com.g4bzz.linkurto.core.engines.Engine;
import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.core.factory.EngineFactory;
import com.g4bzz.linkurto.exception.RecaptchaValidationException;
import com.g4bzz.linkurto.exception.enums.RecaptchaErrorEnum;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.repository.UrlRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final EngineFactory engineFactory;
    private final RecaptchaClient recaptchaClient;

    public UrlService(UrlRepository urlRepository, EngineFactory engineFactory, RecaptchaClient recaptchaClient) {
        this.urlRepository = urlRepository;
        this.engineFactory = engineFactory;
        this.recaptchaClient = recaptchaClient;
    }

    public Url shortenUrl(String longUrl, String recaptchaToken, EngineType engineType) {
        if (longUrl == null || longUrl.isEmpty()) {
            throw new IllegalArgumentException("Long URL cannot be null or empty");
        }

        if (recaptchaToken == null || recaptchaToken.isEmpty()) {
            throw new RecaptchaValidationException("Recaptcha Validation Error", List.of(RecaptchaErrorEnum.MISSING_INPUT_RESPONSE.toString()));
        }

        recaptchaClient.isRecaptchaValid(recaptchaToken);

        Engine engine = engineFactory.getEngine(engineType);
        Optional<Url> searchedUrl = urlRepository.findByUrl(longUrl);
        if(searchedUrl.isPresent()) {
            return searchedUrl.get();
        }

        String shortUrl = engine.getUniqueKey(longUrl);

        while (urlRepository.findByShortUrl(shortUrl).isPresent() ) {
            shortUrl = engine.getUniqueKey(longUrl + UUID.randomUUID().toString());
        }

        Url url = Url.builder()
                .url(longUrl)
                .shortUrl(shortUrl)
                .expirationDate(LocalDateTime.now().plusDays(engine.getDaysToExpire()))
                .build();

        return urlRepository.save(url);
    }

    public Url resolve(String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            throw new IllegalArgumentException("Short Url cannot be empty or null");
        }
        Optional<Url> searchedUrl = urlRepository.findByShortUrl(shortUrl);
        return searchedUrl.orElseGet(Url::new);
    }
}
