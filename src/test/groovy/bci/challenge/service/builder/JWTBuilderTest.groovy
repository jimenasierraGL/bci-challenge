package bci.challenge.service.builder

import bci.challenge.TestObjectBuilder
import bci.challenge.exception.ApiException
import bci.challenge.exception.JWTException
import bci.challenge.model.entity.User
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import static bci.challenge.TestConstant.MESSAGE

class JWTBuilderTest extends Specification {

    String secretKey
    ObjectMapper objectMapper
    JWTBuilder jwtBuilder;

    def setup(){
        given: "A secret key, an ObjectMapper and a JWTBuilder"
        secretKey = "aSecretKey"
        objectMapper = Mock(ObjectMapper)
        jwtBuilder = new JWTBuilder(objectMapper)
        ReflectionTestUtils.setField(jwtBuilder, "secretKey", "CMAb3Upye7JXFTVjyWdJnxLWWryf90psz2NUwgOrlEs=");

    }

    def "Should create a JWToken from an user"() {
        given: "An user"
        User user = TestObjectBuilder.buildUserFromDataBase()

        when: "Creates a JWToken"
        String token = jwtBuilder.createToken(user)

        then: "Return JWToken as String"
        1 * objectMapper.writeValueAsString(user)
        token.class == String.class
    }

    def "Should throw an ApiException when convert User to json"() {
        given: "An user"
        User user = TestObjectBuilder.buildUserFromDataBase()

        when: "Creates a JWToken"
        jwtBuilder.createToken(user)

        then: "Throws JsonProcessingException, processes it and throw ApiException"
        1 * objectMapper.writeValueAsString(user) >> { throw new JsonProcessingException(MESSAGE){}}
        ApiException exception = thrown(ApiException)
        exception.getMessage() == MESSAGE
        exception.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "Should throw an JWTException on other exceptions "() {
        given: "An user and a incorrect secretKey"
        User user = TestObjectBuilder.buildUserFromDataBase()
        secretKey = "aIncorrectSecretKey"

        when: "Creates a JWToken"
        jwtBuilder.createToken(user)

        then: "Throws Exception, processes it and throw JWTException"
        1 * objectMapper.writeValueAsString(user) >> { throw new JWTException(MESSAGE)}
        JWTException exception = thrown(JWTException)
        exception.getMessage() == "JWT creation failed: " + MESSAGE
        exception.getHttpStatus() == HttpStatus.FORBIDDEN
    }

    def "Should get an user from a JWToken"() {
        given: "An token"
        String token = "dG9rZW5IZWFkZXI=.IHsibmFtZSI6ImFOYW1lIiwiZW1haWwiOiJhbkVtYWlsIn0=.dG9rZW5TaWdu"

        when: "Decodes token and get user from body"
        User user = jwtBuilder.getUserFromJWT(token)

        then:
        1 * objectMapper.readValue(_, User.class) >> TestObjectBuilder.buildUserFromDataBase()
        user.class == User.class
    }

    def "Should throw an JWTException when fails"() {
        given: "An incorrect token"
        String token = "an.Incorrect.Token"

        when: "Gets user from a JWToken"
        jwtBuilder.getUserFromJWT(token)

        then: "Throws Exception, processes it and throw JWTException"
        JWTException exception = thrown(JWTException)
        exception.getMessage() == "JWT creation failed: Impossible modulus [1]"
        exception.getHttpStatus() == HttpStatus.FORBIDDEN
    }
}
