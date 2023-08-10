package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenizer {

    private final SecurityProperties securityProperties;
    @Autowired
    private NotUserRepository notUserRepository;


    public JwtTokenizer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * creates a JWT token for the given user and roles.
     *
     * @param user  the user
     * @param roles the roles
     * @return the JWT token
     */
    public String getAuthToken(String user, List<String> roles) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();

        Optional<ApplicationUser> applicationUser = notUserRepository.findApplicationUsersByEmail(user);
        if (applicationUser.isEmpty()) {
            throw new FatalException("Couldn't find user by Email");
        }
        JwtBuilder token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getJwtType())
            .setIssuer(securityProperties.getJwtIssuer())
            .setAudience(securityProperties.getJwtAudience())
            .setSubject(user)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
            .claim("rol", roles)
            .claim("user", applicationUser.get().getId());

        return securityProperties.getAuthTokenPrefix() + token.compact();
    }
}
