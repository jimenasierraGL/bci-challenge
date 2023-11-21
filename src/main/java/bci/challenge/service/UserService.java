package bci.challenge.service;

import bci.challenge.exception.BadRequestException;
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

        newUser = userRepository.save(newUser);
        UserDto userDto = UserMapper.INSTANCE.mapUserToUserDto(newUser);
        String token = jwtBuilder.createToken(newUser);
        userDto.setToken(token);
        return userDto;
    }

    private void buildPhones(List<PhoneDto> phones, User newUser) {
        List<Phone> phoneList = PhoneMapper.INSTANCE.mapPhoneDtoListToPhoneList(phones);
        phoneList.forEach(phone -> phone.setUser(newUser));
        newUser.setPhones(phoneList);
    }

    public UserDto getUser(String token) {

        User userFromJWT = jwtBuilder.getUserFromJWT(token);

        User user = userRepository.findById(userFromJWT.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        user = userRepository.save(user);

        String newToken = jwtBuilder.createToken(user);
        UserDto userDto = UserMapper.INSTANCE.mapUserToUserDto(user);
        userDto.setToken(newToken);
        return userDto;
    }
}
