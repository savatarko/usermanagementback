package org.raf.usermanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.raf.usermanagement.db.PermissionRepository;
import org.raf.usermanagement.db.UserPermissionRepository;
import org.raf.usermanagement.db.UserRepository;
import org.raf.usermanagement.domain.Permission;
import org.raf.usermanagement.domain.User;
import org.raf.usermanagement.domain.UserPermission;
import org.raf.usermanagement.dtos.user.*;
import org.raf.usermanagement.exceptions.NoUserException;
import org.raf.usermanagement.exceptions.NotAuthorizedException;
import org.raf.usermanagement.exceptions.PermissionNotFoundException;
import org.raf.usermanagement.exceptions.UserAlreadyExistsException;
import org.raf.usermanagement.mapper.UserMapper;
import org.raf.usermanagement.security.MyTokenService;
import org.raf.usermanagement.security.PasswordSecurity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private UserPermissionRepository userPermissionRepository;
    private MyTokenService tokenService;

    public UserServiceImpl(UserRepository userRepository, PermissionRepository permissionRepository, UserPermissionRepository userPermissionRepository, MyTokenService tokenService) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.tokenService = tokenService;
    }

    @Override
    public void createUser(String jwt, CreateUserDto createUserDto) {
        if(userRepository.findUserByEmail(createUserDto.email).isPresent()){
            throw new UserAlreadyExistsException();
        }
        Long id = tokenService.parseToken(jwt).get("id", Long.class);
        if(userRepository.findById(id).isEmpty()){
            throw new NoUserException();
        }
        User admin = userRepository.findById(id).get();
        Permission tmppermission = permissionRepository.findPermissionByName("can_create_users").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(admin, tmppermission).isEmpty()){
            throw new NotAuthorizedException();
        }


        User user = UserMapper.userCreateDtoToUser(createUserDto);
        List<UserPermission> userPermissions = new ArrayList<>();
        userRepository.save(user);
        for(String permission : createUserDto.permissions){
            if(permissionRepository.findPermissionByName(permission).isEmpty()){
                throw new PermissionNotFoundException();
            }
            UserPermission userPermission = new UserPermission();
            userPermission.setUser(user);
            userPermission.setPermission(permissionRepository.findPermissionByName(permission).get());
            userPermissions.add(userPermission);
        }
        userPermissionRepository.saveAll(userPermissions);
    }

    @Override
    public TokenResponseDto login(LoginDto loginDto) {
        Claims claims = Jwts.claims();
        String pass = PasswordSecurity.toHexString(PasswordSecurity.getSHA(loginDto.password));
        if(userRepository.findUserByEmailAndPassword(loginDto.email, pass).isEmpty()){
            throw new NoUserException();
        }
        User user = userRepository.findUserByEmailAndPassword(loginDto.email, pass).get();
        claims.put("id", user.getId());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ReadUsersDto getUsers(String jwt) {
        Long id = tokenService.parseToken(jwt).get("id", Long.class);
        if(userRepository.findById(id).isEmpty()){
            throw new NoUserException();
        }
        User user = userRepository.findById(id).get();
        Permission permission = permissionRepository.findPermissionByName("can_read_users").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, permission).isEmpty()){
            throw new NotAuthorizedException();
        }
        List<User> users = userRepository.findAll();
        ReadUsersDto readUsersDto = new ReadUsersDto();
        for(User u : users){
            UserDto userDto = UserMapper.userToUserDto(u);
            userDto.permissions = userPermissionRepository.findUserPermissionsByUser(u).get().stream().map(userPermission -> userPermission.getPermission().getName()).toList();
            readUsersDto.users.add(userDto);
        }
        return readUsersDto;
    }

    @Override
    public void changeUser(String jwt, ChangeUserDto changeUserDto) {
        Long id = tokenService.parseToken(jwt).get("id", Long.class);
        if(userRepository.findById(id).isEmpty()){
            throw new NoUserException();
        }
        User user = userRepository.findById(id).get();
        Permission permission = permissionRepository.findPermissionByName("can_update_users").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, permission).isEmpty()){
            throw new NotAuthorizedException();
        }

        User userToChange = userRepository.findById(changeUserDto.id).orElseThrow(NoUserException::new);

        if(userRepository.findUserByEmail(changeUserDto.email).isPresent() && !userRepository.findUserByEmail(changeUserDto.email).get().getId().equals(changeUserDto.id)){
            throw new UserAlreadyExistsException();
        }

        List<Permission> permissions = permissionRepository.findAll();

        for(Permission p: permissions){
            boolean flag = changeUserDto.permissions.contains(p.getName());
            if(userPermissionRepository.findUserPermissionByUserAndPermission(userToChange, p).isEmpty() && flag){
                UserPermission userPermission = new UserPermission();
                userPermission.setUser(userToChange);
                userPermission.setPermission(p);
                userPermissionRepository.save(userPermission);
            }
            else if(!userPermissionRepository.findUserPermissionByUserAndPermission(userToChange, p).isEmpty() && !flag){
                userPermissionRepository.delete(userPermissionRepository.findUserPermissionByUserAndPermission(userToChange, p).get());
            }
        }

        //User userToChange = userRepository.findById(changeUserDto.id).orElseThrow(NoUserException::new);
        userToChange.setName(changeUserDto.name);
        userToChange.setSurname(changeUserDto.surname);
        userToChange.setEmail(changeUserDto.email);
        userRepository.save(userToChange);
    }

    @Override
    public void deleteUser(String jwt, String email) {
        Long id = tokenService.parseToken(jwt).get("id", Long.class);
        if(userRepository.findById(id).isEmpty()){
            throw new NoUserException();
        }
        User user = userRepository.findById(id).get();
        Permission permission = permissionRepository.findPermissionByName("can_delete_users").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, permission).isEmpty()){
            throw new NotAuthorizedException();
        }

        User userToDelete = userRepository.findUserByEmail(email).orElseThrow(NoUserException::new);

        if(userToDelete.equals(user)){
            throw new NotAuthorizedException();
        }

        userPermissionRepository.deleteAllByUser(userToDelete);

        userRepository.delete(userToDelete);
    }


}
