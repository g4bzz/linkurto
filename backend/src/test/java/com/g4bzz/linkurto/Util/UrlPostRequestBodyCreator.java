package com.g4bzz.linkurto.Util;

import com.g4bzz.linkurto.dto.UrlPostRequestBody;

public class UrlPostRequestBodyCreator {
    public static UrlPostRequestBody createValidGithubUrlPostRequestBody() {
        return UrlPostRequestBody.builder()
                .url("https://github.com/")
                .recaptchaToken("test")
                .build();
    }

    public static UrlPostRequestBody createEmptyUrlPostRequestBody() {
        return UrlPostRequestBody.builder()
                .url("")
                .build();
    }

    public static UrlPostRequestBody createNullUrlPostRequest() {
        return UrlPostRequestBody.builder()
                .url(null)
                .build();
    }

    public static UrlPostRequestBody createInvalidUrlPostRequest() {
        return UrlPostRequestBody.builder()
                .url("htp:/www.github.test")
                .build();
    }
}
