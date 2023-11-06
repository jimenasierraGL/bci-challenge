package bci.challenge.controller

import bci.challenge.TestObjectBuilder
import bci.challenge.exception.BadRequestException
import bci.challenge.exception.NotFoundException
import bci.challenge.model.dto.ErrorResponseDto
import bci.challenge.model.dto.UserDto
import bci.challenge.model.mapper.UserMapper
import bci.challenge.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

import static bci.challenge.TestConstant.*;

class UserControllerTest extends Specification {

    UserService userService;
    UserController userController;
    UserDto userFromDB;

    def setup() {
        userService = Mock(UserService)
        userController = new UserController(userService)
        userFromDB = UserMapper.INSTANCE.mapUserToUserDto(TestObjectBuilder.buildUserFromDataBase())
    }

    def "should post an User with HttpStatus.CREATED"() {
        given: "an userDto and Errors"
        UserDto userDto = TestObjectBuilder.buildUserDto()
        BeanPropertyBindingResult error = new BeanPropertyBindingResult(userDto, "userDto")

        when: "post user"
        ResponseEntity responseEntity = userController.postUser(userDto, error)

        then: "return an User"
        1 * userService.createUser(userDto) >> userFromDB

        responseEntity.getStatusCode() == HttpStatus.CREATED
        responseEntity.getBody() == userFromDB
    }

    def "should handle an ApiException and return an ErrorResponseDto with HttpStatus.BAD_REQUEST"() {
        given: "an userDto and Errors"
        UserDto userDto = TestObjectBuilder.buildUserDto()
        BeanPropertyBindingResult error = new BeanPropertyBindingResult(userDto, "userDto")

        when: "post user"
        ResponseEntity responseEntity = userController.postUser(userDto, error)

        then: "return an ErrorResponseDto"
        1 * userService.createUser(userDto) >> { throw new BadRequestException(MESSAGE)}
        responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST
        ErrorResponseDto.cast(responseEntity.getBody()).getError().every {
            it.getCode() == responseEntity.getStatusCode().value() &&
            it.getDetail() == MESSAGE}
    }

    def "should return an User from a JWToken with HttpStatus.OK"() {
        given: "a JWToken"
        String token = TOKEN

        when: "gets an user"
        ResponseEntity responseEntity = userController.getUser(token)

        then: "return an User"
        1 * userService.getUser(token) >> userFromDB

        responseEntity.getStatusCode() == HttpStatus.OK
        responseEntity.getBody() == userFromDB
    }

    def "should handle an ApiException and return an ErrorResponseDto with HttpStatus.NOT_FOUND"() {
        given: "a JWToken"
        String token = TOKEN

        when: "gets an user"
        ResponseEntity responseEntity = userController.getUser(token)

        then: "return an ErrorResponseDto"
        1 * userService.getUser(token) >> { throw new NotFoundException(MESSAGE)}
        responseEntity.getStatusCode() == HttpStatus.NOT_FOUND
        ErrorResponseDto.cast(responseEntity.getBody()).getError().every {
            it.getCode() == responseEntity.getStatusCode().value() &&
                    it.getDetail() == MESSAGE}
    }
}