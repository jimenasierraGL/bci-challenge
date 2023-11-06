package bci.challenge.exception;

import bci.challenge.model.dto.Error;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidArgumentException  extends ApiException{
    public InvalidArgumentException(String message, List<Error> errors) {
        super(message, HttpStatus.BAD_REQUEST, errors);
    }
}