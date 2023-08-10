package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * The DTO for registering a new User.
 */
public class UserRegisterDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Admin status is required")
    private boolean isAdmin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * validate the user register dto.
     *
     * @return the list of errors
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (email == null || email.isBlank() || firstName == null || email.isBlank()
            || lastName == null || lastName.isBlank() || password == null || password.isBlank()) {
            return List.of("All fields are required");
        }
        //regex for email
        final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        //regex for password, atleast 8 characters, atleast 1 uppercase, atleast 1 lowercase, atleast 1 number
        final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\W]{8,}$";
        //regex for name
        final String nameRegex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";

        if (!email.matches(emailRegex)) {
            errors.add("{Invalid email format.}");
        }
        if (!firstName.matches(nameRegex)) {
            errors.add("{Invalid first name format. First name must be at least 2 characters long and contain only letters}");
        }
        if (!lastName.matches(nameRegex)) {
            errors.add("{Invalid last name format. Last name must be at least 2 characters long and contain only letters}");
        }
        if (!password.matches(passwordRegex)) {
            errors.add("{Invalid password format. Password must be at least 8 characters long,"
                + " contain at least 1 uppercase letter, 1 lowercase letter and 1 number}");
        }
        return errors;
    }

    public String toString() {
        return "UserRegisterDto{"
            + "email='" + email + '\''
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", isAdmin='" + isAdmin + '\''
            // dont include password
            + '}';
    }

    public static final class UserRegisterDtoBuilder {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private boolean isAdmin;

        private UserRegisterDtoBuilder() {
        }

        public static UserRegisterDtoBuilder anUserRegisterDto() {
            return new UserRegisterDtoBuilder();
        }

        public UserRegisterDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserRegisterDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserRegisterDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRegisterDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserRegisterDtoBuilder withIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public UserRegisterDto build() {
            UserRegisterDto userRegisterDto = new UserRegisterDto();
            userRegisterDto.setEmail(email);
            userRegisterDto.setFirstName(firstName);
            userRegisterDto.setLastName(lastName);
            userRegisterDto.setPassword(password);
            userRegisterDto.setIsAdmin(isAdmin);
            return userRegisterDto;
        }
    }
}