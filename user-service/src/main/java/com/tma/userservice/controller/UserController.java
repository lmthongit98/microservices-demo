package com.tma.userservice.controller;

import com.tma.userservice.dto.UserDto;
import com.tma.userservice.entity.User;
import com.tma.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PreAuthorize("#oauth2.hasScope('write') && hasRole('ADMIN')")
    @PostMapping("/add")
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PreAuthorize("#oauth2.hasScope('read') && hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAll() {
        logger.info("AccountService Controller: get all accounts");
        return userService.getAll();
    }

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @PostAuthorize("returnObject.body.username == authentication.name || hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable(name = "id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('write') && hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
    }

    @PreAuthorize("#oauth2.hasScope('write') && hasRole('ADMIN')")
    @PutMapping
    public void update(@RequestBody UserDto userDto) {
        userService.update(userDto);
    }

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/me")
    public Principal me(Principal principal, @RequestHeader("Authorization") String bearerToken) {
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/info")
    public UserDto getUserInfo(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

}
