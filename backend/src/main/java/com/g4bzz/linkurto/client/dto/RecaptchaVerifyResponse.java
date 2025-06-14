package com.g4bzz.linkurto.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecaptchaVerifyResponse {
    private boolean success;
    @JsonProperty("challenge_ts")
    private LocalDate challengeTs;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
}
