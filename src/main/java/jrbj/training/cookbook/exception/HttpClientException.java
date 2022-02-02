package jrbj.training.cookbook.exception;

import lombok.Getter;

/**
 * Handling exceptions due to client errors (typical bad requests), noting error category and exact problem
 */
public class HttpClientException extends RuntimeException {
    @Getter
    private final String problemCode;

    public HttpClientException(String problemCode, String messageFormat, Object... messageFormatArguments) {
        super(String.format(messageFormat, messageFormatArguments));
        this.problemCode = problemCode;
    }
}
