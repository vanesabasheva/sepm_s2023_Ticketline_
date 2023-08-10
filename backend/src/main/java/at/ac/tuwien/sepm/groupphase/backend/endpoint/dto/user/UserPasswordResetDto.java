package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * * The DTO for updating a User's password after requesting a password reset.
 */
public class UserPasswordResetDto {

    @NotBlank(message = "password is required")
    private String password;

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (password == null || password.isBlank()) {
            return List.of("password is required");
        }
        //regex for password, atleast 8 characters, atleast 1 uppercase, atleast 1 lowercase, atleast 1 number
        final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\W]{8,}$";

        if (!password.matches(passwordRegex)) {
            errors.add("{Invalid password format. Password must be at least 8 characters long,"
                + " contain at least 1 uppercase letter, 1 lowercase letter and 1 number}");
        }
        return errors;
    }

    @Override
    public String toString() {
        return "UserNewPasswordDto{"
            + "password='" + password + '\''
            + '}';
    }
}
