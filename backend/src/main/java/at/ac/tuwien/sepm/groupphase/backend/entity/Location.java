package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer postalCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;


    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "Location{"
            +
            "id=" + id
            +
            ", postalCode=" + postalCode
            +
            ", street='" + street + '\''
            +
            ", city='" + city + '\''
            +
            ", country='" + country + '\''
            +

            '}';
    }


    public static final class LocationBuilder {
        private Integer id;
        private Integer postalCode;
        private String street;
        private String city;
        private String country;
        private ApplicationUser user;

        private LocationBuilder() {
        }

        public static LocationBuilder aLocation() {
            return new LocationBuilder();
        }

        public LocationBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public LocationBuilder withPostalCode(Integer postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public LocationBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public LocationBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public Location build() {
            Location location = new Location();
            location.setId(id);
            location.setPostalCode(postalCode);
            location.setStreet(street);
            location.setCity(city);
            location.setCountry(country);
            location.setUser(user);
            return location;
        }
    }
}
