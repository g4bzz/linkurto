package com.g4bzz.linkurto.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g4bzz.linkurto.Util.RecaptchaVerifyResponseCreator;
import com.g4bzz.linkurto.exception.RecaptchaValidationException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@RunWith(SpringRunner.class)
@RestClientTest(RecaptchaClient.class)
public class RecaptchaClientTest {
        @Value("${google.recaptcha-v2.url}")
        private String RECAPTCHA_URL;

        @Autowired
        private RecaptchaClient recaptchaClient;

        @Autowired
        private MockRestServiceServer server;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @DisplayName("should return true when recaptcha token is valid")
        public void isRecaptchaValid_shouldReturnTrue_whenRecaptchaTokenIsValid() throws Exception {
                // given
                String validTokenRequestBody = objectMapper
                                .writeValueAsString(RecaptchaVerifyResponseCreator.createValidRecaptchaResponse());

                // when
                this.server.expect(
                                MockRestRequestMatchers.requestTo(RECAPTCHA_URL + "?secret=test-secret&response=valid"))
                                .andRespond(MockRestResponseCreators.withSuccess(validTokenRequestBody,
                                                MediaType.APPLICATION_JSON));

                // then
                boolean isRecaptchaValid = this.recaptchaClient.isRecaptchaValid("valid");
                Assertions.assertThat(isRecaptchaValid).isTrue();
        }

        @Test
        @DisplayName("should throw RecaptchaValidationException when recaptcha token is invalid")
        public void isRecaptchaValid_shouldThrowRecaptchaValidationException_whenRecaptchaTokenIsInvalid()
                        throws Exception {
                // given
                String invalidTokenRequestBody = objectMapper
                                .writeValueAsString(RecaptchaVerifyResponseCreator.createInvalidRecaptchaResponse());

                // when
                this.server.expect(MockRestRequestMatchers
                                .requestTo(RECAPTCHA_URL + "?secret=test-secret&response=invalid"))
                                .andRespond(MockRestResponseCreators.withSuccess(invalidTokenRequestBody,
                                                MediaType.APPLICATION_JSON));

                // then
                Assertions.assertThatThrownBy(() -> this.recaptchaClient.isRecaptchaValid("invalid"))
                                .isInstanceOf(RecaptchaValidationException.class);
        }
}
