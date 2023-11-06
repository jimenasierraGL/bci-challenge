package bci.challenge.controller;

import bci.challenge.model.dto.ErrorResponseDto;
import bci.challenge.model.dto.UserDto;
import bci.challenge.exception.ApiException;
import bci.challenge.service.UserService;
import bci.challenge.utils.ErrorProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity postUser(@Valid @RequestBody UserDto user, Errors error) {
        log.info("Incoming request: POST User {}", user);
        try {
            ErrorProcessor.processErrors(error);
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        } catch (ApiException exception) {
            ErrorResponseDto errors = new ErrorResponseDto(exception.getErrors());
            return new ResponseEntity<>(errors, exception.getHttpStatus());
        }
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam String token) {

        log.info("Incoming request: GET User {}", token);
        try {
            return new ResponseEntity<>(userService.getUser(token), HttpStatus.OK);
        } catch (ApiException exception) {
            ErrorResponseDto errors = new ErrorResponseDto(exception.getErrors());
            return new ResponseEntity<>(errors, exception.getHttpStatus());
        }
    }
}
