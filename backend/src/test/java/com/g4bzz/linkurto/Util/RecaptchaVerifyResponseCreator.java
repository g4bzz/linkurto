package com.g4bzz.linkurto.Util;

import com.g4bzz.linkurto.client.dto.RecaptchaVerifyResponse;

public class RecaptchaVerifyResponseCreator {
    public static RecaptchaVerifyResponse createValidRecaptchaResponse() {
        return RecaptchaVerifyResponse.builder()
                .success(true)
                .hostname("localhost")
                .build();
    }
    public static RecaptchaVerifyResponse createInvalidRecaptchaResponse() {
        return RecaptchaVerifyResponse.builder()
                .success(false)
                .build();
    }
}
