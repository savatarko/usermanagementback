package org.raf.usermanagement.service;

import org.raf.usermanagement.dtos.*;

import java.util.List;

public interface UserService {

    void createUser(String jwt, CreateUserDto createUserDto);

    TokenResponseDto login(LoginDto loginDto);
    ReadUsersDto getUsers(String jwt);
    void changeUser(String jwt, ChangeUserDto changeUserDto);
    void deleteUser(String jwt, String email);
}
