package com.g4bzz.linkurto.dto;

import com.g4bzz.linkurto.constraints.ValidUrl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlPostRequestBody {
    @NotNull(message = "The URL cannot be null")
    @ValidUrl(message = "The URL must be valid")
    private String url;

}
