package hexlet.code.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    @NotBlank
    @Size(min = 1)
    private String firstName;

    @NotBlank
    @Size(min = 1)
    private String lastName;

    @Email
    private String email;

    @Size(min = 3, max = 100)
    private String password;
}