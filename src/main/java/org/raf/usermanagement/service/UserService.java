package org.raf.usermanagement.service;

import org.raf.usermanagement.dtos.user.*;

public interface UserService {

    void createUser(String jwt, CreateUserDto createUserDto);

    TokenResponseDto login(LoginDto loginDto);
    ReadUsersDto getUsers(String jwt);
    void changeUser(String jwt, ChangeUserDto changeUserDto);
    void deleteUser(String jwt, String email);
}
