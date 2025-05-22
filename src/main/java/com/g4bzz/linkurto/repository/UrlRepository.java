package com.g4bzz.linkurto.repository;

import com.g4bzz.linkurto.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<com.g4bzz.linkurto.model.Url, Long> {
    Optional<com.g4bzz.linkurto.model.Url> findByShortUrl(String shortUrl);

    Optional<com.g4bzz.linkurto.model.Url> findByUrl(String url);

    @Query("FROM Url u WHERE u.expirationDate <= :expirationDate")
    List<Url> findAllWithExpirationDateBefore(
            @Param("expirationDate") LocalDateTime expirationDate);
}
