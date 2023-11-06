package bci.challenge.exception;

import bci.challenge.model.dto.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
    private List<Error> errors;

    public ApiException(String message, HttpStatus httpStatus, List<Error> errors) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.errors = buildError();
    }

    public ApiException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errors = buildError();
    }

    private List<Error> buildError() {
        return Collections.singletonList(Error.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .code(httpStatus.value())
                .detail(message)
                .build());
    }
}
