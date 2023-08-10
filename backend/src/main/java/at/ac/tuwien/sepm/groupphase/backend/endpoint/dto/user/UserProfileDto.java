package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDto {
    private int id;
    private boolean admin;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer points;
    private String passwordResetToken;
    private LocalDateTime passwordResetTs;
    private Boolean locked;
    private int failedLogin;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (email == null || email.isBlank() || firstName == null || email.isBlank()
            || lastName == null || lastName.isBlank()) {
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
        return errors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTs() {
        return passwordResetTs;
    }

    public void setPasswordResetTs(LocalDateTime passwordResetTs) {
        this.passwordResetTs = passwordResetTs;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public int getFailedLogin() {
        return failedLogin;
    }

    public void setFailedLogin(int failedLogin) {
        this.failedLogin = failedLogin;
    }

}
