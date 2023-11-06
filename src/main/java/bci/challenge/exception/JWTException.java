package bci.challenge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JWTException extends ApiException{
    public JWTException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
