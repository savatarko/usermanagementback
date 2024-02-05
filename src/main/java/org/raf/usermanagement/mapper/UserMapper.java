package org.raf.usermanagement.mapper;

import org.raf.usermanagement.domain.User;
import org.raf.usermanagement.dtos.user.CreateUserDto;
import org.raf.usermanagement.dtos.user.UserDto;
import org.raf.usermanagement.security.PasswordSecurity;

public class UserMapper {
    public static User userCreateDtoToUser(CreateUserDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.name);
        user.setSurname(userCreateDto.surname);
        user.setEmail(userCreateDto.email);
        user.setPassword(PasswordSecurity.toHexString(PasswordSecurity.getSHA(userCreateDto.password)));
        return user;
    }

    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.name = user.getName();
        userDto.surname = user.getSurname();
        userDto.email = user.getEmail();
        return userDto;
    }
}
