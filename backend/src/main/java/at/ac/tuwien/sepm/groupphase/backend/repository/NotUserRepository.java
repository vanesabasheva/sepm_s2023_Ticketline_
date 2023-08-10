package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NotUserRepository extends JpaRepository<ApplicationUser, Integer> {

    /**
     * find application user by id.
     *
     * @param id id of application user
     * @return application user
     */
    @EntityGraph(attributePaths = {
        "reservations",
        "reservations.ticket",
        "locations",
        "paymentDetails",
        "paymentDetails.orders",
        "orders",
    })
    ApplicationUser findApplicationUserById(Integer id);

    /**
     * Find all users.
     *
     * @return list of all users
     */
    List<ApplicationUser> findAll();

    /**
     * Find all users where locked is true.
     *
     * @return list of all locked users
     */
    @Query("SELECT DISTINCT u from ApplicationUser u WHERE u.locked = true")
    List<ApplicationUser> findAllByLockedTrue();

    /**
     * Find all users with location & paymentdetails JOINED (eagerly).
     *
     * @return list of all users
     */
    @Query("SELECT DISTINCT u FROM ApplicationUser u JOIN FETCH u.locations JOIN FETCH u.paymentDetails")
    List<ApplicationUser> findAllWithLocationsAndPaymentDetails();

    /**
     * Find user by id.
     *
     * @param id of user to find
     * @return user with given id
     */
    ApplicationUser getApplicationUserById(Integer id);

    /**
     * get application user by email.
     *
     * @param email of user to find
     * @return ApplicationUser with given email
     */
    Optional<ApplicationUser> findApplicationUsersByEmail(String email);


    /**
     * get application user by password reset token.
     *
     * @param token of user to find
     * @return ApplicationUser with given token
     */
    Optional<ApplicationUser> getApplicationUserByPasswordResetToken(String token);

    /**
     * update locked status of user.
     *
     * @param id     of user to update.
     * @param locked status to set
     */
    @Transactional
    @Modifying
    @Query("UPDATE ApplicationUser u SET u.locked = :locked WHERE u.id = :id")
    void updateUserLocked(@Param("id") Integer id, @Param("locked") boolean locked);

    /**
     * reset user login attempts.
     *
     * @param id of user to update.
     */
    @Transactional
    @Modifying
    @Query("UPDATE ApplicationUser u SET u.failedLogin = 0 WHERE u.id = :id")
    void resetUserLoginAttempts(@Param("id") Integer id);


}
