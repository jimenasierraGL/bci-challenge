package bci.challenge.config.handler

import bci.challenge.exception.BadRequestException
import bci.challenge.model.dto.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import static bci.challenge.TestConstant.MESSAGE

class ApiExceptionHandlerTest extends Specification {

    ApiExceptionHandler apiExceptionHandler

    def setup() {
        given: "An ApiExceptionHandler"
        apiExceptionHandler = new ApiExceptionHandler()
    }

    def "Should return an ResponseEntity with ErrorResponseDto body for ApiException classes"() {
        given: "An BadRequestException"
        BadRequestException badRequestException = new BadRequestException(MESSAGE)

        when: "Handle api exception"
        ResponseEntity<ErrorResponseDto> responseEntity = apiExceptionHandler.handleApiException(badRequestException)

        then: "Map exception and return ResponseEntity with ErrorResponseDto body"
        badRequestException.getErrors().size() == responseEntity.getBody().getError().size()
        badRequestException.getHttpStatus().value() == responseEntity.getStatusCode().value()
    }

    def "Should return an ResponseEntity with ErrorResponseDto body Exception classes"() {
        given: "An BadRequestException"
        NullPointerException nullPointerException = new NullPointerException(MESSAGE)

        when: "Handle api exception"
        ResponseEntity<ErrorResponseDto> responseEntity = apiExceptionHandler.handleException(nullPointerException)

        then: "Map exception and return ResponseEntity with ErrorResponseDto body"
        nullPointerException.getMessage() == responseEntity.getBody().getError().get(0).getDetail()
        HttpStatus.INTERNAL_SERVER_ERROR.value() == responseEntity.getStatusCode().value()
    }
}
