package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class UserPasswordChangeDto {
    @NotBlank(message = "password is required")
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public List<String> validate() {
        if (oldPassword == null || oldPassword.isBlank()) {
            return List.of("Old password is required");
        }
        if (newPassword1 == null || newPassword1.isBlank()) {
            return List.of("New password is required");
        }
        if (newPassword2 == null || newPassword2.isBlank()) {
            return List.of("Confirmation of new password is required");
        }
        // regex for password, at least 8 characters, at least 1 uppercase, at least 1 lowercase, at least 1 number
        final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\W]{8,}$";

        if (!newPassword1.matches(passwordRegex)) {
            return List.of("Password must be at least 8 characters long, "
                + "contain at least 1 uppercase letter, 1 lowercase letter and 1 number");
        } else if (!newPassword1.equals(newPassword2)) {
            return List.of("New passwords do not match");
        }
        List<String> errors = new ArrayList<>();
        return errors;
    }
}
