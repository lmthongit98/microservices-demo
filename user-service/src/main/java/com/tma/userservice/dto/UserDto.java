package com.tma.userservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private String username;
    private String email;
    private Set<String> roles;
}
