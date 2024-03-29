package jrbj.training.cookbook.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Problem object containing all relevant details of an encountered problem and how it should be responded to.
 *
 * Should only be invoked through static methods for each kind of problem, ie. bad requests.
 */
@JsonPropertyOrder({"id", "dateTime", "status", "title", "code", "detail"})
public class RestControllerProblem {
    @JsonProperty("date_time")
    private final String dateTime = Instant.now().toString();

    @Getter
    @JsonProperty("id")
    private final String id = UUID.randomUUID().toString();

    @Getter
    @JsonProperty("code")
    private final String code;

    @Getter
    @JsonProperty("status")
    private final int status;

    @JsonProperty("title")
    private final String title;

    @Getter
    @JsonProperty("details")
    private final List<String> details = new ArrayList<>();

    /**
     * Creates a problem instance of type bad request, that will return details of errors in request
     */
    public static RestControllerProblem badRequest(String code, List<String> details) {
        return new RestControllerProblem(
                String.format("bad-request.%s", code),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                details);
    }

    /**
     * Creates a problem instance of type internal server error, that will NOT return any inner details of what went wrong.
     */
    public static RestControllerProblem internalServerError() {
        return new RestControllerProblem(
                "internal-server-error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                Collections.emptyList());
    }

    private RestControllerProblem(String code, int status, String title, List<String> details) {
        this.code = code;
        this.status = status;
        this.title = title;
        this.details.addAll(details);
    }
}
