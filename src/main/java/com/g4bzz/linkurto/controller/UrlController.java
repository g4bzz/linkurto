package com.g4bzz.linkurto.controller;

import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.dto.UrlPostRequestBody;
import com.g4bzz.linkurto.dto.UrlPostResponseBody;
import com.g4bzz.linkurto.exception.ValidationExceptionDetails;
import com.g4bzz.linkurto.mapper.UrlMapper;
import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.text.MessageFormat;

@RestController
@RequestMapping("/")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("shorten")
    @Operation(
        summary = "Shorten a URL",
        description = "Shorten a URL using the provided engine type",
        parameters = {
            @Parameter(name = "url", description = "The URL to shorten"),
            @Parameter(name = "engineType", description = "The type of URL shortener to use.")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UrlPostRequestBody.class),
            examples = {
                @ExampleObject(name = "Valid Url",
                        value = """
                                {
                                  "url": "https://www.tests.com/test123"
                                }"""),
            })
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Url shortened successfully", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UrlPostResponseBody.class),
                examples = {@ExampleObject(name = "Url Shortened",
                    value = """
                            {
                              "url": "https://www.test.com/test123",
                              "shortUrl": "bda2ab2",
                              "expirationDate": "2025-06-15T22:47:00.428922185"
                            }""")
                })
            ),
            @ApiResponse(responseCode = "400", description = "Invalid URL provided", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidationExceptionDetails.class),
                examples = {@ExampleObject(name = "Invalid Url",
                    value = """
                            {
                              "title": "Bad Request Exception: Check the documentation",
                              "status": 400,
                              "developerMessage": "org.springframework.web.bind.MethodArgumentNotValidException",
                              "timestamp": "2025-05-21T22:19:11.602161202",
                              "fields": {
                                "url": "The URL must be valid"
                              }
                            }""")
                })
            )
        }
    )
    public ResponseEntity<UrlPostResponseBody> shorten(@RequestBody @Valid UrlPostRequestBody urlPostRequestBody, @RequestParam(defaultValue = "HASHING") EngineType engineType) {
        Url shortenedUrl = urlService.shortenUrl(urlPostRequestBody.getUrl(), engineType);
        UrlPostResponseBody urlPostResponseBody = UrlMapper.INSTANCE.toUrlPostResponseBody(shortenedUrl);
        return new ResponseEntity<>(urlPostResponseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    @Operation(
        summary = "Resolve a URL",
        description = "Resolve a shortened URL to its original form",
        parameters = {
            @Parameter(name = "shortUrl", description = "The shortened URL",example = "bhsudu7")
        },
        responses = {
            @ApiResponse(responseCode = "301", description = "Redirects to the original URL",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "No resource found for the provided short URL",
               content = @Content(
                   schema = @Schema(implementation = NoResourceFoundException.class),
                   examples = {@ExampleObject(name = "No resource found for the provided short URL",
                        value = """
                                {
                                  "title": "Resource was not found. Check the documentation",
                                  "status": 404,
                                  "developerMessage": "org.springframework.web.servlet.resource.NoResourceFoundException",
                                  "timestamp": "2025-05-21T23:08:30.699541022",
                                  "resourcePath": ""
                                }""")
                   }
               )
            )
        }
    )
    public ResponseEntity<Url> resolve(@PathVariable String shortUrl) throws NoResourceFoundException {
        Url url = urlService.resolve(shortUrl);
        if (url == null || url.getId() == 0) {
            throw new NoResourceFoundException(HttpMethod.GET, MessageFormat.format("/{0}", shortUrl));
        }
        //301 Moved Permanently
        return ResponseEntity.status(301).location(URI.create(url.getUrl())).build();
    }
}
