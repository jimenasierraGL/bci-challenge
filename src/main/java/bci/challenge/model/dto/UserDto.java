package bci.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private UUID id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private String name;
    @NotNull(message = "Email must not be null")
    @Pattern(regexp = "^[\\w-\\\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email format should be aaaaaa@undominio.algo")
    private String email;
    @NotNull(message = "Password must not be null")
    @Pattern(regexp = "(?=[a-zA-Z0-9]{8,12}$)(?=[^A-Z]*[A-Z][^A-Z]*$)(^(?:\\D*\\d){2}\\D*$)",
    message = "Password must have only one capital letter and only two numbers (not necessarily consecutive), in " +
            "combination of lowercase letters, maximum length of 12 and minimum 8.")
    @ToString.Exclude
    private String password;
    @ToString.Exclude
    private String token;
    private List<PhoneDto> phones;
}
