package com.g4bzz.linkurto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<com.g4bzz.linkurto.model.Url, Long> {
    Optional<com.g4bzz.linkurto.model.Url> findByShortUrl(String shortUrl);

    Optional<com.g4bzz.linkurto.model.Url> findByLongUrl(String longUrl);
}
