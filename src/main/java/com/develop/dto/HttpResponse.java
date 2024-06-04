package com.develop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

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
        Map<?, ?> data
) {
}
