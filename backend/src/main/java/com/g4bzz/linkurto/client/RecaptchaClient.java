package com.g4bzz.linkurto.client;

import com.g4bzz.linkurto.client.dto.RecaptchaVerifyResponse;
import com.g4bzz.linkurto.exception.RecaptchaValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RecaptchaClient {
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String RECAPTCHA_URL;

    public RecaptchaClient(
            RestClient.Builder builder,
            @Value("${google.recaptcha-v2.secret-key}") String SECRET_KEY,
            @Value("${google.recaptcha-v2.url}") String RECAPTCHA_URL) {
        this.RECAPTCHA_URL = RECAPTCHA_URL;
        this.SECRET_KEY = SECRET_KEY;
        this.restClient = builder.
                baseUrl(RECAPTCHA_URL)
                .build();
    }

    public boolean isRecaptchaValid(String token){
        String url = RECAPTCHA_URL+"?secret={secret}&response={response}";
        Map<String,String> params = Map.of("secret",SECRET_KEY,"response",token);
        ResponseEntity<RecaptchaVerifyResponse> response = restClient.post()
                .uri(url, params)
                .retrieve()
                .toEntity(RecaptchaVerifyResponse.class);

        if(response.getBody() == null || !response.getStatusCode().is2xxSuccessful() || !response.getBody().isSuccess()){
           throw new RecaptchaValidationException("Recaptcha Validation Error", response.getBody().getErrorCodes());
        }
        return response.getBody().isSuccess();
    }
}
