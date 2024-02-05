package org.raf.usermanagement.controller;

import org.raf.usermanagement.dtos.user.*;
import org.raf.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/read")
    public ResponseEntity<ReadUsersDto> readUsers(@RequestHeader("Authorization") String jwt){
        return new ResponseEntity<>(userService.getUsers(jwt), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String jwt, @RequestBody CreateUserDto createUserDto){
        userService.createUser(jwt, createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeUser(@RequestHeader("Authorization") String jwt, @RequestBody ChangeUserDto changeUserDto){
        userService.changeUser(jwt, changeUserDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String jwt, @PathVariable String email){
        userService.deleteUser(jwt, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto){
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.CREATED);
    }
}
