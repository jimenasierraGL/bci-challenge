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
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

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

    def "Should post an User with HttpStatus.CREATED"() {
        given: "An userDto and Errors"
        UserDto userDto = TestObjectBuilder.buildUserDto()
        BeanPropertyBindingResult error = new BeanPropertyBindingResult(userDto, "userDto")

        when: "Post user"
        ResponseEntity responseEntity = userController.postUser(userDto, error)

        then: "Return an User"
        1 * userService.createUser(userDto) >> userFromDB

        responseEntity.getStatusCode() == HttpStatus.CREATED
        responseEntity.getBody() == userFromDB
    }

    def "Should handle an ApiException and return an ErrorResponseDto with HttpStatus.BAD_REQUEST"() {
        given: "an userDto and Errors"
        UserDto userDto = TestObjectBuilder.buildUserDto()
        BeanPropertyBindingResult error = new BeanPropertyBindingResult(userDto, "userDto")

        when: "Post user"
        ResponseEntity responseEntity = userController.postUser(userDto, error)

        then: "Return an ErrorResponseDto"
        1 * userService.createUser(userDto) >> { throw new BadRequestException(MESSAGE)}
        responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST
        ErrorResponseDto.cast(responseEntity.getBody()).getError().every {
            it.getCode() == responseEntity.getStatusCode().value() &&
            it.getDetail() == MESSAGE}
    }

    def "Should return an User from a JWToken with HttpStatus.OK"() {
        given: "A HttpServletRequest"
        HttpServletRequest request = new MockHttpServletRequest()
        request.setParameter("Authorization", TOKEN)

        when: "Gets an user"
        ResponseEntity responseEntity = userController.getUser(request)

        then: "Return an User"
        1 * userService.getUser(_) >> userFromDB

        responseEntity.getStatusCode() == HttpStatus.OK
        responseEntity.getBody() == userFromDB
    }

    def "Should handle an ApiException and return an ErrorResponseDto with HttpStatus.NOT_FOUND"() {
        given: "A HttpServletRequest"
        HttpServletRequest request = new MockHttpServletRequest()
        request.setParameter("Authorization", TOKEN)

        when: "Gets an user"
        ResponseEntity responseEntity = userController.getUser(request)

        then: "Return an ErrorResponseDto"
        1 * userService.getUser(_) >> { throw new NotFoundException(MESSAGE)}
        responseEntity.getStatusCode() == HttpStatus.NOT_FOUND
        ErrorResponseDto.cast(responseEntity.getBody()).getError().every {
            it.getCode() == responseEntity.getStatusCode().value() &&
                    it.getDetail() == MESSAGE}
    }
}