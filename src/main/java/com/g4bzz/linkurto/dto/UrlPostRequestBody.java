package com.g4bzz.linkurto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlPostRequestBody {
    @Getter
    @NotBlank
    @URL(message = "The URL string must be a valid URL")
    private String url;

}
