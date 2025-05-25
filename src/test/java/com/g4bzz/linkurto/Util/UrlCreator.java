package com.g4bzz.linkurto.Util;

import com.g4bzz.linkurto.model.Url;

import java.time.LocalDateTime;

public class UrlCreator {
    public static Url createValidGithubUrl(){
       return Url.builder()
               .url("https://github.com/")
               .shortUrl("008ec44")
               .expirationDate(LocalDateTime.now().plusDays(30))
               .id(1)
               .build();
    }

    public static Url createExpiredGithubUrl(){
        return Url.builder()
                .url("https://github.com/")
                .shortUrl("008ec44")
                .expirationDate(LocalDateTime.now().minusDays(10))
                .id(1)
                .build();
    }

    public static Url createEmptyUrl(){
        return Url.builder()
                .build();
    }
}
