package bci.challenge.utils;

import bci.challenge.exception.InvalidArgumentException;
import bci.challenge.model.dto.Error;
import bci.challenge.model.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorProcessor {

    public static void processErrors(Errors errors) {
        if (errors.hasErrors()){
            List<Error> errorList = errors.getAllErrors().stream().map(error -> Error.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .code(HttpStatus.BAD_REQUEST.value())
                    .detail(error.getDefaultMessage())
                    .build()).collect(Collectors.toList());
            throw new InvalidArgumentException("Invalid arguments", errorList);

        }
    }
}
