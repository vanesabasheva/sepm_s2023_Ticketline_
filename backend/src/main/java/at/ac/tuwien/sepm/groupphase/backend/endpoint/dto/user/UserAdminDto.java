package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

/**
 * DTO for displaying user details for an admin.
 */
public class UserAdminDto {
    private int id;
    private boolean admin;
    private String firstName;
    private String lastName;
    private String email;
    private boolean locked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAdmin() {
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

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public static final class UserAdminDtoBuilder {
        private int id;
        private boolean admin;
        private String firstName;
        private String lastName;
        private String email;
        private boolean locked;

        private UserAdminDtoBuilder() {}

        public static UserAdminDtoBuilder anUserAdminDto() {
            return new UserAdminDtoBuilder();
        }

        public UserAdminDtoBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public UserAdminDtoBuilder withAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserAdminDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserAdminDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserAdminDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserAdminDtoBuilder withLocked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public UserAdminDto build() {
            UserAdminDto userAdminDto = new UserAdminDto();
            userAdminDto.setId(id);
            userAdminDto.setAdmin(admin);
            userAdminDto.setFirstName(firstName);
            userAdminDto.setLastName(lastName);
            userAdminDto.setEmail(email);
            userAdminDto.setLocked(locked);
            return userAdminDto;
        }
    }
}
