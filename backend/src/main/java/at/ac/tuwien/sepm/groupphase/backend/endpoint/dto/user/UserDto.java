package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * UserDTo containing all information about a user.
 */
public class UserDto {
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
    private Set<Order> orders;
    private Set<PaymentDetail> paymentDetails;
    private Set<Reservation> reservations;
    private Set<Location> locations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Set<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
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

    public LocalDateTime getPasswordResetTs() {
        return passwordResetTs;
    }

    public void setPasswordResetTs(LocalDateTime passwordResetTs) {
        this.passwordResetTs = passwordResetTs;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    @Override
    public String toString() {
        return "UserDto{"
            +
            "id=" + id
            +
            ", admin=" + admin
            +
            ", firstName='" + firstName + '\''
            +
            ", lastName='" + lastName + '\''
            +
            ", email='" + email + '\''
            +
            ", password='" + password + '\''
            +
            ", points=" + points
            +
            ", passwordResetToken='" + passwordResetToken + '\''
            +
            ", passwordResetTs=" + passwordResetTs
            +
            ", locked=" + locked
            +
            ", orders=" + orders
            +
            ", paymentDetails=" + paymentDetails
            +
            ", reservations=" + reservations
            +
            ", locations=" + locations
            +
            '}';
    }
}
