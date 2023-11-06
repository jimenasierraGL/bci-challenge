package bci.challenge.service;

import bci.challenge.exception.ApiException;
import bci.challenge.exception.BadRequestException;
import bci.challenge.exception.JWTException;
import bci.challenge.exception.NotFoundException;
import bci.challenge.model.dto.PhoneDto;
import bci.challenge.model.dto.UserDto;
import bci.challenge.model.entity.Phone;
import bci.challenge.model.entity.User;
import bci.challenge.model.mapper.PhoneMapper;
import bci.challenge.model.mapper.UserMapper;
import bci.challenge.persistence.UserRepository;
import bci.challenge.service.builder.JWTBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JWTBuilder jwtBuilder;
    private final PasswordEncoder passwordEncoder;
    @Transactional(rollbackOn = Exception.class)
    public UserDto createUser(UserDto user) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
            if (userOpt.isPresent()) {
                throw new BadRequestException("User " + user.getEmail() + " already exists");
            }

            User newUser = User.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .created(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                    .build();
            buildPhones(user.getPhones(), newUser);

            String token = jwtBuilder.createToken(newUser);
            newUser.setToken(token);

            return UserMapper.INSTANCE.mapUserToUserDto(userRepository.save(newUser));
        } catch (BadRequestException | JWTException exception) {
            throw new ApiException(exception.getMessage(), exception.getHttpStatus());
        }
        catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    private void buildPhones(List<PhoneDto> phones, User newUser) {
        List<Phone> phoneList = PhoneMapper.INSTANCE.mapPhoneDtoListToPhoneList(phones);
        phoneList.forEach(phone -> phone.setUser(newUser));
        newUser.setPhones(phoneList);
    }

    public UserDto getUser(String token) {
        try {

            User userFromJWT = jwtBuilder.getUserFromJWT(token);

            User user = userRepository.findByIdOrEmail(userFromJWT.getId(), userFromJWT.getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            String newToken = jwtBuilder.createToken(user);
            user.setLastLogin(LocalDateTime.now());
            user.setToken(newToken);

            return UserMapper.INSTANCE.mapUserToUserDto(userRepository.save(user));
        } catch (NotFoundException | JWTException exception) {
            throw new ApiException(exception.getMessage(), exception.getHttpStatus());
        }
        catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }
}
