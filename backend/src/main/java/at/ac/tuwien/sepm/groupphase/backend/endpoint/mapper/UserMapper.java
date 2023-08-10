package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserAdminDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * The interface User mapper.
 */
@Mapper
public interface UserMapper {
    /**
     * Application user to dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    UserDto applicationUserToDto(ApplicationUser user);

    /**
     * ApplicationUser to UserAdminDto.
     *
     * @param users the list of users
     * @return a list of user admin DTOs
     */
    List<UserAdminDto> applicationUserToUserAdminDto(List<ApplicationUser> users);

    /**
     * Application user to profile dto user profile dto.
     *
     * @param user the user
     * @return the user profile dto
     */
    UserProfileDto applicationUserToProfileDto(ApplicationUser user);
}
