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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserDto user, Errors error) {
        log.info("Incoming request: POST User {}", user);
        ErrorProcessor.processErrors(error);
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("Incoming request: GET User");
        return new ResponseEntity<>(userService.getUser(token), HttpStatus.OK);
    }
}
