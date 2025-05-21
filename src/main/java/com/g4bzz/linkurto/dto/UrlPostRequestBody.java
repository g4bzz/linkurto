package com.g4bzz.linkurto.dto;

import com.g4bzz.linkurto.constraints.ValidUrl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlPostRequestBody {
    @Getter
    @NotNull(message = "The URL cannot be null")
    @ValidUrl(message = "The URL must be valid")
    private String url;

}
