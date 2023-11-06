package bci.challenge.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends ApiException{

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}