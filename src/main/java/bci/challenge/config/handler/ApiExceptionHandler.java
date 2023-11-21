package bci.challenge.config.handler;

import bci.challenge.exception.ApiException;
import bci.challenge.exception.BadRequestException;
import bci.challenge.exception.JWTException;
import bci.challenge.exception.NotFoundException;
import bci.challenge.model.dto.Error;
import bci.challenge.model.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

        @ExceptionHandler({BadRequestException.class, NotFoundException.class, JWTException.class})
        public ResponseEntity<ErrorResponseDto> handleApiException(ApiException exception) {
            ErrorResponseDto errors = new ErrorResponseDto(exception.getErrors());
            return new ResponseEntity<>(errors, exception.getHttpStatus());
        }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        List<Error> errors = Collections.singletonList(Error.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(exception.getMessage())
                .build());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errors);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
