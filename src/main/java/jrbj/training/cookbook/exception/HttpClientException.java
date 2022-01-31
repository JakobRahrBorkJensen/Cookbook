package jrbj.training.cookbook.exception;

public class HttpClientException extends RuntimeException {
    private final String problemCode;

    public HttpClientException(String problemCode, String messageFormat, Object... messageFormatArguments) {
        super(String.format(messageFormat, messageFormatArguments));
        this.problemCode = problemCode;
    }

    public String getProblemCode() {
        return this.problemCode;
    }
}
