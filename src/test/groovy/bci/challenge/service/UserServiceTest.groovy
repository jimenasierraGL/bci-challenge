package bci.challenge.service

import bci.challenge.TestObjectBuilder
import bci.challenge.exception.ApiException
import bci.challenge.model.dto.UserDto
import bci.challenge.model.entity.User
import bci.challenge.persistence.UserRepository
import bci.challenge.service.builder.JWTBuilder
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import static bci.challenge.TestConstant.*

class UserServiceTest extends Specification{

    UserRepository userRepository
    JWTBuilder jwtBuilder
    PasswordEncoder passwordEncoder
    UserService userService

    def setup() {
        given: "An userService, a passwordEncoder and a mocked userRepository and jwtBuilder"
        userRepository = Mock(UserRepository)
        jwtBuilder = Mock(JWTBuilder)
        passwordEncoder = Mock(PasswordEncoder)
        userService = new UserService(userRepository, jwtBuilder, passwordEncoder)

    }

    def "Should create an user successfully"(){

        given: "An user from dto input"
        UserDto userDto = TestObjectBuilder.buildUserDto()

        when: "Creating user entity"
        def newUser = userService.createUser(userDto)


        then: "Saves and return the user"
        1 * userRepository.findByEmail(userDto.getEmail()) >> Optional.empty()
        1 * passwordEncoder.encode(userDto.getPassword())
        1 * jwtBuilder.createToken(_)
        1 * userRepository.save(_) >> TestObjectBuilder.buildUserFromDataBase()

        newUser.getEmail() == userDto.getEmail()
        newUser.getName() == userDto.getName()
        newUser.getIsActive() == true
        newUser.getId() != null
        newUser.getPhones().size() == userDto.getPhones().size()
        newUser.getCreated() == LOCAL_DATE_TIME_NOW
        newUser.getLastLogin() == LOCAL_DATE_TIME_NOW
    }

    def "Should throw BadRequestException for duplicate user"() {

        given: "An user from dto input"
        UserDto userDto = TestObjectBuilder.buildUserDto()

        when: "Creating user entity"
        userService.createUser(userDto)


        then: "Throws BadRequestException because user is duplicated"
        1 * userRepository.findByEmail(userDto.getEmail()) >> Optional.of(new User())
        ApiException exception = thrown(ApiException)
        exception.getMessage() == "User " + userDto.getEmail() + " already exists"
        exception.getHttpStatus() == HttpStatus.BAD_REQUEST
    }

    def "Should return an user from data base"() {
        given: "A JWT token"
        String token = TOKEN
        User jwtUser = TestObjectBuilder.buildUser()

        when: "Getting an user"
        def user = userService.getUser(token)

        then: "Gets user from data bse and returns it"
        1 * jwtBuilder.getUserFromJWT(TOKEN) >> jwtUser
        1 * userRepository.findById(jwtUser.getId())  >> Optional.of(TestObjectBuilder.buildUserFromDataBase())
        1 * jwtBuilder.createToken(_) >> "aNewToken"
        1 * userRepository.save(_) >> TestObjectBuilder.buildUserWithNewToken()

        user.getToken() != token
        user.getLastLogin() != jwtUser.getLastLogin()
    }
}
