package com.g4bzz.linkurto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlPostResponseBody {
    private String url;
    private String shortUrl;
    private LocalDateTime expirationDate;

}
