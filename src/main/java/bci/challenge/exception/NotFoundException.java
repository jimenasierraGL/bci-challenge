package bci.challenge.exception;

import bci.challenge.model.dto.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Collections;

@Getter
public class NotFoundException extends ApiException{

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}