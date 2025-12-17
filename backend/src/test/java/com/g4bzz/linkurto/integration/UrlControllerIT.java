package com.g4bzz.linkurto.integration;

import com.g4bzz.linkurto.Util.UrlCreator;
import com.g4bzz.linkurto.Util.UrlPostRequestBodyCreator;
import com.g4bzz.linkurto.client.RecaptchaClient;
import com.g4bzz.linkurto.dto.UrlPostRequestBody;
import com.g4bzz.linkurto.dto.UrlPostResponseBody;
import com.g4bzz.linkurto.mapper.UrlMapper;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.repository.UrlRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UrlControllerIT {
        @Autowired
        private TestRestTemplate testRestTemplate;

        @Autowired
        private UrlRepository urlRepository;

        @LocalServerPort
        private int port;

        @MockitoBean
        private RecaptchaClient recaptchaClient;

        @BeforeEach
        void setUp() {
        }

        @Test
        @DisplayName("shorten should return a valid shortened URL when successful.")
        void shorten_shouldReturnValidShortenedUrl_whenSuccessful() {
                UrlPostResponseBody expectedResponse = UrlMapper.INSTANCE
                                .toUrlPostResponseBody(UrlCreator.createValidGithubUrl());
                UrlPostRequestBody requestBody = UrlPostRequestBodyCreator.createValidGithubUrlPostRequestBody();

                Mockito.when(recaptchaClient.isRecaptchaValid(ArgumentMatchers.anyString())).thenReturn(true);

                ResponseEntity<UrlPostResponseBody> responseEntity = testRestTemplate.postForEntity(
                                "/shorten", requestBody, UrlPostResponseBody.class);

                // Assertions
                Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Assertions.assertThat(responseEntity.getBody()).isNotNull();
                Assertions.assertThat(responseEntity.getBody().getShortUrl()).isNotBlank()
                                .isEqualTo(expectedResponse.getShortUrl());
        }

        @Test
        @DisplayName("shorten should return a 400 Bad Request when the url in request body is empty.")
        void shorten_shouldReturnBadRequestError_whenUrlInRequestBodyIsEmpty() {
                UrlPostRequestBody requestBody = UrlPostRequestBodyCreator.createEmptyUrlPostRequestBody();

                Mockito.when(recaptchaClient.isRecaptchaValid(ArgumentMatchers.anyString())).thenReturn(true);

                ResponseEntity<Object> responseEntity = testRestTemplate.postForEntity(
                                "/shorten", requestBody, Object.class);

                // Assertions
                Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                Assertions.assertThat(responseEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("shorten should return a 400 Bad Request when the url in request body is null.")
        void shorten_shouldReturnBadRequestError_whenUrlInRequestBodyIsNull() {
                UrlPostRequestBody requestBody = UrlPostRequestBodyCreator.createNullUrlPostRequest();

                ResponseEntity<Object> responseEntity = testRestTemplate.postForEntity(
                                "/shorten", requestBody, Object.class);

                // Assertions
                Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                Assertions.assertThat(responseEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("shorten should return a 400 Bad Request when the url in request body is invalid.")
        void shorten_shouldReturnBadRequestError_whenUrlInRequestBodyIsInvalid() {
                UrlPostRequestBody requestBody = UrlPostRequestBodyCreator.createInvalidUrlPostRequest();

                ResponseEntity<Object> responseEntity = testRestTemplate.postForEntity(
                                "/shorten", requestBody, Object.class);
                // Assertions
                Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                Assertions.assertThat(responseEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("resolve should redirect to the original url when successful.")
        void resolve_shouldRedirect_WhenSuccessful() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();
                urlRepository.save(Url.builder()
                                .shortUrl(expectedUrl.getShortUrl())
                                .url(expectedUrl.getUrl())
                                .expirationDate(expectedUrl.getExpirationDate())
                                .build());

                ResponseEntity<Object> redirectedPage = testRestTemplate.getForEntity("/{shortUrl}", Object.class,
                                expectedUrl.getShortUrl());

                // Assertions
                Assertions.assertThat(redirectedPage).isNotNull();
                Assertions.assertThat(redirectedPage.getStatusCode()).isEqualTo(HttpStatus.OK);
                // Test if the redirected url is from gitHub.com
                Assertions.assertThat(redirectedPage.getHeaders().get("server")).isNotNull()
                                .containsExactlyInAnyOrderElementsOf(List.of("github.com"));
        }

        @Test
        @DisplayName("resolve should throw a 404 error when the short URL does not exist.")
        void resolve_shouldThrowNotFoundError_WhenShortUrlDoesNotExist() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();

                ResponseEntity<Url> url = testRestTemplate.getForEntity("/{shortUrl}", Url.class,
                                expectedUrl.getShortUrl());

                // Assertions
                Assertions.assertThat(url.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
}
