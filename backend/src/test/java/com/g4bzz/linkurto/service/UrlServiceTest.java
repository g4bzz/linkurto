package com.g4bzz.linkurto.service;

import com.g4bzz.linkurto.Util.UrlCreator;
import com.g4bzz.linkurto.Util.UrlPostRequestBodyCreator;
import com.g4bzz.linkurto.client.RecaptchaClient;
import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.core.engines.HashingEngine;
import com.g4bzz.linkurto.core.factory.EngineFactory;
import com.g4bzz.linkurto.exception.RecaptchaValidationException;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.repository.UrlRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for Url service")
public class UrlServiceTest {
        // injectMocks for the class to be tested
        @InjectMocks
        private UrlService urlService;

        // Mock for the classes used by the tested class
        @Mock
        private UrlRepository urlRepositoryMock;
        @Mock
        private EngineFactory engineFactoryMock;
        @Mock
        private RecaptchaClient recaptchaClientMock;

        @BeforeEach
        public void setUp() {
                BDDMockito.when(recaptchaClientMock.isRecaptchaValid(ArgumentMatchers.anyString()))
                                .thenReturn(true);

                BDDMockito.when(recaptchaClientMock.isRecaptchaValid(ArgumentMatchers.eq("invalid")))
                                .thenThrow(RecaptchaValidationException.class);

                BDDMockito.when(engineFactoryMock.getEngine(ArgumentMatchers.eq(EngineType.HASHING)))
                                .thenReturn(new HashingEngine());

                BDDMockito.when(urlRepositoryMock.findByUrl(ArgumentMatchers.anyString()))
                                .thenReturn(Optional.empty());

                BDDMockito.when(urlRepositoryMock.findByShortUrl(ArgumentMatchers.anyString()))
                                .thenReturn(Optional.empty());

                BDDMockito.when(urlRepositoryMock.save(ArgumentMatchers.any(Url.class)))
                                .thenAnswer(invocation -> {
                                        return invocation.getArgument(0);
                                });

        }

        @Test
        @DisplayName("shortenUrl should return a valid Url created by hashing engine when successful")
        void shortenUrl_shouldReturnValidUrlByHashingEngine_WhenSuccessful() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();
                Url urlToTest = urlService.shortenUrl(
                                UrlPostRequestBodyCreator.createValidGithubUrlPostRequestBody().getUrl(), "test",
                                EngineType.HASHING);

                // Assertions
                Assertions.assertThat(urlToTest).isNotNull();
                Assertions.assertThat(urlToTest).isNotEqualTo(UrlCreator.createEmptyUrl());
                Assertions.assertThat(urlToTest.getShortUrl()).isEqualTo(expectedUrl.getShortUrl());
                Assertions.assertThat(urlToTest.getExpirationDate()).isAfter(LocalDateTime.now());
        }

        @Test
        @DisplayName("shortenUrl should return a valid Url created by hashing engine when url already exists")
        void shortenUrl_shouldReturnUrlCreatedByHashingEngine_WhenUrlAlreadyExists() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();

                BDDMockito.when(urlRepositoryMock.findByUrl(ArgumentMatchers.eq(
                                UrlPostRequestBodyCreator.createValidGithubUrlPostRequestBody().getUrl())))
                                .thenReturn(Optional.of(expectedUrl));

                Url urlToTest = urlService.shortenUrl(expectedUrl.getUrl(), "test", EngineType.HASHING);

                // Assertions
                Assertions.assertThat(urlToTest).isNotNull();
                Assertions.assertThat(urlToTest.getShortUrl()).isEqualTo(expectedUrl.getShortUrl());
                Assertions.assertThat(urlToTest.getUrl()).isEqualTo(expectedUrl.getUrl());
                Assertions.assertThat(urlToTest.getExpirationDate()).isEqualTo(expectedUrl.getExpirationDate());
        }

        @Test
        // This use case will only be applicable when custom short urls are implemented.
        @DisplayName("shortenUrl should return a url with a different short url because the short url already exists as a custom short url for another long url.")
        void shortenUrl_shouldReturnUrlWithAnotherShortUrl_WhenShortUrlAlreadyExists() {
                Url alreadyExistingtUrl = UrlCreator.createValidGithubUrl();

                BDDMockito.when(urlRepositoryMock.findByShortUrl(ArgumentMatchers.eq(
                                UrlCreator.createValidGithubUrl().getShortUrl())))
                                .thenReturn(Optional.of(UrlCreator.createValidGithubUrl()));

                Url urlToTest = urlService.shortenUrl(alreadyExistingtUrl.getUrl(), "test", EngineType.HASHING);

                // Assertions
                Assertions.assertThat(urlToTest).isNotNull();
                Assertions.assertThat(urlToTest.getShortUrl()).isNotEqualTo(alreadyExistingtUrl.getShortUrl());
                Assertions.assertThat(urlToTest.getUrl()).isEqualTo(alreadyExistingtUrl.getUrl());

        }

        @Test
        @DisplayName("shortenUrl should throw IllegalArgumentException when long url is empty.")
        void shortenUrl_throwsIllegalArgumentException_WhenLongUrlIsEmpty() {
                // Assertions
                Assertions.assertThatThrownBy(() -> urlService.shortenUrl("", "", EngineType.HASHING))
                                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("shortenUrl should throw IllegalArgumentException when long url is null.")
        void shortenUrl_throwsIllegalArgumentException_WhenLongUrlIsNull() {
                // Assertions
                Assertions.assertThatThrownBy(() -> urlService.shortenUrl(null, "", EngineType.HASHING))
                                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("shortenUrl should throw RecaptchaValidationException when recaptchaToken is empty.")
        void shortenUrl_throwsRecaptchaValidationException_WhenRecaptchaTokenIsEmpty() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();

                // Assertions
                Assertions.assertThatThrownBy(() -> urlService.shortenUrl(expectedUrl.getUrl(), "", EngineType.HASHING))
                                .isInstanceOf(RecaptchaValidationException.class);
        }

        @Test
        @DisplayName("shortenUrl should throw RecaptchaValidationException when recaptchaToken is null.")
        void shortenUrl_throwsRecaptchaValidationException_WhenRecaptchaTokenIsNull() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();

                // Assertions
                Assertions.assertThatThrownBy(
                                () -> urlService.shortenUrl(expectedUrl.getUrl(), null, EngineType.HASHING))
                                .isInstanceOf(RecaptchaValidationException.class);
        }

        @Test
        @DisplayName("shortenUrl should throw RecaptchaValidationException when recaptchaToken is invalid.")
        void shortenUrl_throwsRecaptchaValidationException_WhenRecaptchaTokenIsInvalid() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();

                // Assertions
                Assertions.assertThatThrownBy(
                                () -> urlService.shortenUrl(expectedUrl.getUrl(), "invalid", EngineType.HASHING))
                                .isInstanceOf(RecaptchaValidationException.class);
        }

        @Test
        @DisplayName("resolve should return url when short url is valid.")
        void resolve_shouldReturnUrl_WhenShortUrlIsValid() {
                Url expectedUrl = UrlCreator.createValidGithubUrl();
                BDDMockito.when(urlRepositoryMock.findByShortUrl(ArgumentMatchers.anyString()))
                                .thenReturn(Optional.of(expectedUrl));
                Url resolvedUrl = urlService.resolve(expectedUrl.getShortUrl());

                // Assertions
                Assertions.assertThat(resolvedUrl).isNotNull();
                Assertions.assertThat(resolvedUrl.getUrl()).isNotBlank();
                Assertions.assertThat(resolvedUrl.getUrl()).isEqualTo(expectedUrl.getUrl());
        }

        @Test
        @DisplayName("resolve should return empty url when short url is not exists.")
        void resolve_shouldReturnEmptyUrl_WhenShortUrlNotExists() {
                Url resolvedUrl = urlService.resolve("test123");

                // Assertions
                Assertions.assertThat(resolvedUrl).isNotNull();
                Assertions.assertThat(resolvedUrl.getUrl()).isBlank();
        }

        @Test
        @DisplayName("resolve should throw IllegalArgumentException when short url is empty.")
        void resolve_throwsIllegalArgumentException_WhenShortUrlIsEmpty() {
                // Assertions
                Assertions.assertThatThrownBy(() -> urlService.resolve(""))
                                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("resolve should throw IllegalArgumentException when short url is null.")
        void resolve_throwsIllegalArgumentException_WhenShortUrlIsNull() {
                // Assertions
                Assertions.assertThatThrownBy(() -> urlService.resolve(null))
                                .isInstanceOf(IllegalArgumentException.class);
        }
}
