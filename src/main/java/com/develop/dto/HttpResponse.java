package com.develop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record HttpResponse(
        String timestamp,
        int statusCode,
        HttpStatus status,
        String message,
        String path,
        String developerMessage,
        String requestMethod,
        Map<?, ?> data) {}
