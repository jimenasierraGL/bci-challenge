package bci.challenge

import bci.challenge.exception.BadRequestException
import bci.challenge.model.dto.Error
import bci.challenge.model.dto.ErrorResponseDto
import bci.challenge.model.dto.PhoneDto
import bci.challenge.model.dto.UserDto
import bci.challenge.model.entity.Phone
import bci.challenge.model.entity.User
import bci.challenge.model.mapper.PhoneMapper
import org.springframework.http.HttpStatus
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.ObjectError

import java.sql.Timestamp
import java.time.LocalDateTime

import static bci.challenge.TestConstant.*

class TestObjectBuilder {
    static UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName(NAME)
        userDto.setEmail(EMAIL)
        userDto.setPassword(PASSWORD)
        userDto.setPhones(buildPhoneDtoList())
        return userDto
    }

    static List<PhoneDto> buildPhoneDtoList() {
        PhoneDto phoneDto = new PhoneDto()
        phoneDto.setNumber(PHONE_NUMBER)
        phoneDto.setCityCode(CITY_CODE)
        phoneDto.setCountryCode(COUNTRY_CODE)
        return Collections.singletonList(phoneDto)
    }

    static List<Phone> buildPhoneList() {
        return PhoneMapper.INSTANCE.mapPhoneDtoListToPhoneList(buildPhoneDtoList())
    }

    static User buildUser() {
        User user = new User();
        user.setName(NAME)
        user.setEmail(EMAIL)
        user.setPassword(PASSWORD)
        user.setIsActive(true)
        user.setCreated(LOCAL_DATE_TIME_NOW)
        user.setLastLogin(LOCAL_DATE_TIME_NOW)
        user.setToken(TOKEN)
        user.setPhones(buildPhoneList())
        return user
    }

    static User buildUserFromDataBase() {
        User user = buildUser()
        user.setId(UUID.randomUUID())
        return user
    }


    static User buildUserWithNewToken() {
        User user = buildUserFromDataBase()
        user.setToken("aNewToken")
        user.setLastLogin(LocalDateTime.now())
        return user
    }

    static ErrorResponseDto buildErrorDto() {
        Error error = new Error(new Timestamp(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(), "aMessage")
        List<Error> errorList = Collections.singletonList(error)
        return new ErrorResponseDto(errorList)
    }
}
