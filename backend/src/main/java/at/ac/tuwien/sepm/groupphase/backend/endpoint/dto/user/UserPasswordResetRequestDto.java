package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


/**
 * The DTO for reseting a User Password.
 */
public class UserPasswordResetRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (email == null || email.isBlank()) {
            return List.of("Email is required");
        }
        //regex for email
        final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        //regex for password, atleast 8 characters, atleast 1 uppercase, atleast 1 lowercase, atleast 1 number
        if (!email.matches(emailRegex)) {
            errors.add("{Invalid email format.}");
        }
        return errors;
    }

    @Override
    public String toString() {
        return "UserResetPasswordRequestDto{"
            + "email='" + email + '\''
            + '}';
    }
}
