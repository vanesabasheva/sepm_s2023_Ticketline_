package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import java.time.LocalDateTime;

public class SimpleUserDto {
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

